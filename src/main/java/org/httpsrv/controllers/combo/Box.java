package org.httpsrv.controllers.combo;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.utils.Jackson;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/combo/box/api/config", "hk4e_cn/combo/box/api/config", "combo/box/api/config"}, produces = "application/json")
public class Box implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/box/api/config/plat-launcher/plat-launcher">https://sdk-os-static.hoyoverse.com/combo/box/api/config/plat-launcher/plat-launcher</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher: Launcher id<br>
     */
    @GetMapping(value = "plat-launcher/plat-launcher")
    public ResponseEntity<LinkedHashMap<String, Object>> SedPlatLauncher(String launcher) {
        if(launcher == null || !launcher.equals("plat-launcher")) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        LinkedHashMap<String, String> vals = new LinkedHashMap<>();
        vals.put("apm_switch", "true");
        vals.put("telemetry_switch", "true");
        vals.put("disk_model_switch", "false");
        vals.put("apm_log_switch", "true");
        vals.put("disable_ldiff", "true");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("vals", vals);
        }}));
    }

    /**
     *  Source: <a href="https://sdk-static.mihoyo.com/combo/box/api/config/porte-cn/porte">https://sdk-static.mihoyo.com/combo/box/api/config/porte-cn/porte</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - client_type: Platform<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "porte-cn/porte")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPorte(String app_id, Integer client_type) {
        if(app_id == null || !Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_PLATFORM_NO_CONFIG, "RetCode_NoConfig", null));
    }

    /**
     *  Source: <a href="https://sdk-static.mihoyo.com/combo/box/api/config/porte-fe-cn/config">https://sdk-static.mihoyo.com/combo/box/api/config/porte-fe-cn/config</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - config: Config id<br>
     */
    @GetMapping(value = "porte-fe-cn/config")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPorteFeConfig(String type) {
        if(type == null || !type.equals("common")) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        LinkedHashMap<String, String> vals = new LinkedHashMap<>();
        vals.put("forgetPassWordV2", "true");
        vals.put("hypLoginManagerUserCenterV2", "true");
        vals.put("enableAccountActivity", "true");
        vals.put("enableAccountDevice", "false");
        vals.put("enablePassportAccountDevice", "false");
        vals.put("h5LoggerPollingTime", "5000");
        vals.put("disableH5Logger", "false");
        vals.put("realPersonPageInfo", "");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("vals", vals);
        }}));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/drmSwitch">https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/drmSwitch</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - biz_game: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client_type: Platform<br>
     */
    @GetMapping(value = "sdk/drmSwitch")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDrmSwitch(String biz_key, Integer client_type) {
        if(biz_key == null || client_type == null || !Utils.checkBizName(biz_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        LinkedHashMap<String, String> vals = new LinkedHashMap<>();
        vals.put("httpdns_enable", String.valueOf(Config.getHttpConfig().enableHttpDns));
        vals.put("httpdns_cache_expire_time", String.valueOf(Config.getHttpConfig().httpDnsCacheExpireTime));
        vals.put("http_keep_alive_time", String.valueOf(Config.getHttpConfig().httpKeepAliveTime));

        if(client_type == 2 || client_type == 8) {
            vals.put("isGooglePayV2", "{\"whiteList\": [ { \"thousandRate\": 1000}]}\n");
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("vals", vals);
        }}));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/box/api/config/porte-os/kibana_box">https://sdk-os-static.hoyoverse.com/combo/box/api/config/porte-os/kibana_box</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - appId: Application id<br>
     *      - platform: Platform<br>
     */
    @GetMapping(value = "porte-os/kibana_box")
    public ResponseEntity<LinkedHashMap<String, Object>> SendKibanaBox(String appId, String platform) {
        if(appId == null || platform == null || !Config.getPropertiesVar().appId.contains(appId)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        if(!platform.equals("ios") && !platform.equals("android")) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_PLATFORM_NO_CONFIG, "RetCode_NoConfig", null));
        }

        LinkedHashMap<String, String> vals = new LinkedHashMap<>();
        vals.put("uploadEventPageSize", String.valueOf(Config.getHttpConfig().kibanaBoxUploadEventPageSize));
        vals.put("minimumUploadInterval", String.valueOf(Config.getHttpConfig().kibanaBoxMinimumUploadInterval));
        vals.put("disabledPaths", String.valueOf(Config.getHttpConfig().kibanaBoxDisabledPaths));
        vals.put("enabled", String.valueOf(Config.getHttpConfig().enableKibanaBox));
        vals.put("minUploadEventTriggerSize", String.valueOf(Config.getHttpConfig().kibanaBoxMinUploadEventTriggerSize));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("vals", vals);
        }}));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/combo">https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/combo</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - biz_game: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client_type: Platform<br>
     */
    @GetMapping(value = "sdk/combo")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCombo(String biz_key, Integer client_type) throws JsonProcessingException {
        if(biz_key == null || client_type == null || !Utils.checkBizName(biz_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        if(client_type > 13 || client_type < 1 || client_type == 7 || client_type == 12) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_PLATFORM_NO_CONFIG, "RetCode_NoConfig", null));
        }

        LinkedHashMap<String, String> vals = new LinkedHashMap<>();
        switch(client_type) {
            case 4:
            case 5:
                vals.put("modify_real_name_other_verify", String.valueOf(Config.getHttpConfig().modifyRealNameOtherVerify));
                vals.put("login_flow_notification", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableLoginFlowNotification);}}));
                break;
            case 11:
                vals.put("login_flow_notification", Jackson.toJsonString((new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableLoginFlowNotification);}})));
                break;
            case 13:
            case 10:
                vals.put("enable_telemetry_data_upload", String.valueOf(Config.getHttpConfig().enableTelemetryDataUpload));
                vals.put("enable_telemetry_h5log", String.valueOf(Config.getHttpConfig().enableTelemetryH5Log));
                vals.put("network_report_config", "{\n\t\"enable\": 1,\n\t\"status_codes\": [200]\n}");
                break;
            case 2:
            case 8:
                vals.put("alipay_recommend", String.valueOf(Config.getHttpConfig().enableAliPayRecommend));
                vals.put("watermark_enable_web_notice", String.valueOf(Config.getHttpConfig().watermarkEnableWebNotice));
                vals.put("enable_oaid", String.valueOf(Config.getHttpConfig().enableOADI));
                vals.put("oaid_multi_process", String.valueOf(Config.getHttpConfig().enableOADIMultiProcess));
                vals.put("oaid_call_hms", String.valueOf(Config.getHttpConfig().enableOADICallHms));
                vals.put("pay_platform_block_h5_cashier", String.valueOf(Config.getHttpConfig().enablePayPlatformBlockH5Cashier));
                vals.put("pay_platform_h5_loading_limit", String.valueOf(Config.getHttpConfig().payPlatformH5LoadingLimit));
                vals.put("isGooglePayV2", String.valueOf(Config.getHttpConfig().enableGoogleV2));
                vals.put("report_black_list", "{\"key\":[\"download_update_progress\"]}");
                vals.put("bili_pay_cancel_strings", "[\"用户取消交易\"]\n");
                vals.put("enable_google_cancel_callback", String.valueOf(Config.getHttpConfig().enableGoogleCancelCallback));
            case 3:
            case 9:
                vals.put("kcp_enable", String.valueOf(Config.getHttpConfig().enableNewKcp));
                vals.put("kibana_pc_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableKibana); put("level", StringUtils.capitalize(Config.getHttpConfig().apiLogLevel.toLowerCase())); put("modules", Config.getHttpConfig().kibanaModules);}}));
                vals.put("webview_rendermethod_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("useLegacy", Config.getHttpConfig().useLegacyWebViewRenderMethod);}}));
                vals.put("enable_web_dpi", String.valueOf(Config.getHttpConfig().enableWebDpi));
                vals.put("account_list_page_enable", String.valueOf(Config.getHttpConfig().enableAccountListPage));
                vals.put("new_forgotpwd_page_enable", String.valueOf(Config.getHttpConfig().enableNewForgotPwdPage));
                vals.put("webview_apm_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("crash_capture_enable", Config.getHttpConfig().enableCrashCapture);}}));
                vals.put("pay_payco_centered_host", Config.getHttpConfig().payCoCenteredHost);
                vals.put("payment_cn_config", Jackson.toJsonString((new LinkedHashMap<String, Object>() {{put("h5_cashier_enable", Config.getHttpConfig().enableH5Cashier); put("h5_cashier_timeout", Config.getHttpConfig().h5CashierTimeout);}})));
            default:
                vals.put("enable_telemetry_data_upload", String.valueOf(Config.getHttpConfig().enableTelemetryDataUpload));
                vals.put("telemetry_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("dataupload_enable", Config.getHttpConfig().enableTelemetryDataUpload);}}));
                vals.put("enable_apm_sdk", String.valueOf(Config.getHttpConfig().enableApmSdk));
                vals.put("enable_telemetry_h5log", String.valueOf(Config.getHttpConfig().enableTelemetryH5Log));
                vals.put("email_bind_remind", String.valueOf(Config.getHttpConfig().enableEmailBindRemind));
                vals.put("email_bind_remind_interval", String.valueOf(Config.getHttpConfig().emailBindRemindInterval));
                vals.put("enable_attribution", String.valueOf(Config.getHttpConfig().enableAttribution));
                vals.put("disable_email_bind_skip", String.valueOf(Config.getHttpConfig().disableEmailBindSkip));
                vals.put("new_register_page_enable", String.valueOf(Config.getHttpConfig().enableNewRegisterPage));
                vals.put("h5log_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableH5Log); put("level", StringUtils.capitalize(Config.getHttpConfig().apiLogLevel.toLowerCase()));}}));
                vals.put("appsflyer_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableAppsFlyer);}}));
                vals.put("enable_register_autologin", String.valueOf(Config.getHttpConfig().enableRegisterAutoLogin));
                vals.put("enable_user_center_v2", String.valueOf(Config.getHttpConfig().enableUserCenterV2));
                vals.put("enable_twitter_v2", String.valueOf(Config.getHttpConfig().enableTwitterV2));
                vals.put("modify_real_name_other_verify", String.valueOf(Config.getHttpConfig().modifyRealNameOtherVerify));
                vals.put("disable_try_verify", String.valueOf(Config.getHttpConfig().disableTryVerify));
                vals.put("login_flow_notification", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableLoginFlowNotification);}}));
                vals.put("network_report_config", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("enable", Config.getHttpConfig().enableNetworkReport); put("status_codes", Config.getHttpConfig().networkStatusCodes); put("url_paths", Config.getHttpConfig().networkUrlPaths);}}));
                vals.put("enable_bind_google_sdk_order", String.valueOf(Config.getHttpConfig().enableBindGoogleSdkOrder));
                vals.put("email_register_hide", String.valueOf(Config.getHttpConfig().enableRegisterHide));
                vals.put("httpdns_enable", String.valueOf(Config.getHttpConfig().enableHttpDns));
                vals.put("httpdns_cache_expire_time", String.valueOf(Config.getHttpConfig().httpDnsCacheExpireTime));
                vals.put("http_keep_alive_time", String.valueOf(Config.getHttpConfig().httpKeepAliveTime));
                vals.put("list_price_tierv2_enable", String.valueOf(Config.getHttpConfig().enableListPriceTierV2));
                vals.put("h5log_filter_config", Jackson.toJsonString(Map.of("function", Map.of("event_name", Config.getHttpConfig().networkConfigs))));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("vals", vals);
        }}));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/box/api/config/sw/precache">https://sdk-os-static.hoyoverse.com/combo/box/api/config/sw/precache</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     */
    @GetMapping(value = "sw/precache")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPrecache(String biz, Integer client) {
        if(biz == null || client == null || !Utils.checkBizName(biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_INVALID_KEY, "RetCode_InvalidKey", null));
        }

        if(client > 3 || client < 1) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_COMBO_PLATFORM_NO_CONFIG, "RetCode_NoConfig", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("data", new LinkedHashMap<>() {{
                put("enable", String.valueOf(Config.getHttpConfig().enableServiceWorker));
                put("url", Config.getHttpConfig().serviceWorkerUrl);
            }});
        }}));
    }
}