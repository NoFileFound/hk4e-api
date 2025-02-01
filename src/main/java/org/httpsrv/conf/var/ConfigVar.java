package org.httpsrv.conf.var;

import java.util.*;

public class ConfigVar {
    public int aliasPushType = 2;
    public boolean allowNotificationConfig = true;
    public String announcementUrl = "https://sdk.mihoyo.com/hk4e/announcement/index.html?sdk_presentation_style=fullscreen&sdk_screen_transparent=true&auth_appid=announcement&authkey_ver=1&game_biz=hk4e_cn&sign_type=2&version=2.33&game=hk4e#/";
    public String apiLogLevel = "DEBUG";
    public boolean disableEmailBindSkip = false;
    public boolean disableMMT = false;
    public boolean disableRegistrations = false;
    public boolean disableTryVerify = false;
    public boolean disableYSDKGuard = false;
    public boolean enableAccountListPage = true;
    public boolean enableAdultLogoAndroid = false;
    public int adultLogoAndroidHeight = 0;
    public int adultLogoAndroidWidth = 0;
    public boolean enableAliPayRecommend = true;
    public boolean enableAnnouncementPopUp = true;
    public boolean enableApmSdk = true;
    public int enableAppsFlyer = 1;
    public boolean enableAttribution = true;
    public boolean enableAttributionReport = true;
    public int attributionReportInterval = 3;
    public List<String> attributionReportDetails = new ArrayList<>(List.of(
            "manufacturer"
    ));
    public boolean enableBBSAuthLogin = false;
    public boolean enableEmailBindRemind = true;
    public int emailBindRemindInterval = 7;
    public boolean enableBindGoogleSdkOrder = true;
    public boolean enableConsentBanner = true;
    public boolean enableCrashCapture = false;
    public boolean enableCXBindAccount = false;
    public boolean enableFireBase = false;
    public boolean enableFireBaseBlacklistDevicesSwitch = false;
    public int fireBaseBlacklistVersion = 1;
    public boolean enableFlashLogin = false;
    public boolean enableGoogleCancelCallback = true;
    public boolean enableGoogleV2 = true;
    public boolean enableGuestLogin = true;
    public int enableH5Cashier = 1;
    public boolean enableH5Log = true;
    public int h5CashierTimeout = 3;
    public boolean enableHoyoLabAuthLogin = false;
    public boolean enableHoyoPlayAuthLogin = false;
    public boolean enableHttpDns = true;
    public int httpDnsCacheExpireTime = 60;
    public int httpKeepAliveTime = 60;
    public int enableKibana = 1;
    public List<String> kibanaModules = new ArrayList<>(List.of(
            "download"
    ));

    public boolean enableKibanaBox = true;
    public int kibanaBoxUploadEventPageSize = 50;
    public int kibanaBoxMinimumUploadInterval = 10;
    public List<String> kibanaBoxDisabledPaths = new ArrayList<>();
    public int kibanaBoxMinUploadEventTriggerSize = 10;
    public boolean enableListPriceTierV2 = true;
    public int enableLoginFlowNotification = 1;
    public boolean enableNewForgotPwdPage = true;
    public boolean enableNewKcp = false;
    public boolean enableNewRegisterPage = true;
    public boolean enableOADI = true;
    public boolean enableOADIMultiProcess = true;
    public boolean enableOADICallHms = true;
    public boolean enablePayPlatformBlockH5Cashier = true;
    public int payPlatformH5LoadingLimit = 3;
    public String payCoCenteredHost = "bill.payco.com";
    public boolean enablePSBindAccount = false;
    public boolean enableRegisterAutoLogin = true;
    public boolean enableRegisterHide = true;
    public boolean enableServerGuest = false;
    public boolean enableServiceWorker = true;
    public String serviceWorkerUrl = "https://webstatic-sea.hoyoverse.com/sw.html";
    public boolean enableTelemetryDataUpload = true;
    public boolean enableTelemetryH5Log = true;
    public boolean enableTwitterV2 = true;
    public boolean enableUserCenterV2 = true;
    public boolean enableWebDpi = true;
    public boolean watermarkEnableWebNotice = true;
    public boolean fetchCurrentInstanceId = true;
    public boolean initializeAppsFlyerConfig = false;
    public boolean jPushConfig = false;
    public boolean modifyRealNameOtherVerify = false;
    public boolean requireHeartBeat = false;
    public boolean requireRealNameVerify = false;
    public boolean requireGuardian = false;
    public boolean useAccountCenter = true;
    public boolean useEmailCaptcha = false;
    public boolean useLegacyWebViewRenderMethod = true;
    public boolean useQRLogin = true;
    public LinkedHashMap<String, Boolean> qrApps = new LinkedHashMap<>(Map.of(
            "bbs", true,
            "cloud", true
    ));

