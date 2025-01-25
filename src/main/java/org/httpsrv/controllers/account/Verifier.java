package org.httpsrv.controllers.account;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "account/ma-cn-verifier/verifier", produces = "application/json")
public class Verifier implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

}

/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileActionTicket
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyThirdpartyBindMobileCaptcha
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileCaptcha
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/getActionTicketInfo
/// TODO Implement: https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/verifyActionTicket
/// TODO Implement: https://hk4e-sdk-os.hoyoverse.com/hk4e_global/account/ma-verifier/api/createActionTicket