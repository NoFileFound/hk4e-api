package org.httpsrv.controllers.combo;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.httpsrv.conf.Config;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.CompareProtocolVersionBody;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/combo/granter/api", "hk4e_cn/combo/granter/api", "combo/granter/api"}, produces = "application/json")
public class Granter implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/api/getDynamicClientConfig">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/api/getDynamicClientConfig</a><br><br>
     *  Method: GET<br><br>
     *  Parameters: (Dynamically)<br>
     *      - check_consent_banner: check_consent_banner<br>
     */
    @GetMapping(value = "getDynamicClientConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDynamicClientConfig(Boolean check_consent_banner, HttpServletRequest request) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(check_consent_banner != null && check_consent_banner) {
            data.put("enable_consent_banner", Config.getHttpConfig().enableConsentBanner);
        }
        data.put("region_code", GeoIP.getCountryCode(request.getRemoteAddr()));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/compareProtocolVersion">https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/compareProtocolVersion</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - channel_id: Channel id<br>
     *      - language: Language<br>
     *      - major:  Major<br>
     *      - minimum:  Minimum<br>
     */
    @PostMapping(value = {"compareProtocolVersion", "getProtocol"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendCompareProtocolVersion(@RequestBody CompareProtocolVersionBody bodyData) {
        if(bodyData == null || bodyData.getLanguage() == null || !Utils.checkGameLanguage(bodyData.getLanguage())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PROTOCOL_FAILED, "Protocol loading error", null));
        }

        if(bodyData.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                bodyData.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                bodyData.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PROTOCOL_FAILED, "Protocol loading error", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        boolean isModified = (bodyData.getChannel_id() != 1);

        data.put("modified", isModified);
        if (isModified) {
            data.put("protocol", new LinkedHashMap<>() {{
                put("id", 0);
                put("app_id", bodyData.getApp_id());
                put("language", bodyData.getLanguage());
                put("user_proto", "");
                put("priv_proto", "");
                put("major", bodyData.getMajor());
                put("minimum", bodyData.getMinimum());
                put("create_time", "0");
                put("teenager_proto", "");
                put("third_proto", "");
                put("full_priv_proto", "");
            }});
        } else {
            data.put("protocol", null);
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-s.mihoyo.com/hk4e_cn/combo/granter/api/compareProtocolVersion">https://hk4e-sdk-s.mihoyo.com/hk4e_cn/combo/granter/api/compareProtocolVersion</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - channel_id: Channel id<br>
     *      - language: Language<br>
     *      - major:  Major<br>
     *      - minimum:  Minimum<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = {"compareProtocolVersion", "getProtocol"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendCompareProtocolVersion(Integer app_id, String language, Integer major, Integer minimum, Integer channel_id) {
        if(app_id == null || language == null || !Utils.checkGameLanguage(language) || major == null || minimum == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PROTOCOL_FAILED, "Protocol loading error", null));
        }

        if(app_id != ApplicationId.GENSHIN_RELEASE.getValue() &&
                app_id != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                app_id != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PROTOCOL_FAILED, "Protocol loading error", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("modified", true);
        data.put("protocol", new LinkedHashMap<>() {{
            put("id", 5);
            put("app_id", app_id);
            put("language", language);
            put("user_proto", "");
            put("priv_proto", "");
            put("major", major);
            put("minimum", minimum);
            put("create_time", "0");
            put("teenager_proto", "");
            put("third_proto", "");
            put("full_priv_proto", "");
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/getConfig">https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/getConfig</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - channel_id: Channel id<br>
     *      - client_type: Platform<br>
     */
    @RequestMapping(value = "getConfig", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(Integer app_id, Integer channel_id, Integer client_type) {
        if(app_id == null || channel_id == null || client_type == null || client_type < 1 || client_type > 13) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        if(app_id != ApplicationId.GENSHIN_RELEASE.getValue() &&
                app_id != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                app_id != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("protocol", channel_id != 1);
        data.put("qr_enabled", Config.getHttpConfig().useQRLogin);
        data.put("log_level", Config.getHttpConfig().apiLogLevel);
        data.put("announce_url", Config.getHttpConfig().announcementUrl);
        data.put("push_alias_type", Config.getHttpConfig().aliasPushType);
        data.put("disable_ysdk_guard", Config.getHttpConfig().disableYSDKGuard);
        data.put("enable_announce_pic_popup", Config.getHttpConfig().enableAnnouncementPopUp);
        data.put("app_name", "原神"); // 原神海外
        if(client_type == 3 || client_type == 9) {
            // Only for PC/Cloud PC
            data.put("qr_enabled_apps", Config.getHttpConfig().qrApps);
            data.put("qr_app_icons", Config.getHttpConfig().qrAppIcons);
        } else {
            data.put("qr_enabled_apps", null);
            data.put("qr_app_icons", null);
        }

        data.put("qr_cloud_display_name", "云·原神");
        data.put("enable_user_center", Config.getHttpConfig().useAccountCenter);

        LinkedHashMap<String, Boolean> switchConfigs = new LinkedHashMap<>();
        switch(client_type)
        {
            case 1:
                // IOS
                switchConfigs.put("jpush", Config.getHttpConfig().jPushConfig);
                switchConfigs.put("initialize_appsflyer", Config.getHttpConfig().initializeAppsFlyerConfig);
                break;
            case 2:
                // Android
                switchConfigs.put("jpush", Config.getHttpConfig().jPushConfig);
                switchConfigs.put("allow_notification", Config.getHttpConfig().allowNotificationConfig);
                switchConfigs.put("initialize_appsflyer", Config.getHttpConfig().initializeAppsFlyerConfig);
                break;
            case 8:
                // Cloud Android
                switchConfigs.put("initialize_appsflyer", Config.getHttpConfig().initializeAppsFlyerConfig);
                break;
        }
        data.put("functional_switch_configs", switchConfigs);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/getFont">https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/getFont</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     */
    @RequestMapping(value = "getFont", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LinkedHashMap<String, Object>> SendFont(Integer app_id) {
        if(app_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "Invalid AppID", null));
        }

        var data = new LinkedHashMap<>();
        var fonts = new ArrayList<>();

        // Chinese
        fonts.add(Map.of(
                "font_id", 0,
                "app_id", 0,
                "name", "zh-cn.tff",
                "url", "https://sdk.hoyoverse.com/sdk-public/2024/12/31/ee21fab3128b390122431dfa967709a5_1886429408221468499.ttf",
                "md5", "ee21fab3128b390122431dfa967709a5"
        ));

        // Japanese
        fonts.add(Map.of(
                "font_id", 0,
                "app_id", 0,
                "name", "ja.tff",
                "url", "https://sdk.hoyoverse.com/sdk-public/2024/12/31/1eb7f8fd3007c8d88272908f7f239ef8_3447476462374302156.ttf",
                "md5", "1eb7f8fd3007c8d88272908f7f239ef8"
        ));

        data.put("fonts", fonts);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}