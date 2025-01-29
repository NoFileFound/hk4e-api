package org.httpsrv.controllers.combo;

import java.util.LinkedHashMap;
import org.httpsrv.data.ApplicationId;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.SetUserTagsBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.UserTag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"hk4e_global/combo/postman", "hk4e_cn/combo/postman", "combo/postman"}, method = RequestMethod.POST, produces = "application/json")
public class Postman implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sdk-os-static.hoyoverse.com/combo/postman/device/setUserTags">https://sdk-os-static.hoyoverse.com/combo/postman/device/setUserTags</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - app_id: Application id<br>
     *      - ts: Current timestamp<br>
     *      - user_tags: User tags<br>
     *      - sign: Signature<br>
     */
    @RequestMapping("device/setUserTags")
    public ResponseEntity<LinkedHashMap<String, Object>> SendSetUserTags(@RequestBody SetUserTagsBody body) {
        if(body == null || body.getTs() == null || body.getUser_tags() == null || body.getApp_id() == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_POSTMAN_PARAMS_ERROR, "params error", null));
        }

        if(body.getApp_id() != ApplicationId.GENSHIN_RELEASE.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_OVERSEAS.getValue() &&
                body.getApp_id() != ApplicationId.GENSHIN_SANDBOX_CHINA.getValue()) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_POSTMAN_PARAMS_ERROR, "invalid app", null));
        }



        if(body.getTs() - System.currentTimeMillis() > 5) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_CANCEL, "Cancelled due to frequent action.", null));
        }

        for(var userTag : body.getUser_tags()) {
            UserTag tag = Database.findUserTag(userTag.getAlias(), userTag.getRegion());
            if(tag != null) {
                tag.getAdd_tags().addAll(userTag.getAdd_tags());
                tag.getRemove_tags().addAll(userTag.getRemove_tags());
                tag.setLastUpdateTs(System.currentTimeMillis());
                tag.save();
            }
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", null));
    }
}

/// TODO Implement: https://sdk-os-static.hoyoverse.com/combo/postman/device/setAlias
/// TODO Implement: https://sdk-os-static.hoyoverse.com/combo/postman/device/delAlias