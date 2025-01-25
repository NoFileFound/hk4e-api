package org.httpsrv.controllers.mdk;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/atropos/api", "hk4e_cn/mdk/atropos/api", "mdk/atropos/api"}, produces = "application/json")
public class Atropos implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }


}

/// TODO: Implement: https://sandbox-sdk-os.hoyoverse.com/hk4e_global/mdk/atropos/api/checkOrder
/// TODO: Implement: https://sandbox-sdk-os.hoyoverse.com/hk4e_global/mdk/atropos/api/fetchGoods
/// TODO: Implement: https://sandbox-sdk-os.hoyoverse.com/hk4e_global/mdk/atropos/api/createOrder
/// TODO: Implement: https://sandbox-sdk-os.hoyoverse.com/hk4e_global/mdk/atropos/api/deduct