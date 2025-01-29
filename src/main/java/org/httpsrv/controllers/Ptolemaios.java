package org.httpsrv.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "ptolemaios_api/api", produces = "application/json")
public class Ptolemaios implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/ptolemaios_api/api/reportStrategyData">https://sg-public-api.hoyoverse.com/ptolemaios_api/api/reportStrategyData</a><br><br>
     *  Method: POST<br><br>
     */
    @GetMapping("reportStrategyData")
    public ResponseEntity<LinkedHashMap<String, Object>> SendReportStrategyData() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/ptolemaios_api/api/getLatestRelease">https://sg-public-api.hoyoverse.com/ptolemaios_api/api/getLatestRelease</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - channel: Channel name<br>
     *      - app_version: Application version<br>
     *      - key: Application key<br>
     */
    @GetMapping("getLatestRelease")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLatestRelease() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("strategy_id", 0);
        data.put("update_type", 0);
        data.put("dialog_num", 0);
        data.put("dialog_period_type", 0);
        data.put("dialog_period", 0);
        data.put("dialog_title", "");
        data.put("dialog_content", "");
        data.put("package_name", "");
        data.put("package_url", "");
        data.put("package_size", "0");
        data.put("has_update", false);
        data.put("package_version", "");
        data.put("package_md5", "");
        data.put("user_defined", "");
        data.put("update_recommended_intensity", "");
        data.put("dialog_content_en", "");
        data.put("dialog_title_en", "");
        data.put("dialog", "");
        data.put("hyp_game_ids", new ArrayList<>());
        data.put("silent", "");
        data.put("loop_way", "");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}