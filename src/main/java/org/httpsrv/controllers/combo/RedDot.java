package org.httpsrv.controllers.combo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "combo/red_dot", produces = "application/json")
public class RedDot implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os-static.hoyoverse.com/combo/red_dot/list">https://hk4e-sdk-os-static.hoyoverse.com/combo/red_dot/list</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br><br>
     *      - uid: Account id<br>
     *      - region: Region name<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - player_level: Player level<br>
     */
    @PostMapping("list")
    public ResponseEntity<LinkedHashMap<String, Object>> SendRedDotList() {
        ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();

        list.add(new LinkedHashMap<>() {{
            put("red_point_type", 2201);
            put("content_id", 184);
            put("display", true);
        }});

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", list));
    }
}