package org.httpsrv.controllers.mdk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/agreement/api", "hk4e_cn/mdk/agreement/api", "mdk/agreement/api"}, produces = "application/json")
public class Agreement implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/agreement/api/getAgreementInfos">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/agreement/api/getAgreementInfos</a><br><br>
     *  Method: GET<br><br>
     *  Parameters: <br>
     *  - biz_key - Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *  - country_code: Country code<br>
     *  - token: session key<br>
     *  - uid: Account id<br>
     */
    @GetMapping("getAgreementInfos")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAgreementInfos() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("marketing_agreements", new ArrayList<>());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/agreement/api/operateAgreement">https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/agreement/api/operateAgreement</a><br><br>
     *  Method: POST<br>
     */
    @PostMapping("operateAgreement")
    public ResponseEntity<LinkedHashMap<String, Object>> SendOperateAgreement() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }
}