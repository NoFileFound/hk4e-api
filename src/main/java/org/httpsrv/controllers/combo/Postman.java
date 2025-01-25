package org.httpsrv.controllers.combo;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"hk4e_global/combo/postman", "hk4e_cn/combo/postman", "bus/combo/postman", "combo/postman"}, produces = "application/json")
public class Postman implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

}

/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/postman/device/setAlias
/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/postman/device/delAlias
/// TODO Implement: https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/postman/device/setUserTags