package org.httpsrv.controllers.combo;

import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.panda.*;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Jackson;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_cn/combo/panda/qrcode", "combo/panda/qrcode"}, method = {RequestMethod.POST}, produces = "application/json")
public class Panda implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/confirm">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/confirm</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     *      - device: Device id<br>
     *      - payload: Payload<br>
     *      + proto: Account<br>
     *      + raw: Raw json ({\"uid\":\"XXXXX\",\"token\":\"XXXXX\"})<br>
     */
    @RequestMapping(value = "confirm")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfirmQRcode(@RequestBody QrCodeConfirmBody body) {
        String actionTicket = body.getTicket();
        Integer appId = body.getApp_id();

        if(actionTicket == null || appId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(appId != ApplicationId.GENSHIN_RELEASE.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Ticket qrTicker = Database.findTicket(actionTicket, "QRLogin_PC");
        if(qrTicker == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "二维码已过期，请重新生成二维码", null));
        }

        if(qrTicker.getState() == 1) {
            qrTicker.setState(2);
            qrTicker.save();
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/scan">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/scan</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     *      - device: Device id<br>
     */
    @RequestMapping(value = "scan")
    public ResponseEntity<LinkedHashMap<String, Object>> SendScanQRCode(@RequestBody QrCodeScanBody body) {
        String actionTicket = body.getTicket();
        Integer appId = body.getApp_id();

        if(actionTicket == null || appId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(appId != ApplicationId.GENSHIN_RELEASE.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Ticket qrTicker = Database.findTicket(body.getTicket(), "QRLogin_PC");
        if(qrTicker == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "二维码已过期，请重新生成二维码", null));
        }

        if(qrTicker.getState() == 0) {
            qrTicker.setState(1);
            qrTicker.setModifiedAt(String.valueOf(System.currentTimeMillis() / 1000));
            qrTicker.save();
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("passport_qr_url", "");
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/fetch">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/fetch</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - device: Device id<br>
     */
    @RequestMapping(value = "fetch")
    public ResponseEntity<LinkedHashMap<String, Object>> SendQRCodeFetch(@RequestBody QrCodeFetchBody body) {
        Integer appId = body.getApp_id();

        if(appId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(appId != ApplicationId.GENSHIN_RELEASE.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Long time = (System.currentTimeMillis() + 60000) / 1000;
        Ticket qrTicket = new Ticket("QRLogin_PC", time);
        qrTicket.save();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("url", String.format("https://user.mihoyo.com/qr_code_in_game.html?app_id=%d&app_name=原神&bbs=true&biz_key=hk4e_cn&expire=%d&ticket=%s", appId, time, qrTicket.getId()));
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/query">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/query</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - device: Device id<br>
     *      - ticket: Ticket id<br>
     */
    @RequestMapping(value = "query")
    public ResponseEntity<LinkedHashMap<String, Object>> SendQueryQRCode(@RequestBody QrCodeQueryBody body, HttpServletRequest request) throws JsonProcessingException {
        String actionTicket = body.getTicket();
        Integer appId = body.getApp_id();

        if(actionTicket == null || appId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        if(appId != ApplicationId.GENSHIN_RELEASE.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                appId != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        Long currentTime = System.currentTimeMillis() / 1000;
        Ticket qrTicket = Database.findTicket(actionTicket, "QRLogin_PC");
        if(qrTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "二维码已过期，请重新生成二维码", null));
        }

        if (currentTime - qrTicket.getTime() > 0) {
            qrTicket.delete();
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "二维码已过期，请重新生成二维码", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if(qrTicket.getState() == 0) {
            data.put("stat", "Init");
            data.put("payload", new LinkedHashMap<>(Map.of(
                    "proto", "Raw",
                    "raw", "",
                    "ext", ""
            )));
        }
        else if(qrTicket.getState() == 1) {
            data.put("stat", "Scanned");
            data.put("payload", new LinkedHashMap<>(Map.of(
                    "proto", "Raw",
                    "raw", "",
                    "ext", ""
            )));
        }
        else if(qrTicket.getState() == 2) {
            Account account = Database.findAccountByDeviceId(body.getDevice());

            data.put("stat", "Confirmed");
            data.put("payload", new LinkedHashMap<>(Map.of(
                    "proto", "Account",
                    "raw", Jackson.toJsonString(new LinkedHashMap<>(Map.of(
                            "uid", account.getId(),
                            "name", account.getName(),
                            "email", account.getEmail(),
                            "mobile", account.getMobile(),
                            "is_email_verify", account.getIsEmailVerified(),
                            "realname", Utils.maskString(account.getRealname()),
                            "identity_card", Utils.maskString(account.getIdentityCard()),
                            "token", account.getSessionKey(),
                            "country", GeoIP.getCountryCode(request.getRemoteAddr())
                    ))),
                    "ext", ""
            )));
        }

        data.put("realname_info", null);
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}