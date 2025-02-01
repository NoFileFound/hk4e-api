package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import org.httpsrv.algorithms.Random;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.DvcAckNewerDevicesBody;
import org.httpsrv.data.body.account.DvcDeleteByCookieTokenBody;
import org.httpsrv.data.body.account.DvcGrantBody;
import org.httpsrv.data.body.account.DvcListNewerDevicesBody;
import org.httpsrv.data.body.account.DvcPreGrantByTicketBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.database.entity.WebProfile;
import org.httpsrv.thirdparty.JakartaMail;
import org.httpsrv.thirdparty.TwilioApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"account/device/api", "device/api"}, produces = "application/json")
public class Device implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/device/api/deleteByCookieToken">https://sg-public-api.hoyoverse.com/device/api/deleteByCookieToken</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - user_device_id: Device id.<br>
     */
    @PostMapping(value = "deleteByCookieToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendDeleteByCookieToken(@RequestBody DvcDeleteByCookieTokenBody body, @CookieValue(value = "cookie_token_v2") String cookieToken) {
        if(cookieToken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        WebProfile profile = Database.findWebProfileByCookie(cookieToken);
        if(profile == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        try {
            profile.getDevices().remove(body.getUser_device_id());
            profile.save();
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
        }catch (Exception ignored) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_SYSTEM_ERROR, "系统错误", null));
        }
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/device/api/listByCookieToken">https://sg-public-api.hoyoverse.com/device/api/listByCookieToken</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "listByCookieToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendListByCookieToken(@CookieValue(value = "cookie_token_v2") String cookieToken) {
        if(cookieToken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        WebProfile profile = Database.findWebProfileByCookie(cookieToken);
        if(profile == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MA_PASSPORT_ILLEGAL_PARAMETER, "参数不合法", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("devices", profile.getDevices());
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/ackNewerDevices">https://api-account-os.hoyoverse.com/account/device/api/ackNewerDevices</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account_id: Account id.<br>
     *      - game_token: Session token.<br>
     *      - latest_id: UNKNOWN.<br>
     */
    @PostMapping(value = "ackNewerDevices")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAckNewerDevices(@RequestBody DvcAckNewerDevicesBody body) {
        String account_id = body.getAccount_id();
        String game_token = body.getGame_token();
        Integer latest_id = body.getLatest_id();

        if(account_id == null || game_token == null || latest_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WRONG_ACCOUNT, "账号错误", null));
        }

        Account account;
        if(isDebug()) {
            account = Database.findAccountById(account_id);
        }
        else {
            account = Database.findAccountByToken(game_token);
        }

        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WRONG_ACCOUNT, "账号错误", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/listNewerDevices">https://api-account-os.hoyoverse.com/account/device/api/listNewerDevices</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account_id: Account id.<br>
     *      - game_token: Session token.<br>
     */
    @PostMapping(value = "listNewerDevices")
    public ResponseEntity<LinkedHashMap<String, Object>> SendListNewerDevices(@RequestBody DvcListNewerDevicesBody body) {
        String account_id = body.getAccount_id();
        String game_token = body.getGame_token();

        if(account_id == null || game_token == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WRONG_ACCOUNT, "账号错误", null));
        }

        Account account;
        if(isDebug()) {
            account = Database.findAccountById(account_id);
        }
        else {
            account = Database.findAccountByToken(game_token);
        }

        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_WRONG_ACCOUNT, "账号错误", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("devices", account.getDeviceIds());
            put("latest_id", account.getCurrentDeviceId());
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/grant">https://api-account-os.hoyoverse.com/account/device/api/grant</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     *      - code: Verification code.<br>
     */
    @PostMapping(value = "grant")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGrant(@RequestBody DvcGrantBody body, @RequestHeader(value = "x-rpc-device_id") String device_id, HttpServletRequest request) {
        String verCode = body.getCode();
        String actionTicket = body.getTicket();

        if(verCode == null || actionTicket == null || device_id == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "请前往官网/商店下载最新版本", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "device_grant");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "请前往官网/商店下载最新版本", null));
        }

        if(!myTicket.getCode().equals(verCode)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "验证码错误", null));
        }

        Account account = Database.findAccountById(myTicket.getAccountId());

        String newToken = Random.generateStr(15);
        account.setSessionKey(newToken);
        account.setCurrentDeviceId(device_id);
        account.setCurrentIP(request.getRemoteAddr());
        account.setRequireDeviceGrant(false);

        if(!account.getDeviceIds().contains(device_id)) {
            account.getDeviceIds().add(device_id);
        }

        account.save();
        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("login_ticket", "");
            put("game_token", newToken);
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/device/api/preGrantByTicket">https://api-account-os.hoyoverse.com/account/device/api/preGrantByTicket</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     *      - device: Device information.<br>
     *      - way: Way to send the verification code.<br>
     */
    @PostMapping(value = "preGrantByTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPreGrantByTicket(@RequestBody DvcPreGrantByTicketBody body, @RequestHeader(value = "x-rpc-risky") String risky_type, @RequestHeader(value = "x-rpc-device_id") String device_id) {
        String actionTicket = body.getAction_ticket();
        String way = body.getWay();

        if(actionTicket == null || device_id == null || way == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "请前往官网/商店下载最新版本", null));
        }

        try {
            if(!Config.getHttpConfig().useEmailCaptcha) {
                risky_type = risky_type.substring(risky_type.indexOf("id=") + 3);
                if(risky_type.contains(";")) {
                    risky_type = risky_type.substring(0, risky_type.indexOf(";"));
                }

                if(!Random.compareRiskyId(device_id, risky_type)) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统请求失败，请返回重试", null));
                }
            }
        }catch(Exception ignore) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统请求失败，请返回重试", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "device_grant");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统请求失败，请返回重试", null));
        }

        boolean success = false;
        switch(way) {
            case "Way_Email": {
                Account account = Database.findAccountById(myTicket.getAccountId());
                if(account.getEmail() == null) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
                }

                String code = Random.generateCode();
                JakartaMail.sendMessage(account.getEmail(), "Verification code", String.format("Your verification code is %s", code));

                myTicket.setCode(code);
                myTicket.save();

                success = true;
                break;

            }
            case "Way_BindMobile":
            case "Way_SafeMobile": {
                Account account = Database.findAccountById(myTicket.getAccountId());
                if(account.getMobile() == null) {
                    return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_NETWORK_AT_RISK, "请求失败，当前网络环境存在风险", null));
                }

                String code = Random.generateCode();
                TwilioApi.sendSms(account.getMobile(), String.format("Verification code\nYour verification code is %s", code));

                myTicket.setCode(code);
                myTicket.save();

                success = true;
                break;
            }
        }

        if(success) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
                put("ticket", actionTicket);
            }}));
        } else {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统请求失败，请返回重试", null));
        }
    }
}

/// TODO: Implement: https://api-os-takumi.hoyoverse.com/account/device/api/preGrantByGame