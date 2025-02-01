package org.httpsrv.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"plutus/api/v2", "plutus/api"}, produces = "application/json")
public class Plutus implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://api-payment.mihoyo.com/plutus/api/v2/listPayPlat">https://api-payment.mihoyo.com/plutus/api/v2/listPayPlat</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "listPayPlat")
    public ResponseEntity<LinkedHashMap<String, Object>> SendListPayPlatforms() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("pay_plats", new ArrayList<>());
        }}));
    }

    /**
     *  Source: <a href="https://api-payment.mihoyo.com/plutus/api/v2/timeNow">https://api-payment.mihoyo.com/plutus/api/v2/timeNow</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "timeNow")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPlutusTimeMillis() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("now", String.valueOf(System.currentTimeMillis() / 1000));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://api-payment.mihoyo.com/plutus/api/v2/check || https://api-payment.mihoyo.com/plutus/api/check
/// TODO Implement: https://api-payment.mihoyo.com/plutus/api/deduct