package org.httpsrv.controllers.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.algorithms.BASE64;
import org.httpsrv.algorithms.HEX;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Player;
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
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/binding/api/getUserGameRolesOfRegionByCookieToken">https://api-account-os.hoyoverse.com/account/binding/api/getUserGameRolesOfRegionByCookieToken</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - t: Timestamp<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - region: Game region<br>
     */
    @GetMapping(value = {"getUserGameRolesOfRegionByCookieToken", "getUserGameRolesByCookieToken"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendUserGameRolesOfRegionByCookieToken(@RequestBody String game_biz, @CookieValue(value = "cookie_token_v2") String cookieToken) {
        Account account = Database.findAccountByToken(HEX.bytesToHex(BASE64.decode(cookieToken)));
        if(account == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BBS_NOT_LOGIN, "Login expired. Please log in again.", null));
        }

        Player player = Database.findPlayerByAccountId(account.getId());
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("list", new ArrayList<>(List.of(
                new LinkedHashMap<>() {{
                    put("game_biz", game_biz);
                    put("region", player.getRegionId());
                    put("game_uid", player.getId());
                    put("nickname", player.getPlayerName());
                    put("level", player.getLevel());
                    put("is_chosen", true);
                    put("region_name", player.getRegionName());
                    put("is_official", true);
                }}
        )));
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/binding/api/genAuthKey">https://api-account-os.hoyoverse.com/account/binding/api/genAuthKey</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - stoken (cookies): Stoken<br>
     */
    @PostMapping(value = "genAuthKey")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAuthKey(@RequestBody String game_biz, @CookieValue(value = "stoken") String stoken) {
        Account account = Database.findAccountByStoken(stoken);
        if(account == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("sign_type", 2);
        data.put("authkey_ver", 1);
        data.put("auth_key", BASE64.encode(account.getSessionKey().getBytes()));
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyolab.com/account/binding/api/getAllRegions">https://api-account-os.hoyolab.com/account/binding/api/getAllRegions</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     */
    @GetMapping("getAllRegions")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAllRegions(String game_biz) {
        if(game_biz == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WEBAPI_PARAMETER_ERROR, "Parameter error", null));
        }

        List<LinkedHashMap<String, String>> region_list = new ArrayList<>();
        for(var region : Config.getRegionVar().regions) {
            region_list.add(new LinkedHashMap<>() {{
                put("name", region.Title);
                put("region", region.Name);
            }});
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", region_list);
        }}));
    }
}

/// TODO Implement: https://api-account-os.hoyoverse.com/account/binding/api/getRegionByISO