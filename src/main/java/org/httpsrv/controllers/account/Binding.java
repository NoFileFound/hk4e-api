package org.httpsrv.controllers.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.BndGenAuthKeyBody;
import org.httpsrv.data.body.account.BndGetUserGameRolesByCookieTokenBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.WebProfile;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"account/binding/api"}, produces = "application/json")
public class Binding implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/binding/api/getRegionByISO">https://api-account-os.hoyoverse.com/account/binding/api/getRegionByISO</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Game region id.<br>
     *      - iso: ISO code.<br>
     */
    @GetMapping(value = "getRegionByISO")
    public ResponseEntity<LinkedHashMap<String, Object>> getRegionByISO(String game_biz, String iso) {
        if(game_biz == null || !Utils.checkBizName(game_biz) || iso == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WEBAPI_PARAMETER_ERROR, "参数错误", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RET_WEBAPI_REGION_NOT_FOUND, "未找到对应区服", null));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/binding/api/getUserGameRolesOfRegionByCookieToken">https://api-account-os.hoyoverse.com/account/binding/api/getUserGameRolesOfRegionByCookieToken</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - t: Current timestamp.<br>
     *      - game_biz: Game region id.<br>
     *      - region: Game region.<br>
     */
    @GetMapping(value = {"getUserGameRolesOfRegionByCookieToken", "getUserGameRolesByCookieToken"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendUserGameRolesOfRegionByCookieToken(@RequestBody BndGetUserGameRolesByCookieTokenBody body, @CookieValue(value = "cookie_token_v2") String cookieToken) {
        String game_biz = body.getGame_biz();

        WebProfile account = Database.findWebProfileByCookie(cookieToken);
        if(account == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BBS_NOT_LOGIN, "登录状态失效，请重新登录", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("list", new ArrayList<>(List.of(
                new LinkedHashMap<>() {{
                    put("game_biz", game_biz);
                    put("region", "os_jp69");
                    put("game_uid", account.getId());
                    put("nickname", account.getGameNickname());
                    put("level", account.getGameLevel());
                    put("is_chosen", false);
                    put("region_name", "Localhost");
                    put("is_official", true);
                }}
        )));
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/binding/api/genAuthKey">https://api-account-os.hoyoverse.com/account/binding/api/genAuthKey</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Game region id.<br>
     *      - stoken: Stoken.<br>
     */
    @PostMapping(value = "genAuthKey")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAuthKey(@RequestBody BndGenAuthKeyBody body, @CookieValue(value = "stoken") String stoken) {
        String game_biz = body.getGame_biz();

        Account account = Database.findAccountByStoken(stoken);
        if(account == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("sign_type", 2);
            put("authkey_ver", 1);
            put("auth_key", account.getSessionKey().getBytes());
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyolab.com/account/binding/api/getAllRegions">https://api-account-os.hoyolab.com/account/binding/api/getAllRegions</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Game region id.<br>
     */
    @GetMapping(value = "getAllRegions")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAllRegions(String game_biz) {
        if(game_biz == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WEBAPI_PARAMETER_ERROR, "参数错误", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", Config.getRegionVar().regions.stream().map(region -> new LinkedHashMap<String, String>() {{ put("name", region.Title); put("region", region.Name); }}).toList());
        }}));
    }
}