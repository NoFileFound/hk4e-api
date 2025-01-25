package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Miscellaneous {
    /**
     *  Source: <a href="https://apm-log-upload.mihoyo.com/_ts">https://apm-log-upload.mihoyo.com/_ts</a><br><br>
     *  Method: GET<br>
     */
    @GetMapping(value = "_ts", produces = "application/json")
    public ResponseEntity<LinkedHashMap<String, Object>> SendServerTimeMillis() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("code", Retcode.RETCODE_SUCC);
        arguments.put("message", "app running");
        arguments.put("milliTs", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok(arguments);
    }

    /**
     *  Source: <a href="https://apm-log-upload.mihoyo.com/ping">https://apm-log-upload.mihoyo.com/ping</a><br><br>
     *  Method: GET<br>
     */
    @GetMapping(value = "ping")
    public ResponseEntity<String> SendPing() {
        return ResponseEntity.ok("ok");
    }

    /**
     *  Source: <a href="https://osuspider.yuanshen.com/log">https://osuspider.yuanshen.com/log</a><br><br>
     *  Method: POST<br>
     */
    @PostMapping(value = "log")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLog() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("code", Retcode.RETCODE_SUCC);

        return ResponseEntity.ok(arguments);
    }

    /**
     *  Source: <a href="https://abtest-api-data-sg.hoyoverse.com/data_abtest_api/config/experiment/list">https://abtest-api-data-sg.hoyoverse.com/data_abtest_api/config/experiment/list</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - app_sign: Application signature<br>
     *      - uid: Account id<br>
     *      - scene_id: Scenes<br>
     *      - params: Parameters<br>
     */
    @PostMapping(value = "data_abtest_api/config/experiment/list")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDataAbtestAPIConfig() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("retcode", Retcode.RETCODE_SUCC);
        arguments.put("success", true);
        arguments.put("message", "");
        arguments.put("data", new LinkedHashMap<>());

        return ResponseEntity.ok(arguments);
    }
}