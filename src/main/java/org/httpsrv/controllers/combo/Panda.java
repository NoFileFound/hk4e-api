package org.httpsrv.controllers.combo;

import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.QrCodeConfirmBody;
import org.httpsrv.data.body.QrCodeFetchBody;
import org.httpsrv.data.body.QrCodeQueryBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.utils.Jackson;
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
     *  Parameters:<br><br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     *      - device: Device id<br>
     *      - payload: Payload<br>
     *      + proto: Account<br>
     *      + raw: Raw json ({\"uid\":\"XXXXX\",\"token\":\"XXXXX\"})<br>
     */
    @RequestMapping(value = "confirm")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfirmQRcode(@RequestBody QrCodeConfirmBody body) {
        if(body == null || body.getTicket() == null || body.getApp_id() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        Ticket qrTicker = Database.findTicket(body.getTicket());
        if(qrTicker == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "The QR code has expired, please regenerate the QR code", null));
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
     *  Parameters:<br><br>
     *      - app_id: Application id<br>
     *      - ticket: Ticket id<br>
     *      - device: Device id<br>
     */
    @RequestMapping(value = "scan")
    public ResponseEntity<LinkedHashMap<String, Object>> SendScanQRCode(@RequestBody QrCodeQueryBody body) {
        if(body == null || body.getTicket() == null || body.getApp_id() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        Ticket qrTicker = Database.findTicket(body.getTicket());
        if(qrTicker == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "The QR code has expired, please regenerate the QR code", null));
        }

        if(qrTicker.getState() == 0) {
            qrTicker.setState(1);
            qrTicker.save();
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("passport_qr_url", "");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/fetch">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/fetch</a><br><br>
     *  Method: POST<br>
     *  Parameters:<br><br>
     *      - app_id: Application id<br>
     *      - device: Device id<br>
     */
    @RequestMapping(value = "fetch")
    public ResponseEntity<LinkedHashMap<String, Object>> SendQRCodeFetch(@RequestBody QrCodeFetchBody body) {
        if(body == null || body.getApp_id() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        Long time = (System.currentTimeMillis() + 60000) / 1000;
        Ticket qrTicket = new Ticket("QRLogin", time);
        qrTicket.save();

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("url", String.format("https://user.mihoyo.com/qr_code_in_game.html?app_id=%d&app_name=原神&bbs=true&biz_key=hk4e_cn&expire=%d&ticket=%s", body.getApp_id(), time, qrTicket.getId()));

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/query">https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/query</a><br><br>
     *  Method: POST<br>
     *  Parameters:<br><br>
     *      - app_id: Application id<br>
     *      - device: Device id<br>
     *      - ticket: Ticket id<br>
     */
    @RequestMapping(value = "query")
    public ResponseEntity<LinkedHashMap<String, Object>> SendQueryQRCode(@RequestBody QrCodeQueryBody body) throws JsonProcessingException {
        if(body == null || body.getApp_id() == null || body.getTicket() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_APP_ID_ERROR, "System request failed, please return and try again", null));
        }

        Long currentTime = System.currentTimeMillis() / 1000;
        Ticket qrTicket = Database.findTicket(body.getTicket());
        if(qrTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "The QR code has expired, please regenerate the QR code", null));
        }

        if (currentTime - qrTicket.getTime() > 0) {
            qrTicket.delete();
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_QRCODE_EXPIRED, "The QR code has expired, please regenerate the QR code", null));
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
			if(account == null) {
				return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "The current network environment is at risk.", null));
			}

            data.put("stat", "Confirmed");
            data.put("payload", new LinkedHashMap<>(Map.of(
                    "proto", "Account",
                    "raw", Jackson.toJsonString(new LinkedHashMap<>(Map.of(
                            "uid", account.getId(),
                            "token", account.getSessionKey()
                    ))),
                    "ext", ""
            )));
        }

        data.put("realname_info", null);
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}