    public LinkedHashMap<String, String> qrAppIcons = new LinkedHashMap<>(Map.of(
            "app", "https://sdk-webstatic.mihoyo.com/sdk-public/2023/10/11/63b6857bddb8be0887185890596b983f_4890465413038841959.png",
            "bbs", "https://sdk-webstatic.mihoyo.com/sdk-public/2023/10/11/69172b1a1fd17290b3e0649632216372_106775796556262449.png",
            "cloud", "https://sdk-webstatic.mihoyo.com/sdk-public/2022/12/07/ec0f2514f044ac43754440241ab0b838_3962973103776517937.png"
    ));

    public LinkedList<String> thirdPartyApps = new LinkedList<>(List.of(
            // Apple
            "ap",
            // Google
            "gl",
            // Facebook
            "fb",
            // Twitter
            "tw",
            // Game center
            "gc",
            // TapTap
            "tp"
    ));

    public LinkedHashMap<String, String> thirdPartyIgnored = new LinkedHashMap<>();
    public LinkedHashMap<String, LinkedHashMap<String, Object>> thirdPartyConfigs = new LinkedHashMap<>() {{
        // Facebook
        put("fb", new LinkedHashMap<>() {{
            put("token_type", "TK_GAME_TOKEN");
            put("game_token_expires_in", 2592000);
        }});

        // Game center
        put("gc", new LinkedHashMap<>() {{
            put("token_type", "TK_GAME_TOKEN");
            put("game_token_expires_in", 604800);
        }});

        // Twitter
        put("tw", new LinkedHashMap<>() {{
            put("token_type", "TK_GAME_TOKEN");
            put("game_token_expires_in", 2592000);
        }});

        // Apple
        put("ap", new LinkedHashMap<>() {{
            put("token_type", "TK_GAME_TOKEN");
            put("game_token_expires_in", 604800);
        }});

        // Google
        put("gl", new LinkedHashMap<>() {{
            put("token_type", "TK_GAME_TOKEN");
            put("game_token_expires_in", 2592000);
        }});
    }};

    public List<Object> BBSAuthLoginIgnored = new ArrayList<>();
    public List<Object> HoyoLabAuthIgnore = new ArrayList<>();
    public int enableNetworkReport = 1;
    public List<Integer> networkStatusCodes = new ArrayList<>(List.of(
            200
    ));

    public List<String> networkUrlPaths = new ArrayList<>(List.of(
            "combo/postman/device/setAlias"
    ));

    public List<String> networkConfigs = new ArrayList<>(List.of(
            "report_set_info",
            "notice_close_notice",
            "apm_crash_add_custom_key_value",
            "hasScanFunc",
            "push_clear_local_notification",
            "push_add_local_notification",
            "launch_del_notification",
            "info_get_device_id",
            "getDeviceId",
            "info_get_channel_id",
            "info_get_sub_channel_id",
            "login_set_server_id",

            // pc
            "info_get_cps",
            "info_get_uapc"
    ));
}