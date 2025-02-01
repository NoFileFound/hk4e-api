package org.httpsrv.controllers.mdk;

import java.util.*;
import org.httpsrv.data.Retcode;
import org.httpsrv.database.Database;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/tally/tally", "hk4e_cn/mdk/tally/tally", "mdk/tally/tally"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
public class Tally implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/getPageProfile">https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/getPageProfile</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - released_flag: Is the build final release?<br>
     */
    @SuppressWarnings("unused")
    @GetMapping(value = "getPageProfile")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPageProfile(String game, Boolean released_flag) {
        if(game == null || !Utils.checkBizName(game)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_TALLY_INVALID_ARGUMENT, "InvalidArgument", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("game", game);
        data.put("game_icon", "https://sdk.hoyoverse.com/sdk-payment-upload/2020/05/26/4d7606d28f4a687c2d5861124edb98e6_6199229105677087263.png");
        data.put("game_name", (game.equals("hk4e_cn")) ? "原神" : "Genshin Impact");
        data.put("game_name_i18n_key", "game_name_ys");
        data.put("game_site", "https://genshin.hoyoverse.com/en");
        data.put("game_site_i18n_key", "guanwang_ys");
        data.put("game_customer_email", "yskefu@mihoyo.com");
        data.put("game_customer_email_i18n_key", "kefu_ysd");
        data.put("banner", new ArrayList<>(List.of(new LinkedHashMap<>(Map.of(
                "game", "",
                "banner_img", "https://sdk.hoyoverse.com/upload/payment-center/2020/07/27/0ff079b16fe6f9dfbf4eeb6e88a760b6_1904010295323218266.png",
                "banner_link", "https://genshin.hoyoverse.com",
                "banner_link_switch", "Unknown"
        )))));
        data.put("top_up_desc", "");
        data.put("top_up_desc_i18n_key", "payment_info");
        data.put("app_key", "");
        data.put("market_desc", "");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/listPayPlat">https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/listPayPlat</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - currency: Currency<br>
     */
    @GetMapping(value = "listPayPlat")
    public ResponseEntity<LinkedHashMap<String, Object>> SendListPayTypes(String currency) {
        if(currency == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_TALLY_INVALID_ARGUMENT, "InvalidArgument", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("pay_types", Database.findAllPayTypes());
        }}));
    }
}