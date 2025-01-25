package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "perf/config", produces = "application/json")
public class Perf implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("code", retcode);
        response.put("message", message);

        return response;
    }

    /**
     *  Source: <a href="https://log-upload-os.hoyoverse.com/perf/config/verify">https://log-upload-os.hoyoverse.com/perf/config/verify</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - device_id: Device id<br>
     *      - platform: Platform<br>
     *      - name: Desktop name<br>
     */
    @GetMapping(value = "verify")
    public ResponseEntity<LinkedHashMap<String, Object>> SendVerify() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "ok", null));
    }
}