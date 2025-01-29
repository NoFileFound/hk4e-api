package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.ActionTicketInfoBody;
import org.httpsrv.data.body.VerifyActionTicketPartlyBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"account/ma-cn-verifier/verifier", "account/ma-verifier/api", "hk4e_global/account/ma-verifier/api"}, produces = "application/json", method = RequestMethod.POST)
public class Verifier implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/account/ma-verifier/api/getActionTicketInfo">https://sg-public-api.hoyoverse.com/account/ma-verifier/api/getActionTicketInfo</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br><br>
     *      - action_type: Action type<br>
     *      - action_ticket: Ticket id<br>
     */
    @RequestMapping("getActionTicketInfo")
    public ResponseEntity<LinkedHashMap<String, Object>> SendActionTicketInfo(@RequestBody ActionTicketInfoBody body, HttpServletRequest request) {
        if(body == null || body.getAction_ticket() == null || body.getAction_type() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_INVALID_PARAMETER, "Parameter error", null));
        }

        Ticket myTicket = Database.findTicket(body.getAction_ticket());
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请求失败，请返回重试", null));
        }

        Account account;
        if(myTicket.getMobile() == null || myTicket.getMobile().isEmpty()) {
            account = Database.findAccountByEmail(myTicket.getEmail());
        }
        else {
            account = Database.findAccountByMobile(myTicket.getMobile());
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("action_ticket", myTicket.getId());
        data.put("verify_info", new LinkedHashMap<>() {{
            put("status", myTicket.isVerified() ? "StatusVerified" : "StatusNew");
            put("verify_method_combinations", new ArrayList<>());
            put("chosen_methods", new ArrayList<>(List.of(2)));
            put("partly_verified_methods", new ArrayList<>(List.of(2)));
        }});
        data.put("user_info", new LinkedHashMap<>() {{
            data.put("aid", account.getId());
            data.put("mid", "12ya9usebi_hy");
            data.put("account_name", account.getName());
            data.put("email", Utils.maskString(account.getEmail()));
            data.put("is_email_verify", (account.getIsEmailVerified()) ? 1 : 0);
            data.put("area_code", Utils.maskString(account.getMobileArea()));
            data.put("mobile", Utils.maskString(account.getMobile()));
            data.put("safe_area_code", Utils.maskString(account.getSafeMobileArea()));
            data.put("safe_mobile", Utils.maskString(account.getSafeMobile()));
            data.put("realname", Utils.maskString(account.getRealname()));
            data.put("identity_code", Utils.maskString(account.getIdentityCard()));
            data.put("rebind_area_code", "");
            data.put("rebind_mobile", "");
            data.put("rebind_mobile_time", "");
            data.put("links", new ArrayList<>());
            data.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
            data.put("password_time", String.valueOf((System.currentTimeMillis() / 1000)));
            data.put("unmasked_email", "");
            data.put("unmasked_email_type", 0);
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/account/ma-verifier/api/verifyActionTicketPartly">https://sg-public-api.hoyoverse.com/account/ma-verifier/api/verifyActionTicketPartly</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br><br>
     *      - action_type: Action type<br>
     *      - action_ticket: Ticket id<br>
     *      - email_captcha: Captcha<br>
     *      - verify_method: Verification method<br>
     */
    @RequestMapping("verifyActionTicketPartly")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyActionTicketPartly(@RequestBody VerifyActionTicketPartlyBody body) {
        if(body == null || body.getAction_ticket() == null || body.getEmail_captcha() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_INVALID_PARAMETER, "Parameter error", null));
        }

        Ticket myTicket = Database.findTicket(body.getAction_ticket());
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().contains(body.getAction_ticket())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "The verification code is invalid", null));
        }
        myTicket.setVerified(true);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyThirdpartyBindMobileCaptcha
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileCaptcha
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyActionTicket
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-verifier/api/createActionTicket