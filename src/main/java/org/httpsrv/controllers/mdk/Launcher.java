package org.httpsrv.controllers.mdk;

import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"hk4e_global/mdk/launcher/api","hk4e_cn/mdk/launcher/api","mdk/launcher/api"}, produces = "application/json")
public class Launcher implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://hk4e-launcher-static.mihoyo.com/hk4e_cn/mdk/launcher/api/resource">https://hk4e-launcher-static.mihoyo.com/hk4e_cn/mdk/launcher/api/resource</a><br><br>
     *  Method: POST<br><br>
     *  Parameters:<br>
     *      - channel_id: Channel id<br>
     *      - key: Launcher key<br>
     *      - launcher_id: Launcher id<br>
     *      - sub_channel_id: Sub channel id<br>
     */
    @GetMapping("resource")
    public ResponseEntity<LinkedHashMap<String, Object>> SendResource() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("deprecated_files", Config.getLauncherVar().game_deprecatedFiles);
        data.put("deprecated_packages", Config.getLauncherVar().game_deprecatedPackages);
        data.put("force_update", Config.getLauncherVar().game_force_update);
        data.put("web_url", "https://ys.mihoyo.com/launcher");
        data.put("pre_download_game", Config.getLauncherVar().game_predownload);
        data.put("sdk", Config.getLauncherVar().game_sdk);
        data.put("plugin", new LinkedHashMap<>() {{
            put("plugins", Config.getLauncherVar().game_plugins_v2);
            put("version", "3");
        }});
        data.put("game", new LinkedHashMap<>() {{
            put("latest", new LinkedHashMap<>() {{
                put("name", Config.getLauncherVar().game_name);
                put("version", Config.getLauncherVar().game_version);
                put("game_size", Config.getLauncherVar().game_size);
                put("game_md5", Config.getLauncherVar().game_md5);
                put("entry", Config.getLauncherVar().game_entry);
                put("voice_packs", Config.getLauncherVar().game_voice_packs);
                put("decompressed_path", Config.getLauncherVar().game_decompressed_path);
                put("segments", Config.getLauncherVar().game_segments);
                put("package_size", Config.getLauncherVar().game_package_size);
            }});
            put("diffs", Config.getLauncherVar().game_diffs);
        }});
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}