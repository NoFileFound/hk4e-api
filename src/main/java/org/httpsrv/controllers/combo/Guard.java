package org.httpsrv.controllers.combo;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/combo/guard/api", "hk4e_cn/combo/guard/api", "combo/guard/api"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
public class Guard implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/combo/guard/api/ping">https://hk4e-sdk-os.hoyoverse.com/combo/guard/api/ping</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br>
     */
    @RequestMapping(value = "ping")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPing() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("stop", false);
        data.put("msg", "");
        data.put("interval", 80);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/combo/guard/api/ping2">https://hk4e-sdk-os.hoyoverse.com/combo/guard/api/ping2</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br>
     */
    @RequestMapping(value = "ping2")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPing2() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("interval", 300);
        data.put("banned", false);
        data.put("msg", null);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/guard/api/checkPay