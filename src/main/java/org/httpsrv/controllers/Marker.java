package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sdk_global/marker/api", produces = "application/json", method = RequestMethod.POST)
public class Marker implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

}

/// TODO Implement: https://sdk-common-api.hoyoverse.com/sdk_global/marker/api/genMark