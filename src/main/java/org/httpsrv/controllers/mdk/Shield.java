package org.httpsrv.controllers.mdk;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.*;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.thirdparty.JakartaMail;
import org.httpsrv.thirdparty.TwilioApi;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/shield/api", "hk4e_cn/mdk/shield/api", "mdk/shield/api"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
public class Shield implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindEmail">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindEmail</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - email: Email address<br>
     */
    @RequestMapping(value = "bindEmail")
    public ResponseEntity<LinkedHashMap<String, Object>> SendEmail(@RequestBody BindEmailBody body) {
        if(body == null || body.getEmail() == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        Account account = Database.findAccountByMobile(info.getMobile());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        account.setEmail(body.getEmail());

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginByAuthTicket">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginByAuthTicket</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     */
    @PostMapping(value = "loginByAuthTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginByAuthTicket(@RequestBody LoginByAuthTicketBody body, HttpServletRequest request) {
        if(body == null || body.getTicket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        Ticket info = Database.findTicket(body.getTicket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "Invalid ticket.", null));
        }

        Account account;
        if(info.getMobile().isEmpty()) {
            account = Database.findAccountByEmail(info.getEmail());
        }
        else {
            account = Database.findAccountByMobile(info.getMobile());
        }
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        info.delete();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("login_ticket", body.getTicket());
        data.put("need_realperson", account.getRequireRealPerson());
        data.put("reactivate_info", new LinkedHashMap<String, Object>() {{
            put("required", account.getRequireActivation());
            put("ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "Reactivation", !account.getMobile().isEmpty()).getId() : "");
        }});
        data.put("realname_info", new LinkedHashMap<String, Object>() {{
            put("required", account.getRequireRealPerson());
            put("action_ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "Realperson", !account.getMobile().isEmpty()).getId() : "");
            put("action_type", "");
        }});
        data.put("token", new LinkedHashMap<String, Object>() {{
            put("token", account.getSessionKey());
            put("token_version", 1);
        }});
        data.put("user_info", new LinkedHashMap<String, Object>() {{
            put("account_name", account.getName());
            put("aid", account.getId());
            put("area_code", Utils.maskString(account.getMobileArea()));
            put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            put("email", Utils.maskString(account.getEmail()));
            put("identity_code", Utils.maskString(account.getIdentityCard()));
            put("is_email_verify", account.getIsEmailVerified() ? 1 : 0);
            put("links", new ArrayList<>());
            put("mid", "12ya9usebi_hy");
            put("mobile", Utils.maskString(account.getMobile()));
            put("realname", Utils.maskString(account.getRealname()));
            put("rebind_area_code", "");
            put("rebind_mobile", "");
            put("rebind_mobile_time", "0");
            put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
            put("safe_mobile", Utils.maskString(account.getSafeMobile()));
            put("unmasked_email", "");
            put("unmasked_email_type", "0");
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptcha</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - email: email address<br>
     */
    @RequestMapping(value = "emailCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendEmailCaptcha(@RequestBody EmailCaptchaBody body) {
        if(body == null || body.getEmail() == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }
        String code = Random.generateCode();

        JakartaMail.sendMessage(body.getEmail(), "Bind email.", "Your activation code is " + code);
        info.setCode(code);
        info.setType("BindEmail");
        info.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindRealname">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindRealname</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - realname: Real name<br>
     *      - identity_card: Identity card<br>
     *      - is_crypto: is encrypted<br>
     */
    @RequestMapping(value = "bindRealname")
    public ResponseEntity<LinkedHashMap<String, Object>> SendRealname(@RequestBody BindRealnameBody body) {
        if(body == null || body.getIs_crypto() == null || body.getAction_ticket() == null || body.getRealname() == null || body.getIdentity_card() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        String identity_card = "";
        String realname = "";
        if(body.getIs_crypto()) {
            try {
                realname = RSA.DecryptPassword(realname);
                identity_card = RSA.DecryptPassword(identity_card);
            }catch(Exception ignored) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
            }

        }
        else {
            realname = body.getRealname();
            identity_card = body.getIdentity_card();
        }

        Account account = Database.findAccountByMobile(info.getMobile());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        account.setRealname(realname);
        account.setIdentityCard(identity_card);
        account.save();

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("realname_operation", "updated");
        data.put("identity_card", identity_card);
        data.put("realname", realname);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginMobile">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginMobile</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action: Action<br>
     *      - area: Mobile country<br>
     *      - captcha: verification code<br>
     *      - mobile: mobile number<br>
     */
    @RequestMapping(value = "loginMobile")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginMobile(@RequestBody LoginMobileBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getMobile() == null || body.getCaptcha() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getMobile(), "MobileLogin", true);
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        if(!info.getCode().equals(body.getCaptcha())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "Invalid code", null));
        }

        Account account = Database.findAccountByMobile(body.getMobile());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        info.delete();

        String token = Random.generateStr(30);
        String ticketId = "";
        account.setSessionKey(token);
        account.setCurrentDeviceId(device_id);
        if(!account.getCurrentIP().equals(request.getRemoteAddr()) && !account.getDeviceIds().contains(device_id)) {
            if(Database.findTicket(account.getMobile(), "DeviceGrant", true) == null) {
                Ticket newTicket = new Ticket(account.getMobile(), "DeviceGrant", "System", "", true);
                newTicket.save();
                ticketId = newTicket.getId();
                account.setRequireDeviceGrant(true);
            }
        }else if(!account.getDeviceIds().contains(device_id)) {
			account.getDeviceIds().add(device_id);
		}
		
        account.save();

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> acc = new LinkedHashMap<>();

        acc.put("uid", account.getId());
        acc.put("name", account.getName());
        acc.put("email", Utils.maskString(account.getEmail()));
        acc.put("mobile", Utils.maskString(account.getMobile()));
        acc.put("is_email_verify", account.getIsEmailVerified() ? '1' : '0');
        acc.put("realname", Utils.maskString(account.getRealname()));
        acc.put("identity_card", Utils.maskString(account.getIdentityCard()));
        acc.put("token", account.getSessionKey());
        acc.put("facebook_name", Utils.maskString(account.getFacebookName()));
        acc.put("google_name", Utils.maskString(account.getGoogleName()));
        acc.put("twitter_name", Utils.maskString(account.getTwitterName()));
        acc.put("game_center_name", Utils.maskString(account.getGameCenterName()));
        acc.put("apple_name", Utils.maskString(account.getAppleName()));
        acc.put("sony_name", Utils.maskString(account.getSonyName()));
        acc.put("tap_name", Utils.maskString(account.getTapName()));
        acc.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
        acc.put("reactivate_ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile(), "Reactivation", true).getId() : "");
        acc.put("area_code", Utils.maskString(account.getMobileArea()));
        acc.put("device_grant_ticket", ticketId.isEmpty() ? account.getRequireDeviceGrant() ? Database.findTicket(account.getMobile(), "DeviceGrant", true).getId() : "" : ticketId);
        acc.put("steam_name", Utils.maskString(account.getSteamName()));
        acc.put("unmasked_email", "");
        acc.put("unmasked_email_type", "0");
        acc.put("safe_mobile", Utils.maskString(account.getSafeMobile()));
        acc.put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
        acc.put("cx_name", Utils.maskString(account.getCxName()));

        data.put("account", acc);
        data.put("realperson_required", account.getRequireRealPerson());
        data.put("safe_moblie_required", account.getRequireSafeMobile());
        data.put("reactivate_required", account.getRequireActivation());
        data.put("device_grant_required", account.getRequireDeviceGrant());
        data.put("realname_operation", account.getRealPersonOperationName());

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginCaptcha</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - area: Mobile country<br>
     *      - mobile: Mobile number<br>
     */
    @RequestMapping(value = "loginCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginCaptcha(@RequestBody LoginCaptchaBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, @RequestHeader(value = "x-rpc-risky", required = false) String risky_type) {
        if(body == null || risky_type == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        Account account = Database.findAccountByMobile(body.getMobile());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "Phone number not found.", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(Database.findTicket(body.getMobile(), "MobileLogin", true) == null) {
            Ticket newTicket = new Ticket(body.getMobile(), "MobileLogin", "System", "use to login", true);
            newTicket.setCode(Random.generateCode());
            newTicket.save();
            TwilioApi.sendSms(body.getMobile(), "mobile login. your code is " + newTicket.getCode());
        }

        data.put("protocol", true);
        data.put("qr_enabled", Config.getHttpConfig().useQRLogin);
        data.put("log_level", Config.getHttpConfig().apiLogLevel);
        data.put("action", "Login");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/reactivateAccount">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/reactivateAccount</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     */
    @RequestMapping(value = "reactivateAccount")
    public ResponseEntity<LinkedHashMap<String, Object>> SendReactivateAccount(@RequestBody ReactivateAccountBody body, HttpServletRequest request) {
        if(body == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        Account account;
        if(info.getMobile().isEmpty()) {
            account = Database.findAccountByEmail(info.getEmail());
        }
        else {
            account = Database.findAccountByMobile(info.getMobile());
        }

        account.setRequireActivation(false);
        account.save();
        Database.deleteTicketById(body.getAction_ticket());

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> acc = new LinkedHashMap<>();

        acc.put("uid", account.getId());
        acc.put("name", account.getName());
        acc.put("email", Utils.maskString(account.getEmail()));
        acc.put("mobile", Utils.maskString(account.getMobile()));
        acc.put("is_email_verify", account.getIsEmailVerified() ? '1' : '0');
        acc.put("realname", Utils.maskString(account.getRealname()));
        acc.put("identity_card", Utils.maskString(account.getIdentityCard()));
        acc.put("token", account.getSessionKey());
        acc.put("facebook_name", Utils.maskString(account.getFacebookName()));
        acc.put("google_name", Utils.maskString(account.getGoogleName()));
        acc.put("twitter_name", Utils.maskString(account.getTwitterName()));
        acc.put("game_center_name", Utils.maskString(account.getGameCenterName()));
        acc.put("apple_name", Utils.maskString(account.getAppleName()));
        acc.put("sony_name", Utils.maskString(account.getSonyName()));
        acc.put("tap_name", Utils.maskString(account.getTapName()));
        acc.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
        acc.put("reactivate_ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "Reactivation", !account.getMobile().isEmpty()).getId() : "");
        acc.put("area_code", Utils.maskString(account.getMobileArea()));
        acc.put("device_grant_ticket", account.getRequireDeviceGrant() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "DeviceGrant", !account.getMobile().isEmpty()).getId() : "");
        acc.put("steam_name", Utils.maskString(account.getSteamName()));
        acc.put("unmasked_email", "");
        acc.put("unmasked_email_type", "0");
        acc.put("safe_mobile", Utils.maskString(account.getSafeMobile()));
        acc.put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
        acc.put("cx_name", Utils.maskString(account.getCxName()));

        data.put("account", acc);
        data.put("safe_moblie_required", account.getRequireSafeMobile());
        data.put("device_grant_required", account.getRequireDeviceGrant());
        data.put("realname_operation", account.getRealPersonOperationName());

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/mobileCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/mobileCaptcha</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - mobile: mobile number<br>
     *      - safe_mobile: is binding safe mobile<br>
     */
    @RequestMapping(value = "mobileCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendMobileCaptcha(@RequestBody MobileCaptchaBody body) {
        if(body == null || body.getMobile() == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }
        String code = Random.generateCode();

        TwilioApi.sendSms(info.getMobile(), "Bind mobile. Your activation code is " + code);
        info.setCode(code);
        info.setType("BindMobile");
        info.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verifyEmailCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verifyEmailCaptcha</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - captcha: code<br>
     */
    @RequestMapping(value = {"verifyEmailCaptcha", "verifyMobileCaptcha"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyEmailCaptcha(@RequestBody VerifyEmailCaptchaBody body) {
        if(body == null || body.getCaptcha() == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        if(!info.getCode().equals(body.getCaptcha())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_GRANT_INVALID_CODE, "The code you provided is invalid.", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptchaByActionTicket">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptchaByActionTicket</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - action_type: Action type<br>
     *      - game_token: Account session token<br>
     */
    @RequestMapping(value = "emailCaptchaByActionTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendEmailCaptchaByActionTicket(@RequestBody EmailCaptchaActionTicketBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, @RequestHeader(value = "x-rpc-risky", required = false) String risky_type) {
        if(body == null || risky_type == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error.", null));
        }

        String code = Random.generateCode();
        JakartaMail.sendMessage(info.getEmail(), "Device verification", "Your activation code is " + code);

        info.setCode(code);
        info.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - action_type: Action type<br>
     *      - game_token: Account session token<br>
     */
    @RequestMapping(value = "actionTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerify(@RequestBody ActionTicketBody body) {
        if(body == null || body.getAccount_id() == null || body.getGame_token() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        Account account = Database.findAccountByToken(body.getGame_token());
        if(account == null || account.getIsEmailVerified()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "System error.", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(Database.findTicket(account.getEmail(), "DeviceGrant", false) == null) {
            Ticket newTicket = new Ticket(account.getEmail(), "DeviceGrant", "System", "Bind mobile", false);
            newTicket.save();
            data.put("ticket", newTicket.getId());
        }
        else {
            Ticket newTicket = Database.findTicket(account.getEmail(), "DeviceGrant", false);
            data.put("ticket", newTicket.getId());
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

                                                                    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - token: Account session token<br>
     */
    @RequestMapping(value = "verify")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerify(@RequestBody VerifyBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getToken() == null || body.getUid() == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        Account account = Database.findAccountByToken(body.getToken());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        if(account.getDeviceIds().contains(device_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "Session has expired, pleaes login again.", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> acc = new LinkedHashMap<>();

        acc.put("uid", account.getId());
        acc.put("name", account.getName());
        acc.put("email", Utils.maskString(account.getEmail()));
        acc.put("mobile", Utils.maskString(account.getMobile()));
        acc.put("is_email_verify", account.getIsEmailVerified() ? '1' : '0');
        acc.put("realname", Utils.maskString(account.getRealname()));
        acc.put("identity_card", Utils.maskString(account.getIdentityCard()));
        acc.put("token", body.getToken());
        acc.put("facebook_name", Utils.maskString(account.getFacebookName()));
        acc.put("google_name", Utils.maskString(account.getGoogleName()));
        acc.put("twitter_name", Utils.maskString(account.getTwitterName()));
        acc.put("game_center_name", Utils.maskString(account.getGameCenterName()));
        acc.put("apple_name", Utils.maskString(account.getAppleName()));
        acc.put("sony_name", Utils.maskString(account.getSonyName()));
        acc.put("tap_name", Utils.maskString(account.getTapName()));
        acc.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
        acc.put("reactivate_ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "Reactivation", !account.getMobile().isEmpty()).getId() : "");
        acc.put("area_code", Utils.maskString(account.getMobileArea()));
        acc.put("device_grant_ticket", account.getRequireDeviceGrant() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "DeviceGrant", !account.getMobile().isEmpty()).getId() : "");
        acc.put("steam_name", Utils.maskString(account.getSteamName()));
        acc.put("unmasked_email", "");
        acc.put("unmasked_email_type", "0");
        acc.put("safe_mobile", Utils.maskString(account.getSafeMobile()));
        acc.put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
        acc.put("cx_name", Utils.maskString(account.getCxName()));

        data.put("account", acc);
        data.put("realperson_required", account.getRequireRealPerson());
        data.put("safe_moblie_required", account.getRequireSafeMobile());
        data.put("reactivate_required", account.getRequireActivation());
        data.put("realname_operation", account.getRealPersonOperationName());
        data.put("device_grant_required", account.getRequireDeviceGrant());

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/login">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/login</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - account: Username<br>
     *      - is_crypto: Is the password encrypted?<br>
     *      - password: Password<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     */
    @RequestMapping(value = "login")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogin(@RequestBody LoginBody body, @RequestHeader(value = "x-rpc-game_biz", required = false) String game_biz, @RequestHeader(value = "x-rpc-language", required = false) String lang, @RequestHeader(value = "x-rpc-channel_id", required = false) Integer channel_id, @RequestHeader(value = "x-rpc-risky", required = false) String risky_type, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getAccount() == null || body.getPassword() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        if(game_biz == null || !Utils.checkBizName(game_biz) || lang == null || !Utils.checkGameLanguage(lang)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        if(device_id == null || risky_type == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        if(channel_id == null || channel_id > 1) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "Thirdparty apps are not supported.", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        boolean isEncrypted = (body.getIs_crypto() != null) ? body.getIs_crypto() : false;
        Account account;
        String username = body.getAccount();
        String password = body.getPassword();

        if(isEncrypted) {
            try {
                password = RSA.DecryptPassword(password);
            } catch(Exception ignore) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Unable to decrypt the password.", null));
            }
        }

        if(password.length() < 15) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "The password format is incorrect. The password format is 8 to 15 characters and is a combination of numbers, uppercase and lowercase letters, and special symbols.", null));
        }

        if(username.contains("@")) {
            account = Database.findAccountByEmail(username);
        } else {
            account = Database.findAccountByMobile(username);
        }

        if(account == null || !account.checkAuthorizationByPassword(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "Account or password is mismatching.", null));
        }
        else {
            String token = Random.generateStr(30);
            String ticketId = "";
            account.setSessionKey(token);
            account.setCurrentDeviceId(device_id);
            if(!account.getCurrentIP().equals(request.getRemoteAddr()) && !account.getDeviceIds().contains(device_id)) {
                if(Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "DeviceGrant", !account.getMobile().isEmpty()) == null) {
                    Ticket newTicket = new Ticket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "DeviceGrant", "System", "", !account.getMobile().isEmpty());
                    newTicket.save();
                    ticketId = newTicket.getId();
                    account.setRequireDeviceGrant(true);
                }
            } else if(!account.getDeviceIds().contains(device_id)) {
                account.getDeviceIds().add(device_id);
            }

            account.save();

            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            LinkedHashMap<String, Object> acc = new LinkedHashMap<>();

            acc.put("uid", account.getId());
            acc.put("name", account.getName());
            acc.put("email", Utils.maskString(account.getEmail()));
            acc.put("mobile", Utils.maskString(account.getMobile()));
            acc.put("is_email_verify", account.getIsEmailVerified() ? '1' : '0');
            acc.put("realname", Utils.maskString(account.getRealname()));
            acc.put("identity_card", Utils.maskString(account.getIdentityCard()));
            acc.put("token", token);
            acc.put("facebook_name", Utils.maskString(account.getFacebookName()));
            acc.put("google_name", Utils.maskString(account.getGoogleName()));
            acc.put("twitter_name", Utils.maskString(account.getTwitterName()));
            acc.put("game_center_name", Utils.maskString(account.getGameCenterName()));
            acc.put("apple_name", Utils.maskString(account.getAppleName()));
            acc.put("sony_name", Utils.maskString(account.getSonyName()));
            acc.put("tap_name", Utils.maskString(account.getTapName()));
            acc.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            acc.put("reactivate_ticket", account.getRequireActivation() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "Reactivation", !account.getMobile().isEmpty()).getId() : "");
            acc.put("area_code", Utils.maskString(account.getMobileArea()));
            acc.put("device_grant_ticket", ticketId.isEmpty() ? account.getRequireDeviceGrant() ? Database.findTicket(account.getMobile().isEmpty() ? account.getEmail() : account.getMobile(), "DeviceGrant", !account.getMobile().isEmpty()).getId() : "" : ticketId);
            acc.put("steam_name", Utils.maskString(account.getSteamName()));
            acc.put("unmasked_email", "");
            acc.put("unmasked_email_type", "0");
            acc.put("safe_mobile", Utils.maskString(account.getSafeMobile()));
            acc.put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
            acc.put("cx_name", Utils.maskString(account.getCxName()));

            data.put("account", acc);
            data.put("realperson_required", account.getRequireRealPerson());
            data.put("safe_moblie_required", account.getRequireSafeMobile());
            data.put("reactivate_required", account.getRequireActivation());
            data.put("device_grant_required", account.getRequireDeviceGrant());
            data.put("realname_operation", account.getRealPersonOperationName());

            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
        }
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadFirebaseBlackList">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadFirebaseBlackList</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     */
    @RequestMapping(value = "loadFirebaseBlackList")
    public ResponseEntity<LinkedHashMap<String, Object>> SendFireBaseBlackList(Integer client, String game_key) {
        if(game_key == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        if(client == null || client < 1 || client > 3 || !Utils.checkBizName(game_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "Missing configuration", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("device_blacklist_version", Config.getHttpConfig().fireBaseBlacklistVersion);
        data.put("device_blacklist_switch", Config.getHttpConfig().enableFireBaseBlacklistDevicesSwitch);
        data.put("device_blacklist", "{\"min_api\":28,\"device\":[]}");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadConfig">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadConfig</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     */
    @RequestMapping(value = "loadConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(Integer client, String game_key) {
        if(game_key == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        if(client == null || client < 1 || client > 13 || !Utils.checkBizName(game_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "Missing configuration", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("id", Utils.getConfigId(client));
        data.put("game_key", game_key);
        data.put("app_id", ApplicationId.GENSHIN_RELEASE);
        data.put("client", Utils.getPlatformNameById(client));
        data.put("identity", "I_IDENTITY");
        data.put("guest", Config.getHttpConfig().enableGuestLogin);
        data.put("ignore_versions", "2.6.0");
        data.put("scene", (client == 3) ? "S_ACCOUNT" : "S_NORMAL");
        data.put("name", "原神");
        data.put("disable_regist", Config.getHttpConfig().disableRegistrations);
        data.put("enable_email_captcha", Config.getHttpConfig().useEmailCaptcha);
        data.put("thirdparty", Config.getHttpConfig().thirdPartyApps);
        data.put("disable_mmt", Config.getHttpConfig().disableMMT);
        data.put("server_guest", Config.getHttpConfig().enableServerGuest);
        data.put("thirdparty_ignore", Config.getHttpConfig().thirdPartyIgnored);
        data.put("enable_ps_bind_account", Config.getHttpConfig().enablePSBindAccount);
        data.put("thirdparty_login_configs", Config.getHttpConfig().thirdPartyConfigs);

        if(client < 3 || client == 8) {
            // Available only on phone devices.
            data.put("initialize_firebase", Config.getHttpConfig().enableFireBase);
        }
        else {
            data.put("initialize_firebase", false);
        }
        data.put("bbs_auth_login", Config.getHttpConfig().enableBBSAuthLogin);
        data.put("bbs_auth_login_ignore", Config.getHttpConfig().BBSAuthLoginIgnored);
        data.put("fetch_instance_id", Config.getHttpConfig().fetchCurrentInstanceId);
        data.put("enable_flash_login", Config.getHttpConfig().enableFlashLogin);
        data.put("enable_logo_18", Config.getHttpConfig().enableAdultLogoAndroid);
        data.put("logo_height", String.valueOf(Config.getHttpConfig().adultLogoAndroidHeight));
        data.put("logo_width", String.valueOf(Config.getHttpConfig().adultLogoAndroidWidth));
        data.put("enable_cx_bind_account", Config.getHttpConfig().enableCXBindAccount);
        if(client < 3 || client == 8) {
            // Available only on phone devices.
            data.put("firebase_blacklist_devices_switch", Config.getHttpConfig().enableFireBaseBlacklistDevicesSwitch);
            data.put("firebase_blacklist_devices_version", Config.getHttpConfig().fireBaseBlacklistVersion);
        }
        else {
            data.put("firebase_blacklist_devices_switch", false);
            data.put("firebase_blacklist_devices_version", 0);
        }
        data.put("hoyolab_auth_login", Config.getHttpConfig().enableHoyoLabAuthLogin);
        data.put("hoyolab_auth_login_ignore", Config.getHttpConfig().HoyoLabAuthIgnore);
        data.put("hoyoplay_auth_login", Config.getHttpConfig().enableHoyoPlayAuthLogin);
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindThirdparty
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/unBindThirdparty
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginByThirdparty
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/createMmt
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/checkAccount