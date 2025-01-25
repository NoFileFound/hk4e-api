package org.httpsrv.controllers.mdk;

import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.GuestDeleteBody;
import org.httpsrv.data.body.GuestLoginBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/guest/guest","hk4e_cn/mdk/guest/guest","mdk/guest/guest"}, produces = "application/json")
public class Guest implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/delete">https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/delete</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - device: device id<br>
     *      - sign: signature<br>
     *      - guest_id: Guest id<br>
     */
    @PostMapping(value = "delete")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDelete(@RequestBody GuestDeleteBody body) {
        if(!Config.getHttpConfig().enableGuestLogin) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_CANCEL, "Guests are disabled.", null));
        }
		
        if(body == null || body.getGuest_id() == null || body.getSign() == null || body.getDevice_id() == null || body.getGame_key() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter Error", null));
        }

        Account account = Database.findGuestAccount(body.getDevice_id());
        account.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/v2/login">https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/v2/login</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     *      - device: device id<br>
     *      - sign: signature<br>
     *      - g_version: genshin version<br>
     */
    @PostMapping(value = {"login", "v2/login"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogin(@RequestBody GuestLoginBody body, HttpServletRequest request) {
        if(!Config.getHttpConfig().enableGuestLogin) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_CANCEL, "Guests are disabled.", null));
        }

        if(body == null || body.getDevice() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter Error", null));
        }

        Account account = Database.findGuestAccount(body.getDevice());
        boolean newly = false;
        if(account == null) {
            newly = true;
            account = new Account(true);
            account.setCurrentIP(request.getRemoteAddr());
            account.setDeviceId(body.getDevice());
            account.setSessionKey(Random.generateStr(30));
            account.save();
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("guest_id", account.getId());
        data.put("newly", newly);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/guest/guest/bind