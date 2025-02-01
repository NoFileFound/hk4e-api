package org.httpsrv.controllers.combo.sdkconfig;

import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"hk4e_global/combo/sdkconfig/webview/api", "combo/sdkconfig/webview/api"}, produces = "application/json")
public class Webview implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

}


/// TODO Implement: https://hk4e-pre-sdk-os.hoyoverse.com/hk4e_global/combo/sdkconfig/webview/api/getActivities
/// TODO Implement: https://hk4e-pre-sdk-os.hoyoverse.com/hk4e_global/combo/sdkconfig/webview/api/getConfig