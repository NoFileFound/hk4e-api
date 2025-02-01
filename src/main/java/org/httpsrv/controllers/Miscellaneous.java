package org.httpsrv.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.ExperimentListBody;
import org.httpsrv.database.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Miscellaneous {

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/ma-open-platform/api/authorizations">https://sg-public-api.hoyoverse.com/ma-open-platform/api/authorizations</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping("ma-open-platform/api/authorizations")
    public ResponseEntity<LinkedHashMap<String, Object>> getAuthorizations() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("code", Retcode.RETCODE_SUCC);
        arguments.put("message", "app running");
        arguments.put("milliTs", String.valueOf(System.currentTimeMillis()));
        arguments.put("data", new LinkedHashMap<>() {{
            put("authorization_list", new ArrayList<>());
        }});

        return ResponseEntity.ok(arguments);
    }

    /**
     *  Source: <a href="https://apm-log-upload.mihoyo.com/_ts">https://apm-log-upload.mihoyo.com/_ts</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
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
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "ping")
    public ResponseEntity<String> SendPing() {
        return ResponseEntity.ok("ok");
    }

    /**
     *  Source: <a href="https://osuspider.yuanshen.com/log">https://osuspider.yuanshen.com/log</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "log")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLog() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("code", Retcode.RETCODE_SUCC);

        return ResponseEntity.ok(arguments);
    }

    /**
     *  Source: <a href="https://abtest-api-data-sg.hoyoverse.com/data_abtest_api/config/experiment/list">https://abtest-api-data-sg.hoyoverse.com/data_abtest_api/config/experiment/list</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - app_sign: Application signature<br>
     *      - uid: Account id<br>
     *      - scene_id: Scenes<br>
     *      - params: Parameters<br>
     */
    @PostMapping(value = "data_abtest_api/config/experiment/list")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDataAbtestAPIConfig(@RequestBody ExperimentListBody body) {
        if(body == null || body.getScene_id() == null) {
            LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
            arguments.put("retcode", Retcode.RETCODE_FAIL);
            arguments.put("success", false);
            arguments.put("message", "参数错误");
            return ResponseEntity.ok(arguments);
        }

        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("retcode", Retcode.RETCODE_SUCC);
        arguments.put("success", true);
        arguments.put("message", "");
        arguments.put("data", Database.findAllExperiments(body.getScene_id()));
        return ResponseEntity.ok(arguments);
    }
}