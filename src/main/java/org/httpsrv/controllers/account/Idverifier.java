package org.httpsrv.controllers.account;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "account/idverifier/idverifier", produces = "application/json")
public class Idverifier implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }


}

/// TODO Implement: https://gameapi-account.mihoyo.com/account/idverifier/idverifier/verifyRealperson
/// TODO Implement: https://gameapi-account.mihoyo.com/account/idverifier/idverifier/bindRealperson
/// TODO Implement: https://gameapi-account.mihoyo.com/account/idverifier/idverifier/confirmRealperson