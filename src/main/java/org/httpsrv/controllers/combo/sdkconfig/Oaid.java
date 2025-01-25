package org.httpsrv.controllers.combo.sdkconfig;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_cn/combo/sdkconfig/oaid/api", "combo/sdkconfig/oaid/api"}, produces = "application/json")
public class Oaid implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sdk-static.mihoyo.com/hk4e_cn/combo/sdkconfig/oaid/api/getConfig">https://sdk-static.mihoyo.com/hk4e_cn/combo/sdkconfig/oaid/api/getConfig</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
     *      - package_name: Package<br>
     */
    @GetMapping("getConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(String package_name) {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_FAIL, "not found the record by param", null));
    }
}