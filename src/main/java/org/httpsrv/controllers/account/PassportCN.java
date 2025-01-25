package org.httpsrv.controllers.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.algorithms.Random;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.MobileCaptchaBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.thirdparty.GeoIP;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/ma-cn-passport", produces = "application/json")
public class PassportCN implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByMobileCaptcha">https://passport-api.mihoyo.com/account/ma-cn-passport/app/loginByMobileCaptcha</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - area_code: phone country code<br>
     *      - mobile: mobile<br>
     *      - action_type: action type<br>
     *      - captcha: captcha<br>
     */
    @PostMapping(value = "app/loginByMobileCaptcha")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginByMobileCaptcha(@RequestBody MobileCaptchaBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getMobile() == null || body.getAction_ticket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter error", null));
        }

        String mobile = body.getMobile();
        String area_code = body.getArea_code();
        try {
            mobile = RSA.DecryptPassword(mobile);
            area_code = RSA.DecryptPassword(area_code);
        }catch (Exception e) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_ACCOUNT_DECRYPTION_FAILED, "Decryption failed", null));
        }

        Account acc = Database.findAccountByMobile(mobile);

        String token = Random.generateStr(30);
        acc.setSessionKey(token);
        acc.setCurrentIP(request.getRemoteAddr());
        acc.setDeviceId(device_id);
        acc.setMobileArea(area_code);
        acc.save();

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("login_ticket", "");
        data.put("need_realperson", false);
        data.put("reactivation_info", new LinkedHashMap<String, Object>() {{
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
            put("area_code", acc.getMobileArea());
            put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            put("email", acc.getEmail());
            put("identity_code", acc.getIdentityCard());
            put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
            put("links", new ArrayList<>());
            put("mid", "");
            put("mobile", acc.getMobile());
            put("realname", acc.getRealname());
            put("rebind_area_code", "");
            put("rebind_mobile", "");
            put("rebind_mobile_time", "");
            put("safe_area_code", acc.getSafeMobileArea());
            put("safe_mobile", acc.getSafeMobile());
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/checkReactivateByActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/addRealname
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-passport/passport/bindMobileByThirdpartyBindMobileTicket