package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.RiskyCheckBody;
import org.httpsrv.thirdparty.GeetestLib;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/risky/api", produces = "application/json")
public class Risky implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/risky/api/check">https://api-account-os.hoyoverse.com/account/risky/api/check</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br><br>
     *      - action_type: Action type<br>
     *      - api_name: Channel id<br>
     *      - username: Account name<br>
     *      - mobile: Mobile number (mobile login)<br>
     */
    @PostMapping("check")
    public ResponseEntity<LinkedHashMap<String, Object>> SendRiskyCheck(@RequestBody RiskyCheckBody body, @RequestHeader(value = "x-rpc-game_biz", required = false) String game_biz, @RequestHeader(value = "x-rpc-language", required = false) String lang, @RequestHeader(value = "x-rpc-channel_id", required = false) Integer channel_id, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, HttpServletRequest request) {
        if(body.getAction_type() == null || body.getApi_name() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_RISKY_INVALID_REQUEST, "Your request poses a security risk.", null));
        }

        if(body.getMobile() == null && body.getUsername() == null && !body.getAction_type().equals("device_grant") && !body.getAction_type().equals("bind_mobile")) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_RISKY_INVALID_REQUEST, "Your request poses a security risk.", null));
        }

        if(game_biz == null || !Utils.checkBizName(game_biz) || lang == null || !Utils.checkGameLanguage(lang) || channel_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_RISKY_INVALID_REQUEST, "Your request poses a security risk.", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        String id = Random.createRiskyId(device_id);

        if(!Config.getHttpConfig().useEmailCaptcha) {
            data.put("id", id);
            data.put("action", "ACTION_NONE");
            data.put("geetest", null);
        }
        else {
            GeetestLib gtSdk = new GeetestLib(Config.getPropertiesVar().geetestId, Config.getPropertiesVar().geetestPrivateKey, true);

            LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("user_id", body.getUsername());
            paramMap.put("client_type", "native");
            paramMap.put("ip_address", request.getRemoteAddr());
            gtSdk.preProcess(paramMap);

            data.put("id", id);
            data.put("action", "ACTION_GEETEST");
            data.put("geetest", gtSdk.getResponseStr());
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}