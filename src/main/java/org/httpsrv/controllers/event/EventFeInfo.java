package org.httpsrv.controllers.event;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "event/event_fe_info", produces = "application/json")
public class EventFeInfo implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/event/event_fe_info/getTime">https://sg-public-api.hoyoverse.com/event/event_fe_info/getTime</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - lang: Language<br>
     */
    @SuppressWarnings("unused")
    @GetMapping("getTime")
    public ResponseEntity<LinkedHashMap<String, Object>> SendTime(String game_biz, String lang) {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("timestamp", System.currentTimeMillis() / 1000);
        }}));
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/event/event_fe_info/ip_location">https://sg-public-api.hoyoverse.com/event/event_fe_info/ip_location</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - fe_type: Is official<br>
     *      - ip_location_type: chinese or overseas<br>
     *      - ip_config_key: is in europe<br>
     */
    @SuppressWarnings("unused")
    @GetMapping("ip_location")
    public ResponseEntity<LinkedHashMap<String, Object>> SendIpLocation(String fe_type, Integer ip_location_type, String ip_config_key) {
        if(ip_location_type == null) ip_location_type = 0;
        if(ip_config_key == null) ip_config_key = "";

        boolean fe_2266315 = true;
        boolean fe_2299282 = ip_config_key.equals("euro_plus_code");

        if(ip_location_type == 1) {
            fe_2266315 = false;
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("fe_2266315", fe_2266315);
        data.put("data", "");
        data.put("cache_ts", 86400);
        data.put("fe_2299282", fe_2299282);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}