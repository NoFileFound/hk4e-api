package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.RC4;
import org.httpsrv.database.Database;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.LogBatchBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Log implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("code", retcode);

        return response;
    }

    /**
     *  Source: <a href="https://apm-log-upload-os.hoyoverse.com/apm/dataUpload">https://apm-log-upload-os.hoyoverse.com/apm/dataUpload</a><br><br>
     *  Method: POST<br>
     */
    @PostMapping(value = {"apm/dataUpload", "sdk/dataUpload", "crash/dataUpload", "sophon/dataUpload", "perf/dataUpload", "ys_custom/dataUpload", "event/dataUpload", "adsdk/dataUpload", "crashdump/dataUpload", "loginsdk/dataUpload", "client/event/dataUpload", "asm/dataUpload", "2g/dataUpload"})
    public ResponseEntity<LinkedHashMap<String, Object>> UploadAGameLogsData(@RequestBody List<Object> log, HttpServletRequest request) {
        String logType = request.getRequestURI().substring(1, request.getRequestURI().lastIndexOf("/")).replace("_", "") + "_log";
        if(logType.contains("/")) {
            logType = logType.substring(logType.lastIndexOf("/") + 1);
        }

        if(!Database.logCollection(logType, log.get(0))) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_FAIL, "Serialization error", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }

    /**
     *  Source: <a href="https://minor-api-os.hoyoverse.com/common/h5log/log/batch">https://minor-api-os.hoyoverse.com/common/h5log/log/batch</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - topic: Topic id<br>
     */
    @PostMapping(value = "common/h5log/log/batch")
    public ResponseEntity<LinkedHashMap<String, Object>> UploadH5LogBatchData(@RequestBody LogBatchBody log) {
        String rc4_key = "F#ju0q8I9HbmH8PMpJzzBee&p0b5h@Yb";
        if(!Database.logCollection("h5logs", RC4.decode(log.getData(), rc4_key))) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_FAIL, "Serialization error", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "success", null));
    }
}