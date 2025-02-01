package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.VerCnGetActionTicketInfoBody;
import org.httpsrv.data.body.account.VerCnVerifyActionTicketPartlyBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"account/ma-cn-verifier/verifier", "account/ma-verifier/api", "hk4e_global/account/ma-verifier/api"}, produces = "application/json", method = RequestMethod.POST)
public class VerifierCN implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/account/ma-verifier/api/getActionTicketInfo">https://sg-public-api.hoyoverse.com/account/ma-verifier/api/getActionTicketInfo</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_type: Action type<br>
     *      - action_ticket: Ticket id<br>
     */
    @RequestMapping("getActionTicketInfo")
    public ResponseEntity<LinkedHashMap<String, Object>> SendActionTicketInfo(@RequestBody VerCnGetActionTicketInfoBody body, HttpServletRequest request) {
        String actionType = body.getAction_type();
        String actionTicket = body.getAction_ticket();

        if(actionType == null || actionTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请求失败，请返回重试", null));
        }

        Account acc = Database.findAccountById(myTicket.getId());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("action_ticket", myTicket.getId());
            put("verify_info", new LinkedHashMap<>() {{
                put("status", myTicket.isVerified() ? "StatusVerified" : "StatusNew");
                put("verify_method_combinations", new ArrayList<>());
                put("chosen_methods", new ArrayList<>(List.of(2)));
                put("partly_verified_methods", new ArrayList<>(List.of(2)));
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
     *  Source: <a href="https://sg-public-api.hoyoverse.com/account/ma-verifier/api/verifyActionTicketPartly">https://sg-public-api.hoyoverse.com/account/ma-verifier/api/verifyActionTicketPartly</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_type: Action type<br>
     *      - action_ticket: Ticket id<br>
     *      - email_captcha: Captcha<br>
     *      - verify_method: Verification method<br>
     */
    @RequestMapping("verifyActionTicketPartly")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyActionTicketPartly(@RequestBody VerCnVerifyActionTicketPartlyBody body) {
        String actionTicket = body.getAction_ticket();
        String actionType = body.getAction_type();
        String captcha = body.getEmail_captcha();

        if(actionTicket == null || actionType == null || captcha == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, actionType);
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().contains(captcha)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_CAPTCHA_MISMATCH, "验证码错误", null));
        }

        myTicket.setVerified(true);
        myTicket.setModifiedAt(String.valueOf(System.currentTimeMillis() / 1000));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyThirdpartyBindMobileCaptcha
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyActionTicket
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-verifier/api/createActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createLoginCaptcha