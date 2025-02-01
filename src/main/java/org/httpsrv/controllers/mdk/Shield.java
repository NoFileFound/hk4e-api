package org.httpsrv.controllers.mdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.AthBindRealnameBody;
import org.httpsrv.data.body.shield.*;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.thirdparty.JakartaMail;
import org.httpsrv.thirdparty.TwilioApi;
import org.httpsrv.utils.Jackson;
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
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginByAuthTicket">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginByAuthTicket</a><br><br>
     *  Method: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     */
    @PostMapping(value = "loginByAuthTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginByAuthTicket(@RequestBody ShdLoginByAuthTicketBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String authTicket = body.getTicket();
        Integer appId = body.getApp_id();

        if(authTicket == null || appId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(appId != ApplicationId.GENSHIN_RELEASE.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(authTicket, "AuthLoginTicket");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数不合法", null));
        }

        Account acc = Database.findAccountById(myTicket.getAccountId());
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数不合法", null));
        }

        myTicket.delete();

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentDeviceId(device_id);

        Ticket _deviceGrantTicket = Database.findTicketByAccountId(acc.getId(), "device_grant");
        if(!acc.getCurrentIP().equals(request.getRemoteAddr()) && !acc.getDeviceIds().contains(device_id)) {
            if(_deviceGrantTicket == null) {
                _deviceGrantTicket = new Ticket(acc.getId(), "device_grant");
                _deviceGrantTicket.save();
            }
        } else if(!acc.getDeviceIds().contains(device_id)) {
            acc.getDeviceIds().add(device_id);
        }

        if(acc.getRealname().isEmpty()) {
            acc.setRequireRealPerson(true);
            acc.setRealPersonOperationName("bindRealname");
        }

        acc.save();
        Ticket reactivationTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket deviceGrantTicket = _deviceGrantTicket;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("account", new LinkedHashMap<>() {{
                put("id", acc.getId());
                put("name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("is_email_verify", acc.getIsEmailVerified() ? '1' : '0');
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_card", Utils.maskString(acc.getIdentityCard()));
                put("token", token);
                put("facebook_name", Utils.maskString(acc.getFacebookName()));
                put("google_name", Utils.maskString(acc.getGoogleName()));
                put("twitter_name", Utils.maskString(acc.getTwitterName()));
                put("game_center_name", Utils.maskString(acc.getGameCenterName()));
                put("apple_name", Utils.maskString(acc.getAppleName()));
                put("sony_name", Utils.maskString(acc.getSonyName()));
                put("tap_name", Utils.maskString(acc.getTapName()));
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("reactivate_ticket", (reactivationTicket == null) ? "" : reactivationTicket.getId());
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("device_grant_ticket", (deviceGrantTicket == null) ? "" : deviceGrantTicket.getId());
                put("steam_name", Utils.maskString(acc.getSteamName()));
                put("unmasked_email", "");
                put("unmasked_email_type", "0");
                put("cx_name", Utils.maskString(acc.getCxName()));
            }});
            put("realperson_required", acc.getRequireRealPerson());
            put("safe_moblie_required", acc.getRequireSafeMobile());
            put("reactivate_required", acc.getRequireActivation());
            put("device_grant_required", acc.getRequireDeviceGrant());
            put("realname_operation", acc.getRealPersonOperationName());
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptcha</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - email: email address<br>
     */
    @RequestMapping(value = "emailCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendEmailCaptcha(@RequestBody ShdEmailCaptchaBody body) {
        String actionTicket = body.getAction_ticket();
        String actionType = body.getAction_type();
        String email = body.getEmail();

        if(actionTicket == null || actionType == null || email == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String code = Random.generateCode();
        JakartaMail.sendMessage(body.getEmail(), "Activation code!", String.format("Your activation code is %s", code));
        myTicket.setCode(code);
        myTicket.setType(actionType);
        myTicket.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindRealname">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindRealname</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - realname: Real name<br>
     *      - identity_card: Identity card<br>
     *      - is_crypto: is encrypted<br>
     */
    @RequestMapping(value = "bindRealname")
    public ResponseEntity<LinkedHashMap<String, Object>> SendRealname(@RequestBody AthBindRealnameBody body) {
        String actionTicket = body.getTicket();
        String realName = body.getRealname();
        String identityCard = body.getIdentity();
        Boolean isEncrypted = body.getIs_crypto();

        if(actionTicket == null || realName == null || identityCard == null || isEncrypted == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "bind_realname");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String encIdentityCard = identityCard;
        String encRealName = realName;
        if(isEncrypted) {
            try {
                realName = RSA.DecryptPassword(realName);
                identityCard = RSA.DecryptPassword(identityCard);
            }catch(Exception ignored) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统错误", null));
            }
        }

        Account account = Database.findAccountById(myTicket.getAccountId());
        account.setRealname(realName);
        account.setIdentityCard(identityCard);
        account.setRealPersonOperationName("None");
        account.setRequireRealPerson(false);
        account.save();

        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("realname_operation", "completed");
            put("uid", account.getId());
            put("name", Utils.maskString(account.getName()));
            put("email", Utils.maskString(account.getEmail()));
            put("identity_card", encIdentityCard);
            put("realname", encRealName);
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginMobile">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginMobile</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action: Action<br>
     *      - area: Mobile country<br>
     *      - captcha: verification code<br>
     *      - mobile: mobile number<br>
     */
    @RequestMapping(value = "loginMobile")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginMobile(@RequestBody ShdLoginMobileBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String mobile = body.getMobile();
        String captcha = body.getCaptcha();

        if(mobile == null || captcha == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Account acc = Database.findAccountByMobile(mobile);
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Ticket myTicket = Database.findTicket(acc.getId(), "mobileLogin");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().equals(body.getCaptcha())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "验证码错误", null));
        }

        myTicket.delete();

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentDeviceId(device_id);

        Ticket _deviceGrantTicket = Database.findTicketByAccountId(acc.getId(), "device_grant");
        if(!acc.getCurrentIP().equals(request.getRemoteAddr()) && !acc.getDeviceIds().contains(device_id)) {
            if(_deviceGrantTicket == null) {
                _deviceGrantTicket = new Ticket(acc.getId(), "device_grant");
                _deviceGrantTicket.save();
            }
        } else if(!acc.getDeviceIds().contains(device_id)) {
            acc.getDeviceIds().add(device_id);
        }

        if(acc.getRealname().isEmpty()) {
            acc.setRequireRealPerson(true);
            acc.setRealPersonOperationName("bindRealname");
        }

        acc.save();
        Ticket reactivationTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket deviceGrantTicket = _deviceGrantTicket;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("account", new LinkedHashMap<>() {{
                put("id", acc.getId());
                put("name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("is_email_verify", acc.getIsEmailVerified() ? '1' : '0');
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_card", Utils.maskString(acc.getIdentityCard()));
                put("token", token);
                put("facebook_name", Utils.maskString(acc.getFacebookName()));
                put("google_name", Utils.maskString(acc.getGoogleName()));
                put("twitter_name", Utils.maskString(acc.getTwitterName()));
                put("game_center_name", Utils.maskString(acc.getGameCenterName()));
                put("apple_name", Utils.maskString(acc.getAppleName()));
                put("sony_name", Utils.maskString(acc.getSonyName()));
                put("tap_name", Utils.maskString(acc.getTapName()));
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("reactivate_ticket", (reactivationTicket == null) ? "" : reactivationTicket.getId());
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("device_grant_ticket", (deviceGrantTicket == null) ? "" : deviceGrantTicket.getId());
                put("steam_name", Utils.maskString(acc.getSteamName()));
                put("unmasked_email", "");
                put("unmasked_email_type", "0");
                put("cx_name", Utils.maskString(acc.getCxName()));
            }});
            put("realperson_required", acc.getRequireRealPerson());
            put("safe_moblie_required", acc.getRequireSafeMobile());
            put("reactivate_required", acc.getRequireActivation());
            put("device_grant_required", acc.getRequireDeviceGrant());
            put("realname_operation", acc.getRealPersonOperationName());
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loginCaptcha</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - area: Mobile country<br>
     *      - mobile: Mobile number<br>
     */
    @RequestMapping(value = "loginCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginCaptcha(@RequestBody ShdLoginCaptchaBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, @RequestHeader(value = "x-rpc-risky") String risky_type) {
        String areaCode = body.getArea();
        String mobile = body.getMobile();

        if(areaCode == null || mobile == null || risky_type == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Account account = Database.findAccountByMobile(mobile);
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "未找到账户", null));
        }

        Ticket myTicket = Database.findTicketByAccountId(account.getId(), "mobileLogin");
        if(myTicket == null) {
            myTicket = new Ticket(account.getId(), "mobileLogin");
            myTicket.setCode(Random.generateCode());
            myTicket.save();
            TwilioApi.sendSms(body.getMobile(), "mobile login. your code is " + myTicket.getCode());
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("protocol", true);
            put("qr_enabled", Config.getHttpConfig().useQRLogin);
            put("log_level", Config.getHttpConfig().apiLogLevel);
            put("action", "Login");
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/reactivateAccount">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/reactivateAccount</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     */
    @RequestMapping(value = "reactivateAccount")
    public ResponseEntity<LinkedHashMap<String, Object>> SendReactivateAccount(@RequestBody ShdReactivateAccountBody body, HttpServletRequest request) {
        String actionTicket = body.getAction_ticket();

        if(actionTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "reactivation");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Account acc = Database.findAccountByEmail(myTicket.getAccountId());

        acc.setRequireActivation(false);
        acc.save();

        myTicket.delete();

        Ticket deviceGrantTicket = Database.findTicketByAccountId(acc.getId(), "device_grant");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("account", new LinkedHashMap<>() {{
                put("id", acc.getId());
                put("name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("is_email_verify", acc.getIsEmailVerified() ? '1' : '0');
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_card", Utils.maskString(acc.getIdentityCard()));
                put("token", acc.getSessionKey());
                put("facebook_name", Utils.maskString(acc.getFacebookName()));
                put("google_name", Utils.maskString(acc.getGoogleName()));
                put("twitter_name", Utils.maskString(acc.getTwitterName()));
                put("game_center_name", Utils.maskString(acc.getGameCenterName()));
                put("apple_name", Utils.maskString(acc.getAppleName()));
                put("sony_name", Utils.maskString(acc.getSonyName()));
                put("tap_name", Utils.maskString(acc.getTapName()));
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("reactivate_ticket", "");
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("device_grant_ticket", (deviceGrantTicket == null) ? "" : deviceGrantTicket.getId());
                put("steam_name", Utils.maskString(acc.getSteamName()));
                put("unmasked_email", "");
                put("unmasked_email_type", "0");
                put("cx_name", Utils.maskString(acc.getCxName()));
            }});
            put("realperson_required", acc.getRequireRealPerson());
            put("safe_moblie_required", acc.getRequireSafeMobile());
            put("reactivate_required", acc.getRequireActivation());
            put("device_grant_required", acc.getRequireDeviceGrant());
            put("realname_operation", acc.getRealPersonOperationName());
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/mobileCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/mobileCaptcha</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - mobile: mobile number<br>
     *      - safe_mobile: is binding safe mobile<br>
     */
    @RequestMapping(value = "mobileCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendMobileCaptcha(@RequestBody ShdMobileCaptchaBody body) {
        String actionTicket = body.getAction_ticket();
        String actionType = body.getAction_type();
        String mobile = body.getMobile();

        if(actionTicket == null || actionType == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Account account = Database.findAccountByMobile(mobile);
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String code = Random.generateCode();

        TwilioApi.sendSms(mobile, String.format("Your activation code is %s", code));
        myTicket.setCode(code);
        myTicket.setType(actionType);
        myTicket.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verifyEmailCaptcha">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verifyEmailCaptcha</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - action_type: Action type<br>
     *      - captcha: code<br>
     */
    @RequestMapping(value = {"verifyEmailCaptcha", "verifyMobileCaptcha"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyEmailCaptcha(@RequestBody ShdVerifyEmailCaptchaBody body) {
        String actionTicket = body.getAction_ticket();
        String actionType = body.getAction_type();
        String captcha = body.getCaptcha();

        if(captcha == null || actionTicket == null || actionType == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().equals(captcha)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "验证码错误", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptchaByActionTicket">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/emailCaptchaByActionTicket</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - action_type: Action type<br>
     *      - game_token: Account session token<br>
     */
    @RequestMapping(value = "emailCaptchaByActionTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendEmailCaptchaByActionTicket(@RequestBody ShdEmailCaptchaActionTicketBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, @RequestHeader(value = "x-rpc-risky") String risky_type) {
        String actionTicket = body.getAction_ticket();
        String actionType = body.getAction_type();

        if(actionTicket == null || risky_type == null || device_id == null || actionType == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Account account = Database.findAccountById(myTicket.getAccountId());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String code = Random.generateCode();
        JakartaMail.sendMessage(account.getEmail(), "Activation code", String.format("Your activation code is %s", code));

        myTicket.setCode(code);
        myTicket.setModifiedAt(String.valueOf(System.currentTimeMillis() / 1000));
        myTicket.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/actionTicket">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/actionTicket</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - action_type: Action type<br>
     *      - game_token: Account session token<br>
     */
    @RequestMapping(value = "actionTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendActionTicket(@RequestBody ShdActionTicketBody body) {
        String accountId = body.getAccount_id();
        String gameToken = body.getGame_token();
        String actionType = body.getAction_type();

        if((accountId == null && gameToken == null) || actionType == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
        }

        Account account;
        if(isDebug() && (accountId != null && !accountId.isEmpty())) {
            account = Database.findAccountById(accountId);
        }
        else {
            account = Database.findAccountByToken(gameToken);
        }

        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
        }

        Ticket myTicket = Database.findTicketByAccountId(account.getId(), actionType);
        if(myTicket == null) {
            myTicket = new Ticket(account.getId(), actionType);
            myTicket.save();
        }

        String ticketId = myTicket.getId();
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ticket", ticketId);
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/verify</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account id: Account uid<br>
     *      - token: Account session token<br>
     */
    @RequestMapping(value = "verify")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerify(@RequestBody ShdVerifyBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String accountId = body.getUid();
        String token = body.getToken();

        if(accountId == null || token == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Account acc = Database.findAccountByToken(token);
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
        }

        if(acc.getDeviceIds().contains(device_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "请重新登录", null));
        }

        Ticket reactivationTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
        Ticket deviceGrantTicket = Database.findTicketByAccountId(acc.getId(), "device_grant");
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("account", new LinkedHashMap<>() {{
                put("id", acc.getId());
                put("name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("is_email_verify", acc.getIsEmailVerified() ? '1' : '0');
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_card", Utils.maskString(acc.getIdentityCard()));
                put("token", token);
                put("facebook_name", Utils.maskString(acc.getFacebookName()));
                put("google_name", Utils.maskString(acc.getGoogleName()));
                put("twitter_name", Utils.maskString(acc.getTwitterName()));
                put("game_center_name", Utils.maskString(acc.getGameCenterName()));
                put("apple_name", Utils.maskString(acc.getAppleName()));
                put("sony_name", Utils.maskString(acc.getSonyName()));
                put("tap_name", Utils.maskString(acc.getTapName()));
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("reactivate_ticket", (reactivationTicket == null) ? "" : reactivationTicket.getId());
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("device_grant_ticket", (deviceGrantTicket == null) ? "" : deviceGrantTicket.getId());
                put("steam_name", Utils.maskString(acc.getSteamName()));
                put("unmasked_email", "");
                put("unmasked_email_type", "0");
                put("cx_name", Utils.maskString(acc.getCxName()));
            }});
            put("realperson_required", acc.getRequireRealPerson());
            put("safe_moblie_required", acc.getRequireSafeMobile());
            put("reactivate_required", acc.getRequireActivation());
            put("device_grant_required", acc.getRequireDeviceGrant());
            put("realname_operation", acc.getRealPersonOperationName());
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/login">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/login</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account: Username<br>
     *      - is_crypto: Is the password encrypted?<br>
     *      - password: Password<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     */
    @RequestMapping(value = "login")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogin(@RequestBody ShdLoginBody body, @RequestHeader(value = "x-rpc-language") String lang, @RequestHeader(value = "x-rpc-risky") String risky_type, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String account = body.getAccount();
        String password = body.getPassword();
        Boolean isEncrypted = body.getIs_crypto();
        String gameBiz = body.getGame_key();

        if(account == null || password == null || isEncrypted == null || device_id == null || risky_type == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
        }

        if((gameBiz != null && !Utils.checkBizName(gameBiz)) || lang == null || !Utils.checkGameLanguage(lang)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(isEncrypted) {
            try {
                password = RSA.DecryptPassword(password);
            } catch(Exception ignore) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "系统请求失败，请返回重试", null));
            }
        }

        if(password.length() < 15) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "密碼格式為8-30位，並且至少包含數字、大小寫字母、英文特殊符號其中兩種", null));
        }

        Account acc = Database.findAccountByEmail(account);
        if(acc == null || !acc.checkAuthorizationByPassword(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "未找到账户", null));
        }
        else {
            String token = Random.generateStr(30);
            acc.setSessionKey(token);
            acc.setCurrentDeviceId(device_id);

            Ticket _deviceGrantTicket = Database.findTicketByAccountId(acc.getId(), "device_grant");
            if(!acc.getCurrentIP().equals(request.getRemoteAddr()) && !acc.getDeviceIds().contains(device_id)) {
                if(_deviceGrantTicket == null) {
                    _deviceGrantTicket = new Ticket(acc.getId(), "device_grant");
                    _deviceGrantTicket.save();
                }
            } else if(!acc.getDeviceIds().contains(device_id)) {
                acc.getDeviceIds().add(device_id);
            }

            if(acc.getRealname().isEmpty()) {
                acc.setRequireRealPerson(true);
                acc.setRealPersonOperationName("bindRealname");
            }

            acc.save();
            Ticket reactivationTicket = Database.findTicketByAccountId(acc.getId(), "reactivation");
            Ticket deviceGrantTicket = _deviceGrantTicket;
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
                put("account", new LinkedHashMap<>() {{
                    put("id", acc.getId());
                    put("name", acc.getName());
                    put("email", Utils.maskString(acc.getEmail()));
                    put("mobile", Utils.maskString(acc.getMobile()));
                    put("is_email_verify", acc.getIsEmailVerified() ? '1' : '0');
                    put("realname", Utils.maskString(acc.getRealname()));
                    put("identity_card", Utils.maskString(acc.getIdentityCard()));
                    put("token", token);
                    put("facebook_name", Utils.maskString(acc.getFacebookName()));
                    put("google_name", Utils.maskString(acc.getGoogleName()));
                    put("twitter_name", Utils.maskString(acc.getTwitterName()));
                    put("game_center_name", Utils.maskString(acc.getGameCenterName()));
                    put("apple_name", Utils.maskString(acc.getAppleName()));
                    put("sony_name", Utils.maskString(acc.getSonyName()));
                    put("tap_name", Utils.maskString(acc.getTapName()));
                    put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                    put("reactivate_ticket", (reactivationTicket == null) ? "" : reactivationTicket.getId());
                    put("area_code", Utils.maskString(acc.getMobileArea()));
                    put("device_grant_ticket", (deviceGrantTicket == null) ? "" : deviceGrantTicket.getId());
                    put("steam_name", Utils.maskString(acc.getSteamName()));
                    put("unmasked_email", "");
                    put("unmasked_email_type", "0");
                    put("cx_name", Utils.maskString(acc.getCxName()));
                }});
                put("realperson_required", acc.getRequireRealPerson());
                put("safe_moblie_required", acc.getRequireSafeMobile());
                put("reactivate_required", acc.getRequireActivation());
                put("device_grant_required", acc.getRequireDeviceGrant());
                put("realname_operation", acc.getRealPersonOperationName());
            }}));
        }
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadFirebaseBlackList">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadFirebaseBlackList</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     */
    @RequestMapping(value = "loadFirebaseBlackList")
    public ResponseEntity<LinkedHashMap<String, Object>> SendFireBaseBlackList(Integer client, String game_key) throws JsonProcessingException {
        if(game_key == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(client == null || client < 1 || client > 3 || !Utils.checkBizName(game_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "参数错误", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("device_blacklist_version", Config.getHttpConfig().fireBaseBlacklistVersion);
        data.put("device_blacklist_switch", Config.getHttpConfig().enableFireBaseBlacklistDevicesSwitch);
        data.put("device_blacklist", Jackson.toJsonString(new LinkedHashMap<>() {{
            put("min_api", 28);
            put("device", Jackson.toJsonString(Database.findAllBlacklistedDevices()));
        }}));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadConfig">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadConfig</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     */
    @RequestMapping(value = "loadConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(Integer client, String game_key) {
        if(game_key == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(client == null || client < 1 || client > 13 || !Utils.checkBizName(game_key)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "参数错误", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("id", Utils.getConfigId(client));
        data.put("game_key", game_key);
        data.put("app_id", ApplicationId.GENSHIN_RELEASE.getValue());
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
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/bindEmail