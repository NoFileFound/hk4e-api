package org.httpsrv.controllers.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.PstCnCheckRiskVerifiedBody;
import org.httpsrv.data.body.account.PstCnLoginByMobileCaptchaBody;
import org.httpsrv.data.body.account.PstCnLoginByPasswordBody;
import org.httpsrv.data.body.account.PstCnQueryQRLoginStatusBody;
import org.httpsrv.data.body.account.PstCnReactivateAccountBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"account/ma-cn-passport/app", "account/ma-cn-passport/web"}, produces = "application/json")
public class PassportCN implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/queryQRLoginStatus">https://passport-api.mihoyo.com/account/ma-cn-passport/app/queryQRLoginStatus</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     */
    @PostMapping(value = "queryQRLoginStatus")
    public ResponseEntity<LinkedHashMap<String, Object>> queryQRLoginStatus(@RequestBody PstCnQueryQRLoginStatusBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String actionTicket = body.getTicket();

        if(actionTicket == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "QRLogin_CN");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_QR_CODE_EXPIRED, "二维码已失效，请刷新后重新扫描", null));
        }

        String qrState = (myTicket.getState() == 0) ? "Created" : (myTicket.getState() == 1) ? "Scanned" : "Confirmed";
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("status", qrState);
            put("app_id", Config.getPropertiesVar().appId.get(0));
            put("client_type", 4);
            put("created_at", myTicket.getCreatedAt());
            put("scanned_at", myTicket.getModifiedAt());
            put("tokens", new ArrayList<>());
            if(qrState.equals("Confirmed")) {
                Account acc = Database.findAccountByDeviceId(device_id);

                Ticket _realPersonActionTicket = null;
                String _actionType = "";
                for (String action : new String[]{"bind_realname", "modify_realname", "bind_realperson", "verify_realperson"}) {
                    _realPersonActionTicket = Database.findTicketByAccountId(acc.getId(), action);
                    if (_realPersonActionTicket != null) {
                        _actionType = action;
                        break;
                    }
                }

                Ticket realPersonActionTicket = _realPersonActionTicket;
                String actionType = _actionType;

                put("user_info", new LinkedHashMap<>() {{
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
                    put("password_time", "0");
                    put("unmasked_email", "");
                    put("unmasked_email_type", 0);
                }});
                put("realname_info", new LinkedHashMap<String, Object>() {{
                    put("required", (realPersonActionTicket != null));
                    put("action_ticket", (realPersonActionTicket == null) ? "" : realPersonActionTicket.getId());
                    put("action_type", actionType);
                }});
                put("need_realperson", acc.getRequireRealPerson());
            }
            else {
                put("user_info", null);
                put("realname_info", null);
                put("need_realperson", false);
            }
        }}));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/createQRLogin">https://passport-api.mihoyo.com/account/ma-cn-passport/app/createQRLogin</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "createQRLogin")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCreateQRLogin(@RequestHeader(value = "x-rpc-device_id") String device_id, @RequestHeader(value = "x-rpc-app_id") String app_id) {
        if(device_id == null || app_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        if(!Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Long time = (System.currentTimeMillis() + 60000) / 1000;
        Ticket qrTicket = new Ticket("QRLogin_CN", time);
        qrTicket.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("url", String.format("https://user.mihoyo.com/login-platform/mobile.html?expire=%s\\u0026tk=%s\\u0026token_types=4#/login/qr", time, qrTicket.getId()));
            put("token", qrTicket.getId());
        }}));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/checkRiskVerified">https://passport-api.mihoyo.com/account/ma-cn-passport/app/checkRiskVerified</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     */
    @PostMapping(value = "checkRiskVerified")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCheckRiskVerified(@RequestBody PstCnCheckRiskVerifiedBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String actionTicket = body.getAction_ticket();

        if(actionTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "loginTicketCn");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "参数不合法", null));
        }

        String token = Random.generateStr(30);
        Account acc = Database.findAccountById(myTicket.getAccountId());
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setCurrentDeviceId(device_id);
        acc.save();

        Ticket _realPersonActionTicket = null;
        String _actionType = "";
        for (String action : new String[]{"bind_realname", "modify_realname", "bind_realperson", "verify_realperson"}) {
            _realPersonActionTicket = Database.findTicketByAccountId(acc.getId(), action);
            if (_realPersonActionTicket != null) {
                _actionType = action;
                break;
            }
        }

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket realPersonActionTicket = _realPersonActionTicket;
        String actionType = _actionType;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("login_ticket", actionTicket);
            put("need_realperson", (actionType .equals("bind_realperson") || actionType.equals("modify_realperson")));
            put("reactivate_info", new LinkedHashMap<String, Object>() {{
                put("required", reactivateActionTicket != null);
                put("ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            }});
            put("realname_info", new LinkedHashMap<String, Object>() {{
                put("required", (realPersonActionTicket != null));
                put("action_ticket", (realPersonActionTicket == null) ? "" : realPersonActionTicket.getId());
                put("action_type", actionType);
            }});
            put("token", new LinkedHashMap<String, Object>() {{
                put("token", token);
                put("token_version", 1);
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
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
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/reactivateAccount">https://passport-api.mihoyo.com/account/ma-cn-passport/app/reactivateAccount</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     */
    @PostMapping(value = "reactivateAccount")
    public ResponseEntity<LinkedHashMap<String, Object>> SendReactivateAccount(@RequestBody PstCnReactivateAccountBody body, HttpServletRequest request) {
        String actionTicket = body.getAction_ticket();

        if(actionTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "reactivation");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请前往官网/商店下载最新版本", null));
        }

        Account acc = Database.findAccountById(myTicket.getAccountId());
        acc.setRequireActivation(false);
        acc.save();

        myTicket.delete();

        Ticket _realPersonActionTicket = null;
        String _actionType = "";
        for (String action : new String[]{"bind_realname", "modify_realname", "bind_realperson", "verify_realperson"}) {
            _realPersonActionTicket = Database.findTicketByAccountId(acc.getId(), action);
            if (_realPersonActionTicket != null) {
                _actionType = action;
                break;
            }
        }

        Ticket realPersonActionTicket = _realPersonActionTicket;
        String actionType = _actionType;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("login_ticket", "");
            put("need_realperson", (actionType .equals("bind_realperson") || actionType.equals("modify_realperson")));
            put("realname_info", new LinkedHashMap<String, Object>() {{
                put("required", (realPersonActionTicket != null));
                put("action_ticket", (realPersonActionTicket == null) ? "" : realPersonActionTicket.getId());
                put("action_type", actionType);
            }});
            put("token", new LinkedHashMap<String, Object>() {{
                put("token", acc.getSessionKey());
                put("token_version", 1);
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
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
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
      *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByPassword">https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByPassword</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
      *      - account: Account name.<br>
      *      - password: Password.<br>
      */
    @PostMapping(value = "loginByPassword")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginByPassword(@RequestBody PstCnLoginByPasswordBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
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
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_CN_ACCOUNT_NEW_DEVICE_DETECTED, "您正在新裝置上登入，為確保是您本人操作，請先驗證身分後再登入", null));
        }

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setCurrentDeviceId(device_id);
        acc.save();

        Ticket _realPersonActionTicket = null;
        String _actionType = "";
        for (String action : new String[]{"bind_realname", "modify_realname", "bind_realperson", "verify_realperson"}) {
            _realPersonActionTicket = Database.findTicketByAccountId(acc.getId(), action);
            if (_realPersonActionTicket != null) {
                _actionType = action;
                break;
            }
        }

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket realPersonActionTicket = _realPersonActionTicket;
        String actionType = _actionType;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("login_ticket", "");
            put("need_realperson", (actionType .equals("bind_realperson") || actionType.equals("modify_realperson")));
            put("reactivate_info", new LinkedHashMap<String, Object>() {{
                put("required", reactivateActionTicket != null);
                put("ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            }});
            put("realname_info", new LinkedHashMap<String, Object>() {{
                put("required", (realPersonActionTicket != null));
                put("action_ticket", (realPersonActionTicket == null) ? "" : realPersonActionTicket.getId());
                put("action_type", actionType);
            }});
            put("token", new LinkedHashMap<String, Object>() {{
                put("token", token);
                put("token_version", 1);
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
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
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByMobileCaptcha">https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByMobileCaptcha</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - area_code: phone country code<br>
     *      - mobile: mobile<br>
     *      - captcha: captcha<br>
     */
    @PostMapping(value = "loginByMobileCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginByMobileCaptcha(@RequestBody PstCnLoginByMobileCaptchaBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        String mobile = body.getMobile();
        String verCode = body.getCaptcha();

        if(mobile == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数错误", null));
        }

        try {
            mobile = RSA.DecryptPassword(mobile);
        }catch (Exception e) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_SYSTEM_ERROR, "系统错误", null));
        }

        Account acc = Database.findAccountByMobile(mobile);
        Ticket myTicket = Database.findTicketByAccountId(acc.getId(), "mobileLogin");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "系统请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().equals(verCode)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_CAPTCHA_MISMATCH, "验证码错误", null));
        }

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setCurrentDeviceId(device_id);
        acc.save();

        Ticket _realPersonActionTicket = null;
        String _actionType = "";
        for (String action : new String[]{"bind_realname", "modify_realname", "bind_realperson", "verify_realperson"}) {
            _realPersonActionTicket = Database.findTicketByAccountId(acc.getId(), action);
            if (_realPersonActionTicket != null) {
                _actionType = action;
                break;
            }
        }

        Ticket reactivateActionTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket realPersonActionTicket = _realPersonActionTicket;
        String actionType = _actionType;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("login_ticket", "");
            put("need_realperson", (actionType .equals("bind_realperson") || actionType.equals("modify_realperson")));
            put("reactivate_info", new LinkedHashMap<String, Object>() {{
                put("required", reactivateActionTicket != null);
                put("ticket", (reactivateActionTicket == null) ? "" : reactivateActionTicket.getId());
            }});
            put("realname_info", new LinkedHashMap<String, Object>() {{
                put("required", (realPersonActionTicket != null));
                put("action_ticket", (realPersonActionTicket == null) ? "" : realPersonActionTicket.getId());
                put("action_type", actionType);
            }});
            put("token", new LinkedHashMap<String, Object>() {{
                put("token", token);
                put("token_version", 1);
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
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
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/checkReactivateByActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/addRealname
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/bindMobileByThirdpartyBindMobileTicket