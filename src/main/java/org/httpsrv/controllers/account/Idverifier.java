package org.httpsrv.controllers.account;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.IdvBindRealpersonBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Ticket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "account/idverifier/idverifier", produces = "application/json")
public class Idverifier implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/idverifier/idverifier/verifyRealperson">https://gameapi-account.mihoyo.com/account/idverifier/idverifier/verifyRealperson</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - meta_info: Device information<br>
     *      - ticket: Ticket id<br>
     */
    @PostMapping(value = "verifyRealperson")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerifyRealperson(@RequestBody IdvBindRealpersonBody body) {
        if (body == null || body.getTicket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BIND_REAL_PERSON_ERROR_PARAMETERS, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(body.getTicket(), "verify_realperson");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BIND_REAL_PERSON_INVALID_TICKET, "无效token", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("token", myTicket.getId());
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/idverifier/idverifier/bindRealperson">https://gameapi-account.mihoyo.com/account/idverifier/idverifier/bindRealperson</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - meta_info: Device information<br>
     *      - ticket: Ticket id<br>
     */
    @PostMapping(value = "bindRealperson")
    public ResponseEntity<LinkedHashMap<String, Object>> SendBindRealperson(@RequestBody IdvBindRealpersonBody body) {
        if (body == null || body.getTicket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BIND_REAL_PERSON_ERROR_PARAMETERS, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(body.getTicket(), "bind_realperson");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BIND_REAL_PERSON_INVALID_TICKET, "无效token", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("token", myTicket.getId());
        }}));
    }
}

/// TODO Implement: https://gameapi-account.mihoyo.com/account/idverifier/idverifier/confirmRealperson