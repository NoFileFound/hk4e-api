package org.httpsrv.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.GetFpBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "device-fp/api", produces = "application/json")
public class Device implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-data-api.hoyoverse.com/device-fp/api/getExtList">https://sg-public-data-api.hoyoverse.com/device-fp/api/getExtList</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - platform: Platform<br>
     *      - app_name: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     */
    @GetMapping(value = "getExtList")
    public ResponseEntity<LinkedHashMap<String, Object>> SendExtensionList(Integer platform) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(platform == null || platform <= 0 || platform > 13) {
            data.put("code", 403);
            data.put("message", (platform == null) ? "传入的参数有误" : "不支持的platform");
            data.put("ext_list", new ArrayList<>());
            data.put("pkg_list", new ArrayList<>());
            data.put("pkg_str", "");
        }
        else {
            data.put("code", 200);
            data.put("msg", "ok");
            data.put("ext_list", Config.getHttpConfig().extensionList);
            if(platform == 1 || platform == 2) {
                data.put("pkg_list", Config.getHttpConfig().mobilePackages);
            }
            else {
                data.put("pkg_list", new ArrayList<>());
            }

            data.put("pkg_str", ""); // /vK5WTh5SS3SAj8Zm0qPWg==
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sg-public-data-api.hoyoverse.com/device-fp/api/getFp">https://sg-public-data-api.hoyoverse.com/device-fp/api/getFp</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - device_id: device id<br>
     *      - seed_id: Seed id<br>
     *      - seed_time: Seed time<br>
     *      - platform: Platform<br>
     *      - device_fp: Device fingerprint<br>
     *      - app_name: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - ext_fields: Extension fields<br>
     *
     */
    @PostMapping(value = "getFp")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDeviceFingerprint(@RequestBody GetFpBody bodyData) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("code", 200);
        data.put("msg", "ok");
        data.put("device_fp", (bodyData.getDevice_fp() == null) ? "" : bodyData.getDevice_fp());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}