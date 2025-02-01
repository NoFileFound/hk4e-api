package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.PstAppLoginByAuthTicketBody;
import org.httpsrv.data.body.account.PstAppLoginByPasswordBody;
import org.httpsrv.data.body.account.PstReactivateAccountBody;
import org.httpsrv.data.body.web.WebGetUserMarketingStatusBody;
import org.httpsrv.data.body.web.WebLoginByPasswordBody;
import org.httpsrv.data.body.web.WebUpdateUserMarketingStatusBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.database.entity.WebProfile;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/account/ma-passport/api", "account/ma-passport/api", "hk4e_global/account/ma-passport/token", "account/ma-password/token"}, produces = "application/json")
public class Passport implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/webLoginByPassword">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/webLoginByPassword</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account: Account name<br>
     *      - password: Password<br>
     *      - token_type: Token type<br>
     */
    @PostMapping(value = "webLoginByPassword")
    public ResponseEntity<LinkedHashMap<String, Object>> SendWebLoginByPassword(@RequestBody WebLoginByPasswordBody body, HttpServletRequest request) {
        String account = body.getAccount();
        String password = body.getPassword();

        if(account == null || password == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WEBAPI_UNAUTHORIZED, "UNAUTHORIZED", null));
        }

        try {
            account = RSA.DecryptPassword(account);
            password = RSA.DecryptPassword(password);
        } catch (Exception ignored) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_SYSTEM_ERROR, "系统错误", null));
        }

        if(password.length() < 8 || password.length() > 15) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PASSWORD_FORMAT_ERROR, "密碼格式為8-30位，並且至少包含數字、大小寫字母、英文特殊符號其中兩種", null));
        }

        Account acc = Database.findAccountByEmail(account);
        if(acc == null || !acc.checkAuthorizationByPassword(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ACCOUNT_NOT_EXIST, "帳號或密碼錯誤", null));
        }

        WebProfile profile = Database.findWebProfile(acc.getId());
        String cookieToken = Random.generateStr(30);
        profile.setCookieToken(cookieToken);
        profile.setIpAddress(request.getRemoteAddr());
        profile.save();

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("reactivate_action_ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getUserMarketingStatus">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getUserMarketingStatus</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - entity: Entity<br>
     */
    @SuppressWarnings("unused")
    @PostMapping(value = "getUserMarketingStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGetMarketingStatus(@RequestBody WebGetUserMarketingStatusBody body, @CookieValue(value = "cookie_token_v2") String cookieToken) {
        if(cookieToken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        WebProfile profile = Database.findWebProfileByCookie(cookieToken);
        if(profile == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("status", profile.getEnableEmailNotifications() ? 2 : 1);
            put("update_time", System.currentTimeMillis() / 1000);
            put("need_reauth", false);
        }}));
    }

   /**
    *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/updateUserMarketingStatus">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/updateUserMarketingStatus</a><br><br>
    *  Method: POST<br>
    *  Content-Type: application/json<br><br>
    *  Parameters:<br>
    *      - entity: Entity<br>
    *      - marketing_status: Marketing status<br>
    */
    @PostMapping(value = "updateUserMarketingStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> SendUpdateMarketingStatus(@RequestBody WebUpdateUserMarketingStatusBody body, @CookieValue(value = "cookie_token_v2") String cookieToken) {
        if(cookieToken == null || body.getMarketing_status() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        WebProfile profile = Database.findWebProfileByCookie(cookieToken);
        if(profile == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        profile.setEnableEmailNotifications(body.getMarketing_status() == 2);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/logout">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/logout</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "logout")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogout(@CookieValue(value = "cookie_token_v2") String cookieToken) {
        if(cookieToken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/token/verifyCookieToken">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/token/verifyCookieToken</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "verifyCookieToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyCookieToken(@CookieValue(value = "cookie_token_v2") String cookieToken, HttpServletRequest request) {
        if(cookieToken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Account acc = Database.findAccountByStoken(Database.findWebProfileByCookie(cookieToken).getAccountId());
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("token", new ArrayList<>());
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/token/verifySToken">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/token/verifySToken</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "verifySToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifySToken(@CookieValue(value = "stoken") String stoken, HttpServletRequest request) {
        if(stoken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Account acc = Database.findAccountByStoken(stoken);
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket bindEmailActionTicket = Database.findTicketByAccountId(acc.getId(), "bind_email");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("reactivate_action_ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            put("bind_email_action_ticket", (bindEmailActionTicket == null) ? "" : bindEmailActionTicket.getId());
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("token", new LinkedHashMap<>() {{
                put("token", (bindEmailActionTicket != null) ? "" : acc.getStokenKey());
                put("token_type", 1); // stoken
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByAuthTicket">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByAuthTicket</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - ticket: Ticket id<br>
     */
    @PostMapping(value = "appLoginByAuthTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAppLoginByAuthTicket(@RequestBody PstAppLoginByAuthTicketBody body, HttpServletRequest request) {
        String authTicket = body.getTicket();

        if(authTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(authTicket, "AuthLoginTicket");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Account acc = Database.findAccountById(myTicket.getAccountId());
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        myTicket.delete();

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket bindEmailActionTicket = Database.findTicketByAccountId(acc.getId(), "bind_email");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("reactivate_action_ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            put("bind_email_action_ticket", (bindEmailActionTicket == null) ? "" : bindEmailActionTicket.getId());
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("token", new LinkedHashMap<>() {{
                put("token", (bindEmailActionTicket != null) ? "" : acc.getStokenKey());
                put("token_type", 1); // stoken
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/reactivateAccount">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/reactivateAccount</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     */
    @PostMapping(value = "reactivateAccount")
    public ResponseEntity<LinkedHashMap<String, Object>> SendReactivateAccount(@RequestBody PstReactivateAccountBody body, HttpServletRequest request) {
        String actionTicket = body.getAction_ticket();

        if(actionTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "reactivation");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请前往官网/商店下载最新版本", null));
        }

        Account account = Database.findAccountById(myTicket.getAccountId());
        account.setRequireActivation(false);
        account.save();

        myTicket.delete();

        Ticket bindEmailActionTicket = Database.findTicket(account.getId(), "bind_email");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("reactivate_action_ticket", "");
            put("bind_email_action_ticket", (bindEmailActionTicket == null) ? "" : bindEmailActionTicket.getId());
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("token", new LinkedHashMap<>() {{
                put("token", (bindEmailActionTicket != null) ? "" : account.getSessionKey());
                put("token_type", 3);
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", account.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", account.getName());
                put("email", Utils.maskString(account.getEmail()));
                put("is_email_verify", account.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(account.getMobileArea()));
                put("mobile", Utils.maskString(account.getMobile()));
                put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(account.getSafeMobile()));
                put("realname", Utils.maskString(account.getRealname()));
                put("identity_code", Utils.maskString(account.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByPassword">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByPassword</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account: Account name/email<br>
     *      - password: Password<br>
     */
    @PostMapping(value = "appLoginByPassword")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAppLoginByPassword(@RequestBody PstAppLoginByPasswordBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String account = body.getAccount();
        String password = body.getPassword();

        if(account == null || password == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        try {
            account = RSA.DecryptPassword(account);
            password = RSA.DecryptPassword(password);
        } catch (Exception ignored) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_SYSTEM_ERROR, "系统错误", null));
        }

        if(!account.contains("@")) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ACCOUNT_FORMAT_ERROR, "帳號格式錯誤", null));
        }

        if(password.length() < 8 || password.length() > 15) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PASSWORD_FORMAT_ERROR, "密碼格式為8-30位，並且至少包含數字、大小寫字母、英文特殊符號其中兩種", null));
        }

        Account acc = Database.findAccountByEmail(account);
        if(acc == null || !acc.checkAuthorizationByPassword(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ACCOUNT_MISMATCH, "帳號或密碼錯誤", null));
        }

        if(!acc.getDeviceIds().contains(device_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ACCOUNT_NEW_DEVICE_DETECTED, "您正在新裝置上登入，為確保是您本人操作，請先驗證身分後再登入", null));
        }

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setCurrentDeviceId(device_id);
        acc.save();

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket bindEmailActionTicket = Database.findTicketByAccountId(acc.getId(), "bind_email");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("reactivate_action_ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            put("bind_email_action_ticket", (bindEmailActionTicket == null) ? "" : bindEmailActionTicket.getId());
            put("ext_user_info", new LinkedHashMap<>() {{
                put("guardian_email", "");
                put("birth", "0");
            }});
            put("token", new LinkedHashMap<>() {{
                put("token", (bindEmailActionTicket != null) ? "" : token);
                put("token_type", 3); // game token
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");  // last mobile changed time.
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0"); // last password changed time.
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getConfig">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getConfig</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "getConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(HttpServletRequest request) {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ip", new LinkedHashMap<String, Object>() {{
                put("country_code", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("language", "en-us");
                put("area_code", GeoIP.getCountryMobileCode(request.getRemoteAddr()));
            }});
            put("area_wl", new ArrayList<>(List.of("KR")));
            put("realname_wl", new ArrayList<>());
            put("guardian_age_limit", "14");
            put("disable_mmt", Config.getHttpConfig().disableMMT);
            put("show_birthday", "false");
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getSwitchStatus">https://hk4e-sdk-os-static.hoyoverse.com/hk4e_global/account/ma-passport/api/getSwitchStatus</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - appId: Application id<br>
     *      - platform: Platform<br>
     */
    @GetMapping(value = "getSwitchStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> SendSwitchStatus(String app_id, Integer platform) {
        if (app_id == null || platform == null || !Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
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
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/appLoginByEmailCaptcha
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/sendEmailNotificationByGameToken
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-passport/api/bindThirdPartyBySToken
/// TODO Implement: https://hk4e-sdk-os-hoyoverse.com/hk4e_global/account/ma-passport/token/verifyLToken