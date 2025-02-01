package org.httpsrv.controllers.common;

import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"common/hk4e_global/announcement/api", "common/hk4e_cn/announcement/api", "common/announcement/api"}, produces = "application/json")
public class Announcement implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAnnList">https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAnnList</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game: Game name<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - lang: Language<br>
     *      - bundle_id: Bundle<br>
     *      - platform: Platform<br>
     *      - region: server region<br>
     *      - level: client's level<br>
     *      - channel_id: Channel id<br>
     *      - uid: Account id<br>
     */
    @RequestMapping(value = "getAnnList")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAnnList() {
        int int_sz = 0;
        for(var ann : Config.getAnnouncementVar().announcementFullObjects) {
            int_sz += ann.list.size();
        }

        int total_sz = int_sz;
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", Config.getAnnouncementVar().announcementFullObjects);
            put("total", total_sz);
            put("type_list", Config.getAnnouncementVar().announcementTypeList);
            put("alert", false);
            put("alert_id", 0);
            put("timezone", 8);
            put("t", "1737817242_d4633d3881620c1ebb91ce606dc49f24_d41d8cd98f00b204e9800998ecf8427e");
            put("pic_list", Config.getAnnouncementVar().announcementPicFullObjects);
            put("pic_total", Config.getAnnouncementVar().announcementPicFullObjects.size());
            put("pic_type_list", Config.getAnnouncementVar().announcementPicsTypeList);
            put("pic_alert", false);
            put("pic_alert_id", 0);
            put("static_sign", "");
            put("banner", "");
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertAnn">https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertAnn</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game: Game name<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - lang: Language<br>
     *      - bundle_id: Bundle<br>
     *      - platform: Platform<br>
     *      - region: server region<br>
     *      - level: client's level<br>
     *      - channel_id: Channel id<br>
     *      - uid: Account id<br>
     */
    @RequestMapping(value = "getAlertAnn")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAlertAnn() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("alert", false);
        data.put("alert_id", 0);
        data.put("remind", true);
        data.put("extra_remind", false);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertPic">https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertPic</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game: Game name<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - lang: Language<br>
     *      - bundle_id: Bundle<br>
     *      - platform: Platform<br>
     *      - region: server region<br>
     *      - level: client's level<br>
     *      - channel_id: Channel id<br>
     *      - uid: Account id<br>
     */
    @RequestMapping(value = "getAlertPic")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAlertPic() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", Config.getAnnouncementVar().announcementPics);
            put("total", Config.getAnnouncementVar().announcementPics.size());
        }}));
    }

    /**
     *  Source: <a href="https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAnnContent">https://hk4e-ann-api.mihoyo.com/common/hk4e_cn/announcement/api/getAnnContent</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game: Game name<br>
     *      - game_biz: Genshin Impact release version type (hk4e_global/hk4e_cn)<br>
     *      - lang: Language<br>
     *      - bundle_id: Bundle<br>
     *      - platform: Platform<br>
     *      - region: server region<br>
     *      - level: client's level<br>
     *      - channel_id: Channel id<br>
     *      - uid: Account id<br>
     */
    @RequestMapping(value = "getAnnContent")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAnnContent() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", Config.getAnnouncementVar().announcementContent);
            put("total", Config.getAnnouncementVar().announcementContent.size());
            put("pic_list", Config.getAnnouncementVar().announcementPics);
            put("pic_total", Config.getAnnouncementVar().announcementPics.size());
        }}));
    }
}

/// TODO Implement: https://sandbox-api.mihoyo.com/common/hk4e_cn/announcement/api/consumeAlertPic