package org.httpsrv.controllers.common;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "common/csc/client", produces = "application/json", method = RequestMethod.POST)
public class Csc implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }


}

/// TODO Implement: https://sg-public-api.hoyoverse.com/common/csc/client/addCliTempInfo?sign_type=2&auth_appid=csc&authkey_ver=1