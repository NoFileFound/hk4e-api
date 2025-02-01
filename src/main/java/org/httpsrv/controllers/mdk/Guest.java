package org.httpsrv.controllers.mdk;

import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.guest.GstGuestDeleteBody;
import org.httpsrv.data.body.guest.GstGuestLoginBody;
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
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - device: device id<br>
     *      - sign: signature<br>
     *      - guest_id: Guest id<br>
     */
    @PostMapping(value = "delete")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDelete(@RequestBody GstGuestDeleteBody body) {
        if(!Config.getHttpConfig().enableGuestLogin) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_CANCEL, "客人已禁用", null));
        }
		
        if(body == null || body.getGuest_id() == null || body.getSign() == null || body.getDevice_id() == null || body.getGame_key() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(!isDebug()) {
            // TODO: Implement HMAC verification
        }

        Account account = Database.findGuestAccount(body.getDevice_id());
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }
        account.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/v2/login">https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/v2/login</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_key: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - client: Platform<br>
     *      - device: device id<br>
     *      - sign: signature<br>
     *      - g_version: genshin version<br>
     */
    @PostMapping(value = {"login", "v2/login"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendLogin(@RequestBody GstGuestLoginBody body, HttpServletRequest request) {
        if(!Config.getHttpConfig().enableGuestLogin) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_CANCEL, "客人已禁用", null));
        }

        String device = body.getDevice();
        if(device == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(!isDebug()) {
            // TODO: Implement HMAC verification
        }

        Account account = Database.findGuestAccount(device);
        boolean newly = false;
        if(account == null) {
            newly = true;
            account = new Account(true);
            account.setCurrentIP(request.getRemoteAddr());
            account.setCurrentDeviceId(device);
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