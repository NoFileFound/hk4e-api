package org.httpsrv.controllers;

import java.util.LinkedHashMap;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.database.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"downloader/sophon/api", "downloader/sophon_chunk/api"}, produces = "application/json")
public class Sophon implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-downloader-api.hoyoverse.com/downloader/sophon_chunk/api/getPatchBuild">https://sg-downloader-api.hoyoverse.com/downloader/sophon_chunk/api/getPatchBuild</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - branch: game branch<br>
     *      - package_id: game package id<br>
     *      - password: game package password<br>
     *      - plat_app: platform application<br>
     */
    @PostMapping(value = "getPatchBuild")
    public ResponseEntity<LinkedHashMap<String, Object>> SendPatchBuild(String branch, String package_id, String password) {
        if(!Config.getLauncherVar().game_package_branch.equals(branch) || !Config.getLauncherVar().game_package_id.equals(package_id) || !Config.getLauncherVar().game_package_password.equals(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_GRANT_INVALID_CODE, "invalid params", null));
        }
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("build_id", Config.getLauncherVar().game_build_id);
        data.put("tag", Config.getLauncherVar().game_package_tag);
        data.put("manifests", Config.getLauncherVar().game_manifests_patch);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="http://sg-public-api.hoyoverse.com/downloader/sophon/api/getABTest">http://sg-public-api.hoyoverse.com/downloader/sophon/api/getABTest</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "getABTest")
    public ResponseEntity<LinkedHashMap<String, Object>> SendABTest() {
        LinkedHashMap<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("retcode", Retcode.RETCODE_SUCC);
        arguments.put("success", true);
        arguments.put("message", "");
        arguments.put("data", Database.findAllLauncherExperiments("58,59"));
        return ResponseEntity.ok(arguments);
    }

    /**
     *  Source: <a href="https://sg-public-api.hoyoverse.com/downloader/sophon_chunk/api/getBuild">https://sg-public-api.hoyoverse.com/downloader/sophon_chunk/api/getBuild</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - branch: game branch<br>
     *      - package_id: game package id<br>
     *      - password: game package password<br>
     *      - plat_app: platform application<br>
     */
    @GetMapping(value = {"getBuild", "getBuildWithStokenLogin"})
    public ResponseEntity<LinkedHashMap<String, Object>> SendBuild(String branch, String package_id, String password) {
        if(!Config.getLauncherVar().game_package_branch.equals(branch) || !Config.getLauncherVar().game_package_id.equals(package_id) || !Config.getLauncherVar().game_package_password.equals(password)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_GRANT_INVALID_CODE, "invalid params", null));
        }
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("build_id", Config.getLauncherVar().game_build_id);
        data.put("tag", Config.getLauncherVar().game_package_tag);
        data.put("manifests", Config.getLauncherVar().game_manifests);

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sg-public-api-static.hoyoverse.com/downloader/sophon/api/getParamsConfig">https://sg-public-api-static.hoyoverse.com/downloader/sophon/api/getParamsConfig</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - client_type: Platform<br>
     *      - plat_app: platform launcher name<br>
     */
    @GetMapping(value = "getParamsConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendParamsConfig(String plat_app, Integer client_type) {
        if(plat_app == null || !Config.getPropertiesVar().launcherIds.contains(plat_app)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("params_config", new LinkedHashMap<String, Object>() {{
                put("is_enabled", true);
                put("buffer_size", 32);
                put("retry_on_download_failure", 3);
                put("request_timeout_wait_time", 30);
                put("max_concurrent_tasks", 4);
                put("max_validation_threads", 1);
                put("error_retry_mode", 1);
                put("max_backoff_seconds", 33);
                put("buffer_queue_size", 10240);
                put("multi_thread_io_read_enabled", true);
                put("multi_thread_io_write_enabled", true);
                put("chunk_max_concurrent_tasks", 12);
                put("chunk_max_validation_threads", 6);
                if(client_type < 3) {
                    put("pcdn", null);
                }
                else {
                    put("pcdn", new LinkedHashMap<String, Object>() {{
                        put("is_enabled", false);
                        put("distribution", new LinkedHashMap<>());
                        put("max_redirect_percent", 0);
                        put("max_failed_percent", 0);
                        put("file_size_threshold", 0);
                    }});
                }

                put("ldiff_max_concurrent_tasks", 2);
            }});
        }}));
    }
}