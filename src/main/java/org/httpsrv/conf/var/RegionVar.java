package org.httpsrv.conf.var;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionVar {
    public List<RegionClass> regions = new ArrayList<>(List.of(
            new RegionClass("os_jp69", "Localhost", "DEV_PUBLIC", "127.0.0.1", 8882)
    ));

    public boolean enableDownload = false;
    public String dispatchUrl = "http://127.0.0.1:8881";
    public String videoKey = "5578228838233776";
    public int regionDispatchType = 0;
    public int downloadPatchAsbAutoFallback = 0;
    public boolean downloadEnablePatchedAsb = true;
    public boolean downloadOptionalCategoryAlwaysExist = true;
    public int downloadVerifyRetryNum = 3;
    public boolean downloadEnableUltraVerify = true;
    public boolean downloadEnableXXhash = true;
    public boolean downloadExcludeUselessRes = false;
    public boolean downloadNoAudioDiff = false;
    public boolean downloadIgnore403 = false;
    public int downloadThreadNum = 20;
    public int downloadMode = 0;
    public String regionConfig = "pm|fk|add";
    public boolean showexception = true;
    public int sdkenv = 8;
    public boolean loadPatch = true;
    public boolean checkdevice = false;
    public String devicelist = "";
    public int injectfixVersion = 0;
    public RegionUpdateConfig resource_options = new RegionUpdateConfig();

    public static class RegionClass {
        public String Name;
        public String Title;
        public String Type;
        public String Ip;
        public int Port;
        public Maintenance maintenance;
        public RegionOptions options = new RegionOptions();

        public RegionClass() {};

        public RegionClass(String name, String title, String type, String address, int port) {
            this.Name = name;
            this.Title = title;
            this.Type = type;
            this.Ip = address;
            this.Port  = port;
            this.maintenance = null;
        }

        public RegionClass(String name, String title, String type, String address, int port, Maintenance maintenance) {
            this.Name = name;
            this.Title = title;
            this.Type = type;
            this.Ip = address;
            this.Port = port;
            this.maintenance = maintenance;
        }
    }

    public static class Maintenance {
        public int startDate = 0;
        public int endDate = 0;
        public String url = "https://www.hoyolab.com/";
        public String msg = "Head on over to HoYoLAB to discuss new content updates and check out the latest game news!";

        public Maintenance() {};

        public Maintenance(int startDate, int endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Maintenance(int startDate, int endDate, String url, String msg) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.url = url;
            this.msg = msg;
        }
    }

    public static class RegionUpdateConfig {
        public String bulletin_url = "";
        public String user_center_url = "";
        public String pay_callback_url = "";
        public String resource_url = "";
        public String next_resource_url = "";
        public String data_url = "";
        public String feedback_url = "";
        public String resource_url_bak = "";
        public Integer client_data_version = 0;
        public String handbook_url = "";
        public Integer client_silence_data_version = 0;
        public String client_data_md5 = "";
        public String client_silence_data_md5 = "";
        public String official_community_url = "";
        public String client_version_suffix = "";
        public String client_silence_version_suffix = "";
        public String account_bind_url = "";
        public String cdkey_url = "";
        public String privacy_policy_url = "";
        public String game_biz = "";
        public String area_type = "";
        public Integer res_version_config_version = 0;
        public String res_version_config_md5 = "";
        public String res_version_config_release_total_size = "";
        public String res_version_config_version_suffix = "";
        public String res_version_config_branch = "";

        public RegionUpdateConfig() {}
    }

    public static class RegionOptions {
        public static class GreyTest {
            public List<Integer> codeSwitchs = new ArrayList<>();
            public List<Integer> il2cppCodeSwitchs = new ArrayList<>();
            public List<Integer> engineCodeSwitchs = new ArrayList<>();
            public List<Integer> Platforms = new ArrayList<>();
            public int rate = 0;
        }

        public static class MtrConfig {
            public boolean isOpen = true;
            public int maxTTL = 32;
            public int timeOut = 5000;
            public int traceCount = 5;
            public int abortTimeOutCount = 3;
            public int autoTraceInterval = 3600;
            public int traceCDEachReason = 600;
            public int checkCDEachReason = 0;
            public int timeInterval = 1000;
            public boolean useOldWinVersion = false;
            public List<String> banReasons = new ArrayList<>();
        }

        public static class UrlCheckConfig {
            public boolean isOpen = true;
            public int timeOut = 5000;
            public int traceCount = 5;
            public int errorTraceCount = 0;
            public int successTraceCount = 0;
            public int abortTimeOutCount = 3600;
            public int timeInterval = 1000;
            public int checkCDEachReason = 0;
            public List<String> banReasons = new ArrayList<>();
        }

        public static class ReportNetDelayConfig {
            public boolean openGateserver = true;
            public boolean enableShowIPRegion = true;
            public boolean universalLinkEnable = true;
            public boolean cdnNameEnable = true;
        }

        public String perf_report_config_url = "https://ys-log-upload-os.hoyoverse.com/perf/config/verify";
        public String perf_report_record_url = "https://ys-log-upload-os.hoyoverse.com/perf/dataUpload";
        public List<String> perf_report_account_blacklist = new ArrayList<>();
        public List<String> perf_report_account_whitelist = new ArrayList<>();
        public List<String> perf_report_platform_blacklist = new ArrayList<>();
        public List<String> perf_report_platform_whitelist = new ArrayList<>();
        public int perf_report_percent = 100;
        public boolean perf_report_enable = true;
        public int perf_report_servertype = 0;
        public String post_client_data_url = "https://api-takumi.mihoyo.com/common/csc/client/addCliTempInfo?sign_type=2\\u0026auth_appid=csc\\u0026authkey_ver=1";
        public String gate_ticket = "test";
        public String game_Version = "5.0.0";
        public boolean useLoadingBlockChecker = true;
        public boolean disableGetGpuMemorySize = false;
        public int aliWaterMaskAfa = 0;
        public String aliWaterMaskReqUrl = "";
        public int aliWaterMaskTimeOut = 0;
        public boolean sdkserverlog = true;
        public boolean checkEntityPreloadOverTime = false;
        public boolean enableRazerChromaInit = true;
        public List<Integer> codeSwitch = new ArrayList<>();
        public List<Integer> coverSwitch = new ArrayList<>();
        public List<Integer> engineCodeSwitch = new ArrayList<>();
        public List<Integer> il2cppCodeSwitch = new ArrayList<>();
        public int dumpType = 0;
        public boolean enableMobileMaskResize = false;
        public List<GreyTest> greyTest = new ArrayList<>();
        public MtrConfig mtrConfig = new MtrConfig();
        public UrlCheckConfig urlCheckConfig = new UrlCheckConfig();
        public boolean cah = false;
        public ReportNetDelayConfig reportNetDelayConfig = new ReportNetDelayConfig();
        public boolean homeDotPattern = true;
        public int homeItemFilter = 0;
        public int gachaSharePlatform = 0;
        public int photographSharePlatform = 0;
        public int googlePromotionInterval = 0;
        public int photographShareTopics = 0;
        public String douyinShareTopics = "原神纳塔";
        public int bilibiliShareTopics = 0;
        public int gachaShareTopics = 0;
        public List<Map<String, List<Integer>>> channelCoverSwitch = new ArrayList<>();
        public List<Map<String, List<Integer>>> platformCoverSwitch = new ArrayList<>();
        public String cloud_h5_url = "https://webstatic.mihoyo.com/ys/event/e20210830cloud/index.html?mode=fullscreen\\u0026game_biz=hk4e_cn";
        public List<String> banServerRedPoints = new ArrayList<>();
        public boolean disableRewiredDelayInit = false;
        public boolean disableRewiredInitWatchDog = false;
        public boolean enableMallPay = true;
        public boolean enableMall = true;
        public int audioGlitchFix = 0;
    }
}