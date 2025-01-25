package org.httpsrv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
import lombok.Getter;
import org.httpsrv.algorithms.BASE64;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.XOR;
import org.httpsrv.conf.Config;
import org.httpsrv.conf.var.RegionVar;
import org.httpsrv.data.Retcode;
import org.httpsrv.protobuf.ForceUpdateInfoOuterClass;
import org.httpsrv.protobuf.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;
import org.httpsrv.protobuf.QueryRegionListHttpRspOuterClass.QueryRegionListHttpRsp;
import org.httpsrv.protobuf.RegionInfoOuterClass.RegionInfo;
import org.httpsrv.protobuf.RegionSimpleInfoOuterClass.RegionSimpleInfo;
import org.httpsrv.protobuf.ResVersionConfigOuterClass.ResVersionConfig;
import org.httpsrv.protobuf.StopServerInfoOuterClass.StopServerInfo;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class Region {
    private String queryAllRegionResponse;
    private final Map<String, RegionMap> serverRegions = new HashMap<>();

    public Region() {
        var regionsConfig = new ArrayList<>(Config.getRegionVar().regions);
        List<RegionSimpleInfo> regions = new ArrayList<>();

        regionsConfig.forEach(region -> {
            var regionInfo =
                    RegionInfo.newBuilder()
                            .setUseGateserverDomainName(false)
                            .setGateserverIp(region.Ip)
                            .setGateserverPort(region.Port)
                            .setGameBiz(Config.getRegionVar().resource_options.game_biz)
                            .setDataUrl(Config.getRegionVar().resource_options.data_url)
                            .setResourceUrl(Config.getRegionVar().resource_options.resource_url)
                            .setResourceUrlBak(Config.getRegionVar().resource_options.resource_url_bak)
                            .setNextResourceUrl(Config.getRegionVar().resource_options.next_resource_url)
                            .setClientDataVersion(Config.getRegionVar().resource_options.client_data_version)
                            .setClientSilenceDataVersion(Config.getRegionVar().resource_options.client_silence_data_version)
                            .setClientVersionSuffix(Config.getRegionVar().resource_options.client_version_suffix)
                            .setClientSilenceVersionSuffix(Config.getRegionVar().resource_options.client_silence_version_suffix)
                            .setAreaType(Config.getRegionVar().resource_options.area_type)
                            .setPayCallbackUrl(Config.getRegionVar().resource_options.pay_callback_url)
                            .setCdkeyUrl(Config.getRegionVar().resource_options.cdkey_url)
                            .setClientDataMd5(Config.getRegionVar().resource_options.client_data_md5)
                            .setClientSilenceDataMd5(Config.getRegionVar().resource_options.client_silence_data_md5)
                            .setFeedbackUrl(Config.getRegionVar().resource_options.feedback_url)
                            .setPrivacyPolicyUrl(Config.getRegionVar().resource_options.privacy_policy_url)
                            .setAccountBindUrl(Config.getRegionVar().resource_options.account_bind_url)
                            .setHandbookUrl(Config.getRegionVar().resource_options.handbook_url)
                            .setOfficialCommunityUrl(Config.getRegionVar().resource_options.official_community_url)
                            .setBulletinUrl(Config.getRegionVar().resource_options.bulletin_url)
                            .setUserCenterUrl(Config.getRegionVar().resource_options.user_center_url)
                            .setResVersionConfig(
                                    ResVersionConfig.newBuilder()
                                            .setRelogin(true)
                                            .setMd5(Config.getRegionVar().resource_options.res_version_config_md5)
                                            .setVersion(Config.getRegionVar().resource_options.res_version_config_version)
                                            .setReleaseTotalSize(Config.getRegionVar().resource_options.res_version_config_release_total_size)
                                            .setVersionSuffix(Config.getRegionVar().resource_options.res_version_config_version_suffix)
                                            .setBranch(Config.getRegionVar().resource_options.res_version_config_branch)
                                            .buildPartial())
                            .build();

            regions.add(RegionSimpleInfo.newBuilder().setName(region.Name).setTitle(region.Title).setType(region.Type).setDispatchUrl(Config.getRegionVar().dispatchUrl + "/query_cur_region/" + region.Name).build());

            try {
                var updatedQuery =
                        QueryCurrRegionHttpRsp.newBuilder()
                                .setRetcode(Retcode.RETCODE_SUCC.getValue())
                                .setRegionInfo(regionInfo)
                                .setClientSecretKey(ByteString.copyFrom(RSA.getDispatchSeed()))
                                .setRegionCustomConfigEncrypted(this.getCurrentRegionCustomConfig(region.options))
                                .setConnectGateTicket(region.options.gate_ticket)
                                .build();

                this.serverRegions.put(region.Name, new RegionMap(region, BASE64.encode(updatedQuery.toByteString().toByteArray())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            var updatedRegionList =
                    QueryRegionListHttpRsp.newBuilder()
                            .setRetcode(Retcode.RETCODE_SUCC.getValue())
                            .addAllRegionList(regions)
                            .setClientSecretKey(ByteString.copyFrom(RSA.getDispatchSeed()))
                            .setClientCustomConfigEncrypted(this.getAllRegionsCustomConfig())
                            .setEnableLoginPc(true)
                            .build();
            this.queryAllRegionResponse = BASE64.encode(updatedRegionList.toByteString().toByteArray());
        }catch (JsonProcessingException e) {
            this.queryAllRegionResponse = "";
        }
    }

    /**
     *  Source: <a href="https://dispatchosglobal.yuanshen.com/query_region_list">https://dispatchosglobal.yuanshen.com/query_region_list</a><br><br>
     *  Methods: GET<br><br>
     *  Parameters:<br>
     *      - version: Game version<br>
     *      - lang: Language id<br>
     *      - platform: platform<br>
     *      - binary: send as binary?<br>
     *      - time: time<br>
     *      - channel_id: channel id<br>
     *      - sub_channel_id: sub channel id<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "query_region_list")
    public String SendQueryRegionList(String version, Integer lang, Integer platform, Integer binary, Integer time, Integer channel_id, Integer sub_channel_id) {
        if(version == null || !Utils.checkGameVersion(version) || channel_id == null || channel_id != 1) {
            this.queryAllRegionResponse = "CP///////////wE=";
        }

        return this.queryAllRegionResponse;
    }

    /**
     *  Source: <a href="https://dispatchosglobal.yuanshen.com/query_cur_region">https://dispatchosglobal.yuanshen.com/query_cur_region</a><br><br>
     *  Methods: GET<br><br>
     *  Parameters:<br>
     *      - version: Game version<br>
     *      - lang: Language id<br>
     *      - platform: platform<br>
     *      - binary: send as binary?<br>
     *      - time: time<br>
     *      - channel_id: channel id<br>
     *      - sub_channel_id: sub channel id<br>
     *      - account_type: account type<br>
     *      - dispatchSeed: dispatch seed<br>
     *      - aid: account id
     *      - key_id: platform key id.
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "query_cur_region/{regionName}", produces = "application/json")
    public String SendQueryCurRegion(String version, Integer lang, Integer platform, Integer binary, Integer time, Integer channel_id, Integer sub_channel_id, Integer account_type, String dispatchSeed, Integer key_id, Integer aid, @PathVariable String regionName) throws Exception {
        if(version == null || !version.contains("REL") || channel_id == null || channel_id != 1) {
            return "CAESGE5vdCBGb3VuZCB2ZXJzaW9uIGNvbmZpZw==";
        }

        var region = this.serverRegions.get(regionName);
        if(this.serverRegions.isEmpty() || region == null) {
            QueryCurrRegionHttpRsp rsp =
                    QueryCurrRegionHttpRsp.newBuilder()
                            .setRetcode(7)
                            .setMsg("No config")
                            .setRegionInfo(RegionInfo.newBuilder().build())
                            .build();

            return RSA.encryptAndSignRegionData(rsp.toByteArray(), key_id);
        }

        if(region.regionClass.maintenance != null) {
            QueryCurrRegionHttpRsp rsp =
                    QueryCurrRegionHttpRsp.newBuilder()
                            .setRetcode(11)
                            .setMsg("Under Maintenance")
                            .setRegionInfo(RegionInfo.newBuilder().build())
                            .setStopServer(
                                    StopServerInfo.newBuilder()
                                            .setUrl(region.regionClass.maintenance.url)
                                            .setStopBeginTime(region.regionClass.maintenance.startDate)
                                            .setStopEndTime(region.regionClass.maintenance.endDate)
                                            .setContentMsg(region.regionClass.maintenance.msg)
                                            .build())
                            .buildPartial();
            return RSA.encryptAndSignRegionData(rsp.toByteArray(), key_id);
        }

        String clientVersion = version.replaceAll(Pattern.compile("[a-zA-Z]").pattern(), "");
        if(!clientVersion.equals(region.regionClass.options.game_Version)) {
            QueryCurrRegionHttpRsp rsp =
                    QueryCurrRegionHttpRsp.newBuilder()
                            .setRetcode(20)
                            .setMsg(String.format("Version update found. Please start the launcher to download the latest version.\n\nServer Version: %s\nClient Version: %s", region.regionClass.options.game_Version, clientVersion))
                            .setRegionInfo(RegionInfo.newBuilder().build())
                            .setForceUpdate(ForceUpdateInfoOuterClass.ForceUpdateInfo.newBuilder().setForceUpdateUrl("hoyoverse.com").build())
                            .buildPartial();
            return RSA.encryptAndSignRegionData(rsp.toByteArray(), key_id);
        }

        return RSA.encryptAndSignRegionData(BASE64.decode(region.base64), key_id);
    }

    /**
     *  Source: <a href="https://dispatchosglobal.yuanshen.com/query_security_file">https://dispatchosglobal.yuanshen.com/query_security_file</a><br><br>
     *  Methods: GET<br><br>
     *  Parameters:<br>
     *      - file_key: Game version<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "query_security_file")
    public String SendQuerySecurityFile(String file_key) {
        return "";
    }

    /**
     *  Source: <a href="https://hk4e-beta-sdk-os.hoyoverse.com/dispatch/dispatch/getGateAddress">https://hk4e-beta-sdk-os.hoyoverse.com/dispatch/dispatch/getGateAddress</a><br><br>
     *  Methods: GET<br><br>
     *  Parameters:<br>
     *      - region: Region name<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "dispatch/dispatch/getGateAddress", produces = "application/json")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGateAddress(String region) {
        var reg = this.serverRegions.get(region);
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(reg == null) {
            data.put("data", null);
            data.put("retcode", Retcode.RETCODE_FAIL);
            data.put("message", "get address from config failed, err: get address from config failed no support region");
            return ResponseEntity.ok(data);
        }
        data.put("message", "OK");
        data.put("retcode", Retcode.RETCODE_FAIL);
        data.put("data", new LinkedHashMap<>() {{
            put("address_list", new ArrayList<>(List.of(
                    new LinkedHashMap<>() {{
                        put("ip", reg.regionClass.Ip);
                        put("port", reg.regionClass.Port);
                    }}
            )));
        }});
        return ResponseEntity.ok(data);
    }

    /**
     * Config for all regions.
     */
    private ByteString getAllRegionsCustomConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode customConfig = objectMapper.createObjectNode();

        customConfig.put("sdkenv", Config.getRegionVar().sdkenv);
        customConfig.put("loadPatch", objectMapper.writeValueAsString(Config.getRegionVar().loadPatch));
        customConfig.put("checkdevice", objectMapper.writeValueAsString(Config.getRegionVar().checkdevice));
        customConfig.put("devicelist", Config.getRegionVar().devicelist);
        customConfig.put("showexception", objectMapper.writeValueAsString(Config.getRegionVar().showexception));
        customConfig.put("regionConfig", Config.getRegionVar().regionConfig);
        customConfig.put("downloadMode", objectMapper.writeValueAsString(Config.getRegionVar().downloadMode));
        customConfig.put("downloadThreadNum", objectMapper.writeValueAsString(Config.getRegionVar().downloadThreadNum));
        customConfig.put("downloadIgnore403", objectMapper.writeValueAsString(Config.getRegionVar().downloadIgnore403));
        customConfig.put("downloadNoAudioDiff", objectMapper.writeValueAsString(Config.getRegionVar().downloadNoAudioDiff));
        customConfig.put("downloadExcludeUselessRes", objectMapper.writeValueAsString(Config.getRegionVar().downloadExcludeUselessRes));
        customConfig.put("downloadEnableXXhash", objectMapper.writeValueAsString(Config.getRegionVar().downloadEnableXXhash));
        customConfig.put("downloadEnableUltraVerify", objectMapper.writeValueAsString(Config.getRegionVar().downloadEnableUltraVerify));
        customConfig.put("downloadVerifyRetryNum", objectMapper.writeValueAsString(Config.getRegionVar().downloadVerifyRetryNum));
        customConfig.put("downloadOptionalCategoryAlwaysExist", objectMapper.writeValueAsString(Config.getRegionVar().downloadOptionalCategoryAlwaysExist));
        customConfig.put("downloadEnablePatchedAsb", objectMapper.writeValueAsString(Config.getRegionVar().downloadEnablePatchedAsb));
        customConfig.put("downloadPatchAsbAutoFallback", objectMapper.writeValueAsString(Config.getRegionVar().downloadPatchAsbAutoFallback));
        customConfig.put("regionDispatchType", objectMapper.writeValueAsString(Config.getRegionVar().regionDispatchType));
        customConfig.put("videoKey", Config.getRegionVar().videoKey);
        customConfig.put("enableDownload", objectMapper.writeValueAsString(Config.getRegionVar().enableDownload));
        customConfig.put("downloadEnableParallelVerify", "true");
        customConfig.put("injectfixVersion", objectMapper.writeValueAsString(Config.getRegionVar().injectfixVersion));

        byte[] encryptedConfig = objectMapper.writeValueAsString(customConfig).getBytes();
        XOR.performXor(encryptedConfig, RSA.getDispatchKey());

        return ByteString.copyFrom(encryptedConfig);
    }

    /**
     * Config for specified region.
     */
    private ByteString getCurrentRegionCustomConfig(RegionVar.RegionOptions options) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode customConfig = objectMapper.createObjectNode();

        customConfig.put("useLoadingBlockChecker", objectMapper.writeValueAsString(options.useLoadingBlockChecker));
        customConfig.put("AliWaterMaskAfa", objectMapper.writeValueAsString(options.aliWaterMaskAfa));
        customConfig.put("AliWaterMaskReqUrl", objectMapper.writeValueAsString(options.aliWaterMaskReqUrl));
        customConfig.put("AliWaterMaskTimeOut", objectMapper.writeValueAsString(options.aliWaterMaskTimeOut));
        customConfig.put("SDKServerLog", objectMapper.writeValueAsString(options.sdkserverlog));
        customConfig.put("checkEntityPreloadOverTime", objectMapper.writeValueAsString(options.checkEntityPreloadOverTime));
        customConfig.put("codeSwitch", objectMapper.writeValueAsString(options.codeSwitch));
        customConfig.put("coverSwitch", objectMapper.writeValueAsString(options.coverSwitch));
        customConfig.put("dumpType", objectMapper.writeValueAsString(options.dumpType));
        customConfig.put("enableMobileMaskResize", objectMapper.writeValueAsString(options.enableMobileMaskResize));
        customConfig.put("il2cppCodeSwitch", objectMapper.writeValueAsString(options.il2cppCodeSwitch));
        customConfig.put("engineCodeSwitch", objectMapper.writeValueAsString(options.engineCodeSwitch));
        customConfig.put("greyTest", objectMapper.writeValueAsString(options.greyTest));
        customConfig.put("mtrConfig", objectMapper.writeValueAsString(options.mtrConfig));
        customConfig.put("perf_report_config_url", objectMapper.writeValueAsString(options.perf_report_config_url));
        customConfig.put("perf_report_record_url", objectMapper.writeValueAsString(options.perf_report_record_url));
        customConfig.put("perf_report_enable", objectMapper.writeValueAsString(options.perf_report_enable));
        customConfig.put("perf_report_percent", objectMapper.writeValueAsString(options.perf_report_percent));
        customConfig.put("perf_report_servertype", objectMapper.writeValueAsString(options.perf_report_servertype));
        customConfig.put("post_client_data_url", objectMapper.writeValueAsString(options.post_client_data_url));
        customConfig.put("disableGetGpuMemorySize", objectMapper.writeValueAsString(options.disableGetGpuMemorySize));
        customConfig.put("enableRazerChromaInit", objectMapper.writeValueAsString(options.enableRazerChromaInit));
        customConfig.put("cah", objectMapper.writeValueAsString(options.cah));
        customConfig.put("reportNetDelayConfig", objectMapper.writeValueAsString(options.reportNetDelayConfig));
        customConfig.put("urlCheckConfig", objectMapper.writeValueAsString(options.urlCheckConfig));
        customConfig.put("perf_report_account_blacklist", objectMapper.writeValueAsString(options.perf_report_account_blacklist));
        customConfig.put("perf_report_account_whitelist", objectMapper.writeValueAsString(options.perf_report_account_whitelist));
        customConfig.put("perf_report_platform_blacklist", objectMapper.writeValueAsString(options.perf_report_platform_blacklist));
        customConfig.put("perf_report_platform_whitelist", objectMapper.writeValueAsString(options.perf_report_platform_whitelist));
        customConfig.put("homeDotPattern", objectMapper.writeValueAsString(options.homeDotPattern));
        customConfig.put("homeItemFilter", objectMapper.writeValueAsString(options.homeItemFilter));
        customConfig.put("gachaSharePlatform", objectMapper.writeValueAsString(options.gachaSharePlatform));
        customConfig.put("photographSharePlatform", objectMapper.writeValueAsString(options.photographSharePlatform));
        customConfig.put("googlePromotionInterval", objectMapper.writeValueAsString(options.googlePromotionInterval));
        customConfig.put("photographShareTopics", objectMapper.writeValueAsString(options.photographShareTopics));
        customConfig.put("gachaShareTopics", objectMapper.writeValueAsString(options.gachaShareTopics));
        customConfig.put("douyinShareTopics", options.douyinShareTopics);
        customConfig.put("bilibiliShareTopics", objectMapper.writeValueAsString(options.bilibiliShareTopics));
        customConfig.put("platformCoverSwitch", objectMapper.writeValueAsString(options.platformCoverSwitch));
        customConfig.put("cloud_h5_url", options.cloud_h5_url);
        customConfig.put("banServerRedPoints", objectMapper.writeValueAsString(options.banServerRedPoints));
        customConfig.put("channelCoverSwitch", objectMapper.writeValueAsString(options.channelCoverSwitch));
        customConfig.put("disableRewiredInitWatchDog", objectMapper.writeValueAsString(options.disableRewiredInitWatchDog));
        customConfig.put("disableRewiredDelayInit", objectMapper.writeValueAsString(options.disableRewiredDelayInit));
        customConfig.put("enableMallPay", objectMapper.writeValueAsString(options.enableMallPay));
        customConfig.put("enableMall", objectMapper.writeValueAsString(options.enableMall));
        customConfig.put("audioGlitchFix", objectMapper.writeValueAsString(options.audioGlitchFix));

        byte[] encryptedConfig = objectMapper.writeValueAsString(customConfig).getBytes();
        XOR.performXor(encryptedConfig, RSA.getDispatchKey());

        return ByteString.copyFrom(encryptedConfig);
    }

    @Getter
    private static class RegionMap {
        private RegionVar.RegionClass regionClass;
        private String base64;

        public RegionMap(RegionVar.RegionClass regionClass, String base64) {
            this.regionClass = regionClass;
            this.base64 = base64;
        }
    }
}