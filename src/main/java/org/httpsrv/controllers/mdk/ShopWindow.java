package org.httpsrv.controllers.mdk;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.httpsrv.database.Database;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/shopwindow/shopwindow", "hk4e_cn/mdk/shopwindow/shopwindow", "/mdk/shopwindow/shopwindow"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
public class ShopWindow implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/listPriceTier">https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/listPriceTier</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - currency: Currency<br>
     */
    @RequestMapping(value = {"listPriceTier", "listPriceTierV2"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendPriceTierList(String game_biz, String currency) {
        if(game_biz == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SHOPWINDOW_PARAMETER_MISSING, "game biz missing", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("suggest_currency", (currency == null) ? "USD" : currency);
        data.put("tiers", Database.findAllPaymentTiers());
        data.put("price_tier_version", "0");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/getCurrencyAndCountryByIp">https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/getCurrencyAndCountryByIp</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     */
    @RequestMapping(value = "getCurrencyAndCountryByIp")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCurrencyAndCountryByIp(String game_biz, HttpServletRequest request) {
        if(game_biz == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SHOPWINDOW_PARAMETER_MISSING, "game biz missing", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("currency", "USD");
        data.put("country", GeoIP.getCountryCode(request.getRemoteAddr()));
        data.put("price_tier_version", String.valueOf(System.currentTimeMillis() / 1000));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/fetchGoods">https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/fetchGoods</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - released_flag: Is the build final release?<br>
     *      - game: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - region: Region id<br>
     *      - uid: User id<br>
     *      - account: Account id<br>
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "fetchGoods")
    public ResponseEntity<LinkedHashMap<String, Object>> SendFetchGoods(Boolean released_flag, String game, String region, String uid, String account) {
        if(game == null || region == null || account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SHOPWINDOW_PARAMETER_MISSING, "game or region or account is empty", null));
        }

        var acc = Database.findAccountById(account);
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("goods_list", (acc.getGoods() != null) ? acc : new ArrayList<>());
        }}));
    }
}