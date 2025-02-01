package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.ScnGetTokenByGameTokenBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/ma-cn-session", produces = "application/json")
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
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - aid: Account id<br>
     *      - game_token: Account session key<br>
     */
    @PostMapping(value = "app/getTokenByGameToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendTokenByGameToken(@RequestBody ScnGetTokenByGameTokenBody body, @RequestHeader(value = "x-rpc-app_id") String app_id, HttpServletRequest request) {
        String aid = body.getAccount_id();
        String gameToken = body.getGame_token();

        if(aid == null || gameToken == null || app_id == null || !Config.getPropertiesVar().appId.contains(app_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        Account acc = Database.findAccountByToken(body.getGame_token());
        if(acc == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_TICKET_NOT_EXIST, "无效token", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("need_realperson", false);
            put("realname_info", null);
            put("token", new LinkedHashMap<String, Object>() {{
                put("token_type", 1);
                put("token", body.getGame_token());
            }});
            put("user_info", new LinkedHashMap<String, Object>() {{
                put("aid", acc.getId());
                put("mid", "12ya9usebi_hy");
                put("account_name", acc.getName());
                put("email", Utils.maskString(acc.getEmail()));
                put("is_email_verify", acc.getIsEmailVerified() ? 1 : 0);
                put("area_code", Utils.maskString(acc.getMobileArea()));
                put("mobile", Utils.maskString(acc.getMobile()));
                put("safe_area_code", Utils.maskString(acc.getSafeMobileArea()));
                put("safe_mobile", Utils.maskString(acc.getSafeMobile()));
                put("realname", Utils.maskString(acc.getRealname()));
                put("identity_code", Utils.maskString(acc.getIdentityCard()));
                put("rebind_area_code", "");
                put("rebind_mobile", "");
                put("rebind_mobile_time", "0");
                put("links", new ArrayList<>());
                put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("password_time", "0");
                put("unmasked_email", "");
                put("unmasked_email_type", 0);
            }});
        }}));
    }

    /**
     *  Source: <a href="https://passport-api.mihoyo.com/account/ma-cn-session/app/logout">https://passport-api.mihoyo.com/account/ma-cn-session/app/logout</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - aid: Account id<br>
     *      - token: Account session key<br>
     *      - token_type: token version<br>
     */
    @PostMapping(value = {"app/logout", "web/crossLogout"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogout() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }
}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/createCrossLoginTokenByGameToken
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/exchange
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/web/crossLoginStart
/// TODO Implement: https://passport-api-v4.mihoyo.com/account/ma-cn-session/web/verifyLtoken
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/getTokenBySToken
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/web/webVerifyForGame
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/getSTokenByGameToken
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-session/app/verify