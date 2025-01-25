package org.httpsrv.controllers.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.GetTokenByGameTokenBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/ma-cn-session/app", produces = "application/json")
public class SessionCN implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-session/app/getTokenByGameToken">https://passport-api.mihoyo.com/account/ma-cn-session/app/getTokenByGameToken</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - aid: Account id<br>
     *      - game_token: Account session key<br>
     */
    @PostMapping(value = {"getTokenByGameToken", "getTokenBySToken"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendTokenByGameToken(@RequestBody GetTokenByGameTokenBody body, @RequestHeader(value = "x-rpc-app_id", required = false) String app_id) {
        if(body == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "System error", null));
        }

        if(app_id == null || !Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Account account = Database.findAccountByToken(body.getGame_token());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "token not found", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("token", new LinkedHashMap<String, Object>() {{
            put("token_type", 1);
            put("token", body.getGame_token());
        }});
        data.put("user_info", new LinkedHashMap<String, Object>() {{
            put("aid", account.getId());
            put("mid", ""); /// ?
            put("account_name", "");
            put("email", account.getEmail());
            put("is_email_verify", account.getIsEmailVerified());
            put("area_code", "+86");
            put("mobile", account.getMobile());
            put("safe_area_code", "");
            put("safe_area_name", "");
            put("realname", account.getRealname());
            put("identity_code", ""); /// ?
            put("rebind_area_code", "");
            put("rebind_mobile", "");
            put("rebind_mobile_time", "0");
            put("links", new ArrayList<>());
        }});
        data.put("realname_info", null);
        data.put("need_realperson", false);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-session/app/logout">https://passport-api.mihoyo.com/account/ma-cn-session/app/logout</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - aid: Account id<br>
     *      - token: Account session key<br>
     *      - token_type: token version<br>
     */
    @PostMapping(value = "logout")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogout() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/createCrossLoginTokenByGameToken