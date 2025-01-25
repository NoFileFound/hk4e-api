package org.httpsrv.controllers.account;

import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.GrantBody;
import org.httpsrv.data.body.PreGrantByTicketBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.JakartaMail;
import org.httpsrv.thirdparty.TwilioApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/device/api", method = {RequestMethod.POST}, produces = "application/json")
public class AccountDevice implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/grant">https://api-account-os.hoyoverse.com/account/device/api/grant</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - code: Verification code<br>
     */
    @RequestMapping(value = "grant")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGrant(@RequestBody GrantBody body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body == null || body.getCode() == null || body.getTicket() == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "请前往官网/商店下载最新版本", null));
        }

        Ticket info = Database.findTicket(body.getTicket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        if(!info.getCode().equals(body.getCode())) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_GRANT_INVALID_CODE, "The code you provided is invalid.", null));
        }

        Account account;
        if(!info.getMobile().isEmpty()) {
            account = Database.findAccountByMobile(info.getMobile());
        }
        else {
            account = Database.findAccountByEmail(info.getEmail());
            account.setIsEmailVerified(true);
        }

        String newToken = Random.generateStr(15);
        account.setRequireDeviceGrant(false);
        account.setSessionKey(newToken);
        account.setDeviceId(device_id);
        account.setCurrentIP(request.getRemoteAddr());
        account.save();

        Database.deleteTicketById(body.getTicket());

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("login_ticket", "");
        data.put("game_token", newToken);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/preGrantByTicket">https://api-account-os.hoyoverse.com/account/device/api/preGrantByTicket</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id<br>
     *      - device: Information about client's device.<br>
     *      - way: Device grant type (by email or mobile)<br>
     */
    @RequestMapping(value = "preGrantByTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPreGrantByTicket(@RequestBody PreGrantByTicketBody body, @RequestHeader(value = "x-rpc-risky", required = false) String risky_type, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id) {
        if(body == null || body.getAction_ticket() == null || body.getAction_ticket().isEmpty() || device_id == null || body.getWay() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "请前往官网/商店下载最新版本", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=")+3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "The current network environment is at risk.", null));
                }
            }
        } catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        Ticket info = Database.findTicket(body.getAction_ticket());
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }

        if(body.getWay().equals("Way_Email")) {
            if(info.getEmail() == null) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
            }

            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            String code = Random.generateCode();
            JakartaMail.sendMessage(info.getEmail(), "Device verification", "Your activation code is " + code);

            info.setCode(code);
            info.save();
            data.put("ticket", body.getAction_ticket());

            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
        }
        else if(body.getWay().equals("Way_BindMobile")) {
            if(info.getMobile() == null) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
            }

            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            String code = Random.generateCode();
            TwilioApi.sendSms(info.getMobile(), "Device verification. Your activation code is " + code);

            info.setCode(code);
            info.save();
            data.put("ticket", body.getAction_ticket());

            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
        }
        else {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "Internal Server Error", null));
        }
    }
}

/// TODO: Implement: https://api-account-os.hoyoverse.com/account/device/api/listNewerDevices
/// TODO: Implement: https://api-account-os.hoyoverse.com/account/device/api/ackNewerDevices
/// TODO: Implement: https://api-os-takumi.hoyoverse.com/account/device/api/preGrantByGame