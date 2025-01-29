package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.LoginBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/account/ma-passport/api", "hk4e_cn/account/ma-passport/api", "account/ma-passport/api"}, produces = "application/json")
public class Passport implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/updateUserMarketingStatus">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/updateUserMarketingStatus</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - entity: Entity<br>
     *      - marketing_status: Marketing status<br>
     */
    @PostMapping("updateUserMarketingStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> SendUpdateMarketingStatus() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/logout">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/logout</a><br><br>
     *  Method: POST<br><br>
     */
    @PostMapping("logout")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogout() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/verifySToken">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/verifySToken</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - stoken: Stoken<br>
     */
    @PostMapping(value = "verifySToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifySToken(@CookieValue(value = "stoken") String stoken, HttpServletRequest request) {
        if(stoken == null || stoken.isEmpty()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_INVALID_PARAMETER, "Parameter error", null));
        }

        Account acc = Database.findAccountByStoken(stoken);
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("user_info", new LinkedHashMap<>() {{
            put("account_name", acc.getName());
            put("aid", acc.getId());
            put("area_code", Utils.maskString(acc.getMobileArea()));
            put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            put("email", Utils.maskString(acc.getEmail()));
            put("identity_code", Utils.maskString(acc.getIdentityCard()));
            put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
            put("links", new ArrayList<>());
            put("mid", "12ya9usebi_hy");
            put("mobile", Utils.maskString(acc.getMobile()));
            put("realname", Utils.maskString(acc.getRealname()));
            put("rebind_area_code", "");
            put("rebind_mobile", "");
            put("rebind_mobile_time", "0");
            put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
            put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
            put("password_time", System.currentTimeMillis() / 1000);
            put("unmasked_email", "");
            put("unmasked_email_type", 0);
        }});
        data.put("tokens", new ArrayList<>(List.of(
                new LinkedHashMap<>() {{
                    put("token_type", 1);
                    put("token", stoken);
                }}
        )));
        data.put("ext_user_info", new LinkedHashMap<>() {{
            put("guardian_email", "");
            put("birth", "0");
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByPassword">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByPassword</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - account: Username<br>
     *      - password: Password<br>
     */
    @PostMapping(value = {"appLoginByPassword", "loginByPassword"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendAppLoginByPassword(@RequestBody LoginBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getAccount() == null || body.getPassword() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_INVALID_PARAMETER, "Parameter error", null));
        }

        String account = body.getAccount();
        String password = body.getPassword();
        try {
            account = RSA.DecryptPassword(account);
            password = RSA.DecryptPassword(password);
        }catch (Exception ignored) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "Decryption failed", null));
        }

        Account acc = Database.findAccountByEmail(account);
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "Account is not found.", null));
        }

        if(!acc.checkAuthorizationByPassword(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "Account or password is mismatching.", null));
        }

        if(!acc.getDeviceIds().contains(device_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_ACCOUNT_NEW_DEVICE_DETECTED, "您正在新裝置上登入，為確保是您本人操作，請先驗證身分後再登入", null));
        }

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setCurrentDeviceId(device_id);
        acc.save();

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("login_ticket", "");
        data.put("need_realperson", false);
        data.put("ext_user_info", new LinkedHashMap<String, Object>() {{
            data.put("birth", 0);
            data.put("guardian_email", "");
        }});
        data.put("reactivate_info", new LinkedHashMap<String, Object>() {{
            put("required", acc.getRequireActivation());
            put("ticket", acc.getRequireActivation() ? Database.findTicket(acc.getMobile().isEmpty() ? acc.getEmail() : acc.getMobile(), "Reactivation", !acc.getMobile().isEmpty()).getId() : "");
        }});
        data.put("realname_info", new LinkedHashMap<String, Object>() {{
            put("required", acc.getRequireRealPerson());
            put("action_ticket", acc.getRequireActivation() ? Database.findTicket(acc.getMobile().isEmpty() ? acc.getEmail() : acc.getMobile(), "Realperson", !acc.getMobile().isEmpty()).getId() : "");
            put("action_type", "");
        }});
        data.put("token", new LinkedHashMap<String, Object>() {{
            put("token", token);
            put("token_version", 1);
        }});
        data.put("user_info", new LinkedHashMap<String, Object>() {{
            put("account_name", acc.getName());
            put("aid", acc.getId());
            put("area_code", Utils.maskString(acc.getMobileArea()));
            put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            put("email", Utils.maskString(acc.getEmail()));
            put("identity_code", Utils.maskString(acc.getIdentityCard()));
            put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
            put("links", new ArrayList<>());
            put("mid", "12ya9usebi_hy");
            put("mobile", Utils.maskString(acc.getMobile()));
            put("realname", Utils.maskString(acc.getRealname()));
            put("rebind_area_code", "");
            put("rebind_mobile", "");
            put("rebind_mobile_time", "0");
            put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
            put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
            put("password_time", System.currentTimeMillis() / 1000);
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getConfig">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getConfig</a><br><br>
     *  Method: POST<br><br>
     *  Parameters: None<br>
     */
    @PostMapping(value = "getConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(HttpServletRequest request) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("ip", new LinkedHashMap<String, Object>() {{
            put("country_code", GeoIP.getCountryCode(request.getRemoteAddr()));
            put("language", "en-us");
            put("area_code", GeoIP.getCountryMobileCode(request.getRemoteAddr()));
        }});
        data.put("area_wl", new ArrayList<>());
        data.put("realname_wl", new ArrayList<>());
        data.put("guardian_age_limit", "14");
        data.put("disable_mmt", "true");
        data.put("show_birthday", "true");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getSwitchStatus">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getSwitchStatus</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - appId: Application id<br>
     *      - platform: Platform<br>
     */
    @GetMapping(value = "getSwitchStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> SendSwitchStatus(String app_id, Integer platform) {
        if (app_id == null || platform == null || !Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_INVALID_PARAMETER, "Invalid parameter", null));
        }

        var switchMap = new LinkedHashMap<>();
        if(platform == 2) {
            switchMap.put("ui_v2", new LinkedHashMap<>() {{
                put("enabled", true);
                put("disabled_versions", new ArrayList<>());
            }});
        }

        switchMap.put("apple_login", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("password_reset_entry", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("vn_real_name_v2", new LinkedHashMap<>() {{
            put("enabled", false);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("common_question_entry", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("pwd_login_tab", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("vn_real_name", new LinkedHashMap<>() {{
            put("enabled", false);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("firebase_return_unmasked_email", new LinkedHashMap<>() {{
            put("enabled", false);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("google_login", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("bind_user_thirdparty_email", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("third_party_bind_email", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("bind_thirdparty", new LinkedHashMap<>() {{
            put("enabled", false);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("user_name_bind_email", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("account_register_tab", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("twitter_login", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("facebook_login", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        switchMap.put("marketing_authorization", new LinkedHashMap<>() {{
            put("enabled", true);
            put("disabled_versions", new ArrayList<>());
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("switch_status_map", switchMap);
        }}));
    }
}

/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByThirdParty
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/reactivateAccount
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByEmailCaptcha
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByAuthTicket
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/sendEmailNotificationByGameToken
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/bindThirdPartyBySToken
/// TODO Implement: https://sg-public-api.hoyoverse.com/account/ma-passport/api/getUserMarketingStatus
/// TODO Implement: https://sg-public-api.hoyoverse.com/account/ma-passport/api/webLoginByPassword