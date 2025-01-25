package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.utils.Jackson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "sdk_global/apphub/api", produces = "application/json")
public class Apphub implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sdk-common-static.hoyoverse.com/sdk_global/apphub/api/getAttributionReportConfig">https://sdk-common-static.hoyoverse.com/sdk_global/apphub/api/getAttributionReportConfig</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - client_type: Platform<br>
     */
    @SuppressWarnings("unused")
    @GetMapping("getAttributionReportConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAttributionReportConfig(String app_id, Integer client_type) throws JsonProcessingException {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(app_id != null && Config.getPropertiesVar().appId.contains(app_id)) {
            data.put("device_blacklist", "");
            data.put("enabled", Config.getHttpConfig().enableAttributionReport);
            data.put("report_detail", Jackson.toJsonString(new LinkedHashMap<String, Object>() {{put("allowed_fields", Config.getHttpConfig().attributionReportDetails);}}));
            data.put("report_interval_seconds", Config.getHttpConfig().attributionReportInterval);
        }
        else {
            data.put("device_blacklist", "");
            data.put("enabled", false);
            data.put("report_detail", "");
            data.put("report_interval_seconds", 0);
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://sdk-common-static.hoyoverse.com/sdk_global/apphub/upload/uploadAsa
/// TODO Implement: https://sdk-common-static.hoyoverse.com/sdk_global/apphub/api/getCaid