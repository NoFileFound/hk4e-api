package org.httpsrv.controllers.mdk;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.httpsrv.database.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/luckycat/luckycat", "hk4e_cn/mdk/luckycat/luckycat", "/mdk/luckycat/luckycat"}, produces = "application/json")
public class LuckyCat implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/listPayPlat">https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/listPayPlat</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br>
     */
    @RequestMapping(value = "listPayPlat", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LinkedHashMap<String, Object>> SendListPayPlatforms() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("pay_plats", Database.findAllPaymentPlatforms());
        }}));
    }
}

/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/listPriceTier
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/firstPayment
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/detectPay
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/detectPsPay
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/createOrder
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/checkOrder
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/deduct
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/retrieveToken
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/verify
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/queryGoods
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/bindReceipt
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/reportIapError