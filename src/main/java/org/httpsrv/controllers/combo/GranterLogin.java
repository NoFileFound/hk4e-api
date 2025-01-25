package org.httpsrv.controllers.combo;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.LoginV2Body;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Jackson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/combo/granter/login", "hk4e_cn/combo/granter/login", "combo/granter/login"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
public class GranterLogin implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/login/v2/login">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/login/v2/login</a><br><br>
     *  Methods: GET, POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - channel: Channel id<br>
     *      - data:<br>
     *          + uid: Account id.<br>
     *          + guest: Is the account guest.<br>
     *          + token: Account token<br>
    *          - device: Account device.<br>
    *          - sign: Signature<br>
     */
    @RequestMapping(value = {"login", "v2/login"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendLoginV2(@RequestBody LoginV2Body body, @RequestHeader(value = "x-rpc-device_id", required = false) String device_id, @RequestHeader(value = "x-rpc-channel_id", required = false) Integer channel_id, HttpServletRequest request) throws JsonProcessingException {
        if(body == null || body.getDevice() == null || body.getData() == null || body.getSign() == null || body.getApp_id() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Signature Error", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter Error", null));
        }

        if(body.getChannel_id() == null || !body.getChannel_id().equals(channel_id) || channel_id > 1) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "Parameter Error", null));
        }

        if(!body.getDevice().equals(device_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
        }

        LoginV2Data bodyData = Jackson.fromJsonString(body.getData(), LoginV2Data.class);
        Account account = Database.findAccountByToken(bodyData.getToken());
        if(account == null) {
            account = Database.findAccountByDeviceId(body.getDevice());
            if(account == null) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_INVALID_ACCOUNT, "Please log in again.", null));
            }
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> data2 = new LinkedHashMap<>();
        data2.put("guest", (account.getAccountType() == 0));
        data2.put("country_code", GeoIP.getCountryCode(request.getRemoteAddr()));
        data2.put("is_new_register", false);

        data.put("combo_id", "0");
        data.put("open_id", account.getId());
        data.put("combo_token", account.getSessionKey());
        data.put("heartbeat", (account.getFatigueRemind() != null));
        data.put("data", Jackson.toJsonString(data2));
        data.put("account_type", account.getAccountType());
        data.put("fatigue_remind", (account.getFatigueRemind() != null) ? Jackson.toJsonString(account.getFatigueRemind()) : null);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/login/beforeVerify">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/login/beforeVerify</a><br><br>
     *  Methods: GET, POST<br>
     *  Parameters:<br>
     *      - account_id: Application id<br>
     *      - combo_token: Session token<br>
     */
    @RequestMapping(value = "beforeVerify")
    public ResponseEntity<LinkedHashMap<String, Object>> SendBeforeVerify() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("is_heartbeat_required", Config.getHttpConfig().requireHeartBeat);
        data.put("is_realname_required", Config.getHttpConfig().requireRealNameVerify);
        data.put("is_guardian_required", Config.getHttpConfig().requireGuardian);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    @Getter
    private static class LoginV2Data {
        private String uid;
        private Boolean guest;
        private String token;
        private String is_new_register; // android
        private String country_code; // android
    }
}

/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/granter/login/webLogin