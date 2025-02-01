package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.RiskyCheckBody;
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
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_type: Action type<br>
     *      - api_name: Channel id<br>
     *      - username: Account name<br>
     *      - mobile: Mobile number (mobile login)<br>
     */
    @PostMapping("check")
    public ResponseEntity<LinkedHashMap<String, Object>> SendRiskyCheck(@RequestBody RiskyCheckBody body, @RequestHeader(value = "x-rpc-game_biz") String game_biz, @RequestHeader(value = "x-rpc-language") String lang, @RequestHeader(value = "x-rpc-channel_id") Integer channel_id, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String actionType = body.getAction_type();
        String apiName = body.getApi_name();

        if(actionType == null || apiName == null || game_biz == null || !Utils.checkBizName(game_biz) || lang == null || !Utils.checkGameLanguage(lang) || channel_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_RISKY_INVALID_REQUEST, "你的请求存在安全风险", null));
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