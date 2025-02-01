package org.httpsrv.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hyp/hyp-connect/api", produces = "application/json")
public class Hyp implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePackages">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePackages</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     */
    @GetMapping("getGamePackages")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGamePackages(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("game_packages", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("main", new LinkedHashMap<String, Object>() {{
                                    put("major", new LinkedHashMap<String, Object>() {{
                                        put("version", Config.getLauncherVar().game_version);
                                        put("game_pkgs", Config.getLauncherVar().game_pkgs);
                                        put("audio_pkgs", Config.getLauncherVar().audio_pkgs);
                                        put("res_list_url", Config.getLauncherVar().resource_list_url);
                                    }});
                            put("patches", Config.getLauncherVar().game_patches);
                            }}
                        );
                        put("pre_download", new LinkedHashMap<>() {{
                            put("major", null);
                            put("patches", Config.getLauncherVar().game_pre_download_patches);
                        }});
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_id: Game id<br>
     *      - language: language<br>
     */
    @GetMapping("getGameContent")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameContent(String launcher_id, String game_id) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("content", new LinkedHashMap<String, Object>() {
                    {
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_id);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("language", "en-us");
                        put("banners", Config.getLauncherVar().game_banners);
                        put("posts", Config.getLauncherVar().game_posts);
                        put("social_media_list", Config.getLauncherVar().game_social_media_list);
                    }}
                );
            }}
        ));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_id: Game id<br>
     *      - language: language<br>
     */
    @GetMapping("getAllGameBasicInfo")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAllGameBasicInfo(String launcher_id, String game_id) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("game_info_list", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_id);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("backgrounds", Config.getLauncherVar().game_backgrounds);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     */
    @GetMapping("getGameBranches")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameBranches(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("game_branches", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("main", new LinkedHashMap<String, Object>() {{
                            put("package_id", Config.getLauncherVar().game_package_id);
                            put("branch", Config.getLauncherVar().game_package_branch);
                            put("password", Config.getLauncherVar().game_package_password);
                            put("tag", Config.getLauncherVar().game_package_tag);
                            put("diff_tags", Config.getLauncherVar().game_package_diff_tags);
                        }});
                        put("pre_download", null);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     */
    @GetMapping("getGamePlugins")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGamePlugins(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("plugin_releases", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("plugins", Config.getLauncherVar().game_plugins);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - language: Language<br>
     */
    @GetMapping("getGames")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGames(String launcher_id) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("games", Config.getLauncherVar().game_games);
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     *      - sub_channel_id: Sub channel id<br>
     */
    @GetMapping("getGameDeprecatedFileConfigs")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameDeprecatedFileConfigs(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("deprecated_file_configs", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("deprecated_files", Config.getLauncherVar().game_deprecatedFiles);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     *      - sub_channel_id: Sub channel id<br>
     */
    @GetMapping("getGameChannelSDKs")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameChannelSDKs(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("game_channel_sdks", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("version", "2.14.2");
                        put("channel_sdk_pkg", Config.getLauncherVar().game_channel_sdk);
                        put("pkg_version_file_name", "sdk_pkg_version");
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - game_ids: List of games<br>
     */
    @GetMapping("getGameConfigs")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameConfigs(String launcher_id, String game_ids) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("launch_configs", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("game", new LinkedHashMap<String, Object>() {{
                            put("id", game_ids);
                            put("biz", Config.getRegionVar().resource_options.game_biz);
                        }});
                        put("exe_file_name", Config.getLauncherVar().exe_file_name);
                        put("installation_dir", Config.getLauncherVar().installation_dir);
                        put("audio_pkg_scan_dir", Config.getLauncherVar().audio_pkg_scan_dir);
                        put("audio_pkg_res_dir", Config.getLauncherVar().audio_pkg_res_dir);
                        put("audio_pkg_cache_dir", Config.getLauncherVar().audio_pkg_cache_dir);
                        put("game_cached_res_dir", Config.getLauncherVar().game_cached_res_dir);
                        put("game_screenshot_dir", Config.getLauncherVar().game_screenshot_dir);
                        put("game_log_gen_dir", Config.getLauncherVar().game_log_gen_dir);
                        put("game_crash_file_gen_dir", Config.getLauncherVar().game_crash_file_gen_dir);
                        put("default_download_mode", Config.getLauncherVar().default_download_mode);
                        put("enable_customer_service", Config.getLauncherVar().enable_customer_service);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAgreementVersion">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAgreementVersion</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     *      - language: language<br>
     */
    @SuppressWarnings("unused")
    @GetMapping("getAgreementVersion")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAgreementVersion(String launcher_id, String language) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("version", "4.0");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getFEPackage">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getFEPackage</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - launcher_id: Launcher id<br>
     */
    @GetMapping("getFEPackage")
    public ResponseEntity<LinkedHashMap<String, Object>> SendFEPackage(String launcher_id) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        data.put("version", "");
        data.put("build_version", "");
        data.put("url", "");
        data.put("type", "RELEASE_TYPE_UNSPECIFIED");
        data.put("enable_toast", false);
        data.put("release_id", "");
        data.put("update_strategy", "UPDATE_STRATEGY_UNSPECIFIED");

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getNotification">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getNotification</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - language: Language<br>
     *      - launcher_id: Launcher id<br>
     *      - type: notification type<br>
     */
    @SuppressWarnings("unused")
    @GetMapping("getNotification")
    public ResponseEntity<LinkedHashMap<String, Object>> SendNotification(String language, String launcher_id, String type) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("notifications", Config.getLauncherVar().notifications);
        }}));
    }
}