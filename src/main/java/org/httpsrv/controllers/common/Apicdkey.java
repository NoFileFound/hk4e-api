package org.httpsrv.controllers.common;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "common/apicdkey/api", produces = "application/json")
public class Apicdkey implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }


}

/// TODO Implement: https://sg-hk4e-api.hoyoverse.com/common/apicdkey/api/webExchangeCdkey
/// TODO Implement: https://sg-hk4e-api.hoyoverse.com/common/apicdkey/api/exchangeCdkey