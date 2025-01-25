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
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("main", new LinkedHashMap<String, Object>() {{
                                    put("major", new LinkedHashMap<String, Object>() {{
                                        put("version", "5.3.0");
                                        put("game_pkgs", new ArrayList<>(List.of(
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.001");
                                                    put("md5", "d7ea7d49334e03e590db3f047cd9ea88");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.002");
                                                    put("md5", "b4178034c1d09e889e43fd76b3fb4d3c");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.003");
                                                    put("md5", "43b70975fcb957abaaaf7d940969679a");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.004");
                                                    put("md5", "d734b1edeb1b2b0d47d4d4bab7af6778");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.005");
                                                    put("md5", "95abe987ff924c21f3e5085492448760");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.006");
                                                    put("md5", "492510ae74ae8ac696ee59b4e831d039");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.007");
                                                    put("md5", "0c68334b33ee878c5beac321339b9447");
                                                    put("size", "10737418240");
                                                    put("decompressed_size", "21485322240");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/GenshinImpact_5.3.0.zip.008");
                                                    put("md5", "18d44596a5f1467682f5e038c80bd92a");
                                                    put("size", "1020679689");
                                                    put("decompressed_size", "2051845138");
                                                }}
                                        )));
                                        put("audio_pkgs", new ArrayList<>(List.of(
                                                new LinkedHashMap<>() {{
                                                    put("language", "zh-cn");
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/Audio_Chinese_5.3.0.zip");
                                                    put("md5", "2727087a20d630d35efe804ae683e72e");
                                                    put("size", "15243583853");
                                                    put("decompressed_size", "30497653466");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("language", "en-us");
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/Audio_English(US)_5.3.0.zip");
                                                    put("md5", "76f338d1925ff39cbf73f0418e9ae354");
                                                    put("size", "17440797562");
                                                    put("decompressed_size", "34892080884");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("language", "ko-kr");
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/Audio_Korean_5.3.0.zip");
                                                    put("md5", "6356a494c7cce397bdbb1213aa6e7298");
                                                    put("size", "15027694712");
                                                    put("decompressed_size", "30065875184");
                                                }},
                                                new LinkedHashMap<>() {{
                                                    put("language", "ja-jp");
                                                    put("url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/Audio_Japanese_5.3.0.zip");
                                                    put("md5", "f2b1c1f217dafbcdf27aeece987256b7");
                                                    put("size", "19798806635");
                                                    put("decompressed_size", "39608099030");
                                                }}
                                        )));
                                        put("res_list_url", "https://autopatchhk.yuanshen.com/client_app/download/pc_zip/20241219110745_1vT3FzXdDTDFZFrL/ScatteredFiles");
                                    }});
                            put("patches", new ArrayList<>());
                                }}
                        );
                        put("pre_download", new LinkedHashMap<>() {{
                            put("major", null);
                            put("patches", new ArrayList<>());
                        }});
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
     *      - launcher_id: Launcher id<br>
     *      - game_id: Game id<br>
     *      - language: language<br>
     */
    @GetMapping("getGameContent")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGameContent(String launcher_id, String game_id) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(
                Retcode.RETCODE_SUCC,
                "OK",
                new LinkedHashMap<String, Object>() {{
                    put("content",
                            new LinkedHashMap<String, Object>() {{
                                put("game", new LinkedHashMap<String, Object>() {{
                                    put("id", game_id);
                                    put("biz", "hk4e_global");
                                }});
                                put("language", "en-us");
                                put("banners", new ArrayList<>(List.of(
                                        new LinkedHashMap<String, Object>() {{
                                            put("id", "2PyQ1soJba");
                                            put("image", new LinkedHashMap<String, Object>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/22/3e53f1a3b6a4b3e9e5d4f89b3f99326f_171857030937205121.jpg");
                                                put("link", "https://hoyo.link/n4HoYW3VF");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "cRijxtRvq1");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/22/a5a1f82ffd51ce63bc961bc4ee3a1af5_3480523652496990895.png");
                                                put("link", "https://hoyo.link/SWOhJ1OZy");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "w9leSrCvXg");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/21/2df9cd526506a056355bd66a9b993858_4553611771446266124.png");
                                                put("link", "https://www.hoyolab.com/article_pre/18043?lang=en-us&utm_source=launcher&utm_medium=event&utm_id=2");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "FL6n64tupo");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/21/845a80f2d5bc7c6cf72c97312497a66e_7571008108981442285.jpg");
                                                put("link", "https://hoyo.link/NUdTwLl19");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "fTWvfeaDed");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/20/270a0efc3c24e7fff3a2555872c465ec_8470806807889627039.jpg");
                                                put("link", "https://hoyo.link/oHj1lszyS");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "3WUdvVvksD");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/17/4f4c0a80964b467ac794aa3cad04a646_5191407657216564437.jpg");
                                                put("link", "https://hoyo.link/hejpuiJbI");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "emVUV333iT");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/17/eda9900d1fafedeaefb70be9573ca8b7_6702079311051347163.jpg");
                                                put("link", "https://hoyo.link/hejpuiJbI");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "3MqffxQhZY");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/17/c48609b7a294a03f5c3c119c6e95c119_3690465803796772882.jpg");
                                                put("link", "https://hoyo.link/hejpuiJbI");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "ySH7fAWX5Y");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2025/01/17/bb6171e6447c3ff51aa0201691696500_89128305541006815.jpg");
                                                put("link", "https://hoyo.link/hejpuiJbI");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "jYYb27wxmg");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2025/01/17/222aae3a9fe6c21139ac2f9bbf16ed7c_2244215720846156868.jpg");
                                                put("link", "https://hoyo.link/N7SPDiORI");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "W6MnehnnHH");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2024/12/20/683a72f2a005abaa5abe81189c0ae317_3064233963328185401.jpg");
                                                put("link", "https://hoyo.link/8mclFBAL");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "Xb09hRfNKo");
                                            put("image", new LinkedHashMap<>() {{
                                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2024/12/20/471185f5c8dade5709fed055acdc75a9_4358140955063053563.jpg");
                                                put("link", "https://hoyo.link/9iclFBAL");
                                                put("login_state_in_link", false);
                                            }});
                                            put("i18n_identifier", "");
                                        }}
                                )));
                                put("posts", new ArrayList<>(List.of(
                                        new LinkedHashMap<>() {{
                                            put("id", "cuHrY0AnZ2");
                                            put("type", "POST_TYPE_INFO");
                                            put("title", "Version 5.3 Events Preview - Phase II");
                                            put("link", "https://hoyo.link/arBnebxqM");
                                            put("date", "01/16");
                                            put("login_state_in_link", false);
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "ah582ZsnCP");
                                            put("type", "POST_TYPE_ANNOUNCE");
                                            put("title", "V5.3 Known Issues & Update");
                                            put("link", "https://www.hoyolab.com/article/36039108?utm_source=launcher&utm_medium=notice&utm_id=2");
                                            put("date", "01/01");
                                            put("login_state_in_link", false);
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "CTyZP5f4Jy");
                                            put("type", "POST_TYPE_ACTIVITY");
                                            put("title", "\"Stand With Mavuika\" Version 5.3 Creator Incentive Program");
                                            put("link", "https://hoyo.link/9kalFLAL");
                                            put("date", "01/07");
                                            put("login_state_in_link", false);
                                            put("i18n_identifier", "FALSE");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "pp1aKsnDTN");
                                            put("type", "POST_TYPE_ACTIVITY");
                                            put("title", "\"Incandescent Ode of Resurrection\" Version 5.3 Strategy Guides Contest");
                                            put("link", "https://hoyo.link/IgrTZvm6d");
                                            put("date", "01/01");
                                            put("login_state_in_link", false);
                                            put("i18n_identifier", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "1qNGDJ5L62");
                                            put("type", "POST_TYPE_INFO");
                                            put("title", "\"Incandescent Ode of Resurrection\" Version 5.3 Events Preview - Phase I");
                                            put("link", "https://hoyo.link/QWRkJCCis");
                                            put("date", "12/30");
                                            put("login_state_in_link", false);
                                            put("i18n_identifier", "FALSE");
                                        }}
                                )));
                                put("social_media_list", new ArrayList<>(List.of(
                                        new LinkedHashMap<>() {{
                                            put("id", "2bsPSO6osX");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/a1b226ceebccd56768c5272f306f380e_4710708920702489320.png");
                                                put("hover_url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/dc794590ce0ce174f9627d74ac6cb38b_75198024119073500.png");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", List.of(new LinkedHashMap<>() {{
                                                put("title", "HoYoLAB Community");
                                                put("link", "https://www.hoyolab.com/home?lang=en-us&utm_source=launcher&utm_medium=game&from_id=2&utm_campaign=mimoicon");
                                                put("login_state_in_link", false);
                                            }}));
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "Ki7QSzK75e");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/de382e69cb1a7afc46486c95207db733_5328107646823583568.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/56v1BCAd");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "pSKVL6QcqT");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/f6cc67d31fc009dc6d369cd2a619b1b4_4771236149512887251.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/e0v1BRAd");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "gl1hf4ejqK");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/17106a7bbb7c1bcae00b7f3e1612fc3b_348615524006532840.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/a2v1B9Ad");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "CIeu5LRh70");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/3b36920e61c880999325559f0df506de_3840726654233510117.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/ecv1BbAd");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "D3omMxGLzu");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/54024804ec1b2247cf219c05e39d6c78_6713099726461129324.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/6DOeFCAL");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "Wf5o1ByBRz");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/63dcf03a26d6cdd1ef4f23222fa4ca43_8778273236813196940.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/5GOeFBAL");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }},
                                        new LinkedHashMap<>() {{
                                            put("id", "1TPRyiQdK5");
                                            put("icon", new LinkedHashMap<>() {{
                                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/15/4ddd0290b1c1829644167e2cbceb122a_1272686305946326383.png");
                                                put("hover_url", "");
                                                put("link", "https://hoyo.link/9vgeFBAL");
                                                put("login_state_in_link", false);
                                                put("md5", "");
                                                put("size", 0);
                                            }});
                                            put("qr_image", new LinkedHashMap<>() {{
                                                put("url", "");
                                                put("link", "");
                                                put("login_state_in_link", false);
                                            }});
                                            put("qr_desc", "");
                                            put("links", new ArrayList<>()); // Empty list
                                            put("enable_red_dot", false);
                                            put("red_dot_content", "");
                                        }}
                                )));
                            }}
                    );
                }}
        ));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("backgrounds", new ArrayList<>(List.of(
                                new LinkedHashMap<String, Object>() {{
                                    put("id", "OpjTr7HAka");
                                    put("background", new LinkedHashMap<String, Object>() {{
                                        put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2025/01/21/eaaf0ff8bc99119e407ddc5b6614ad89_7396475589266887715.webp");
                                        put("link", "");
                                        put("login_state_in_link", false);
                                    }});
                                    put("icon", new LinkedHashMap<String, Object>() {{
                                        put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2025/01/21/2ccbec7555004ab1ce7cb9934c4a650f_1634870223182394088.png");
                                        put("hover_url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2025/01/21/fcff765c93c1e13adb8e5a03ced3bebf_5523217199995757226.png");
                                        put("link", "https://act.hoyoverse.com/ys/event/blue-post/index.html?page_sn=b0f94ebdcdaf42cc&mhy_presentation_style=fullscreen&utm_source=launchergenshin&utm_medium=news#/index");
                                        put("login_state_in_link", false);
                                        put("md5", "");
                                        put("size", 0);
                                    }});
                                }}
                            ))
                        );
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("main", new LinkedHashMap<String, Object>() {{
                            put("package_id", "ScSYQBFhu9");
                            put("branch", "main");
                            put("password", "bDL4JUHL625x");
                            put("tag", "5.3.0");
                            put("diff_tags", new ArrayList<>(List.of(
                                    "5.2.0",
                                    "5.1.0"
                            )));
                        }}
                        );
                        put("pre_download", null);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("plugins", new ArrayList<>(List.of(
                                new LinkedHashMap<String, Object>() {{
                                    put("plugin_id", "K3c0Ay0QiR");
                                    put("release_id", "SNDSf61xEH");
                                    put("version", "1.1.0");
                                    put("plugin_pkg", new LinkedHashMap<String, Object>() {{
                                        put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/17/1715956653272/DXSETUP.zip");
                                        put("md5", "ca2ac3835d7d7da6cb8624fefb177083");
                                        put("size", "100647892");
                                        put("decompressed_size", "201295784");
                                        put("command", "DXSETUP/DXSETUP.exe /silent");
                                        put("validation", "[{\"path\":\"DXSETUP/Apr2005_d3dx9_25_x64.cab\",\"md5\":\"fe9e6c4c7d7aa341a84f039d954560e9\"},{\"path\":\"DXSETUP/Apr2005_d3dx9_25_x86.cab\",\"md5\":\"b7457f5d50176d38d36a2e2dc0429fd2\"},{\"path\":\"DXSETUP/Apr2006_d3dx9_30_x64.cab\",\"md5\":\"3d797333854abb64abb6987afe7b4ce9\"},{\"path\":\"DXSETUP/Apr2006_d3dx9_30_x86.cab\",\"md5\":\"2bf0debd3d503ef601e5ee17bfbd139e\"},{\"path\":\"DXSETUP/Apr2006_MDX1_x86.cab\",\"md5\":\"579bef95277d18191e77f631b9f886c8\"},{\"path\":\"DXSETUP/Apr2006_MDX1_x86_Archive.cab\",\"md5\":\"78f5a0256e021167a4b15a2801b4ae58\"},{\"path\":\"DXSETUP/Apr2006_XACT_x64.cab\",\"md5\":\"84aced4aa62f32d3746e32a73c39721f\"},{\"path\":\"DXSETUP/Apr2006_XACT_x86.cab\",\"md5\":\"1407603eaacff8be5ac25de29f29d267\"},{\"path\":\"DXSETUP/Apr2006_xinput_x64.cab\",\"md5\":\"f4a3ef18ca06fb0c707664d298b434fe\"},{\"path\":\"DXSETUP/Apr2006_xinput_x86.cab\",\"md5\":\"19c45fcc5c93c370f9b860c06e012e9a\"},{\"path\":\"DXSETUP/APR2007_d3dx10_33_x64.cab\",\"md5\":\"3fba5d677f19c48f210ca5f6372d55ad\"},{\"path\":\"DXSETUP/APR2007_d3dx10_33_x86.cab\",\"md5\":\"83033f2988efe706886e5aba492974d7\"},{\"path\":\"DXSETUP/APR2007_d3dx9_33_x64.cab\",\"md5\":\"71c6a3c5c04093254e752e0d15af1869\"},{\"path\":\"DXSETUP/APR2007_d3dx9_33_x86.cab\",\"md5\":\"8e3e46de4d520b4836b8776a2c771eaa\"},{\"path\":\"DXSETUP/APR2007_XACT_x64.cab\",\"md5\":\"8acbb49a7c2a97c12f63c16bdd8f7512\"},{\"path\":\"DXSETUP/APR2007_XACT_x86.cab\",\"md5\":\"96f1ec4650282a0fd2dbd1eea7f5cfd6\"},{\"path\":\"DXSETUP/APR2007_xinput_x64.cab\",\"md5\":\"743b333c2db3d4cf190fb39c29f3c346\"},{\"path\":\"DXSETUP/APR2007_xinput_x86.cab\",\"md5\":\"c234df417c9b12e2d31c7fd1e17e4786\"},{\"path\":\"DXSETUP/Aug2005_d3dx9_27_x64.cab\",\"md5\":\"c91aa9c752a7c46aa11101347209ba33\"},{\"path\":\"DXSETUP/Aug2005_d3dx9_27_x86.cab\",\"md5\":\"5cdddd58ae010e03ecc6ced128002291\"},{\"path\":\"DXSETUP/AUG2006_XACT_x64.cab\",\"md5\":\"9cafd440798ed33ec6561b09549ab263\"},{\"path\":\"DXSETUP/AUG2006_XACT_x86.cab\",\"md5\":\"155b66797691647bbb935f0c0872c64d\"},{\"path\":\"DXSETUP/AUG2006_xinput_x64.cab\",\"md5\":\"866da8edbc4e6ecbc4b04e2a77613aad\"},{\"path\":\"DXSETUP/AUG2006_xinput_x86.cab\",\"md5\":\"75b3d7d008dad88064ec3d768fc60fca\"},{\"path\":\"DXSETUP/AUG2007_d3dx10_35_x64.cab\",\"md5\":\"a6e055ffabc8d8b2debbcc37e611078e\"},{\"path\":\"DXSETUP/AUG2007_d3dx10_35_x86.cab\",\"md5\":\"61691eee4a5e4bc6dff8a26c68003918\"},{\"path\":\"DXSETUP/AUG2007_d3dx9_35_x64.cab\",\"md5\":\"87b58e7b8172bf20beeada819496b154\"},{\"path\":\"DXSETUP/AUG2007_d3dx9_35_x86.cab\",\"md5\":\"d9a1ee62d3072b3182ba87f05aa22be0\"},{\"path\":\"DXSETUP/AUG2007_XACT_x64.cab\",\"md5\":\"0e9ab7f465516fc690c79e230185caae\"},{\"path\":\"DXSETUP/AUG2007_XACT_x86.cab\",\"md5\":\"2bdd2481e5982de14ec680aa8b38347f\"},{\"path\":\"DXSETUP/Aug2008_d3dx10_39_x64.cab\",\"md5\":\"b757dc9954f5456c542324ecb25addd2\"},{\"path\":\"DXSETUP/Aug2008_d3dx10_39_x86.cab\",\"md5\":\"cc9ebcf0620ae732796871ef529afef9\"},{\"path\":\"DXSETUP/Aug2008_d3dx9_39_x64.cab\",\"md5\":\"de3cec9d7a3ce447d4e8ee6c63b1460d\"},{\"path\":\"DXSETUP/Aug2008_d3dx9_39_x86.cab\",\"md5\":\"73b607bb226c6e2a7f470317187bd551\"},{\"path\":\"DXSETUP/Aug2008_XACT_x64.cab\",\"md5\":\"0f8573acb2a0e300b97f6743e8a4e7a5\"},{\"path\":\"DXSETUP/Aug2008_XACT_x86.cab\",\"md5\":\"2a8399c8f967263d487ea57289409866\"},{\"path\":\"DXSETUP/Aug2008_XAudio_x64.cab\",\"md5\":\"69478ec34ea5679088c40b8d349c3227\"},{\"path\":\"DXSETUP/Aug2008_XAudio_x86.cab\",\"md5\":\"cce65c41e6c2b87d0ecb5c666302f06d\"},{\"path\":\"DXSETUP/Aug2009_D3DCompiler_42_x64.cab\",\"md5\":\"a34039a6dcc7c42be4d8716e8d73925e\"},{\"path\":\"DXSETUP/Aug2009_D3DCompiler_42_x86.cab\",\"md5\":\"683d8c01c5b5e1e94b6b5901c45927da\"},{\"path\":\"DXSETUP/Aug2009_d3dcsx_42_x64.cab\",\"md5\":\"a91957a8e5f8a7040690a1c2a6349e65\"},{\"path\":\"DXSETUP/Aug2009_d3dcsx_42_x86.cab\",\"md5\":\"d4d7680aee67fc5ae2bc26fbb228c95a\"},{\"path\":\"DXSETUP/Aug2009_d3dx10_42_x64.cab\",\"md5\":\"3c37874314f206c66c2f6ba1141c6bc2\"},{\"path\":\"DXSETUP/Aug2009_d3dx10_42_x86.cab\",\"md5\":\"45c6e46ace94ab79d196b394de916428\"},{\"path\":\"DXSETUP/Aug2009_d3dx11_42_x64.cab\",\"md5\":\"a94559cf5e72e296e14329173c9aa2a0\"},{\"path\":\"DXSETUP/Aug2009_d3dx11_42_x86.cab\",\"md5\":\"07f2ca007a014f75d346d86e2ae5326e\"},{\"path\":\"DXSETUP/Aug2009_d3dx9_42_x64.cab\",\"md5\":\"70cc5b4b3c39879d3e9058a33ef94f27\"},{\"path\":\"DXSETUP/Aug2009_d3dx9_42_x86.cab\",\"md5\":\"d6e61def8b75a600f46605fc204d8e09\"},{\"path\":\"DXSETUP/Aug2009_XACT_x64.cab\",\"md5\":\"7546cb34a87aaaa16e56d5998236bf13\"},{\"path\":\"DXSETUP/Aug2009_XACT_x86.cab\",\"md5\":\"f5241686cef2f3599d7e5115cb888599\"},{\"path\":\"DXSETUP/Aug2009_XAudio_x64.cab\",\"md5\":\"67373584c06ca2d6a9c872637868edde\"},{\"path\":\"DXSETUP/Aug2009_XAudio_x86.cab\",\"md5\":\"57fc6049ef196bedcc893738bc609df8\"},{\"path\":\"DXSETUP/Dec2005_d3dx9_28_x64.cab\",\"md5\":\"95a871983dcce1c8a4fd87483e1f047c\"},{\"path\":\"DXSETUP/Dec2005_d3dx9_28_x86.cab\",\"md5\":\"c5455fac40117333ba1ae32c8cb91633\"},{\"path\":\"DXSETUP/DEC2006_d3dx10_00_x64.cab\",\"md5\":\"ecafbf094869c785aae0730ece84c446\"},{\"path\":\"DXSETUP/DEC2006_d3dx10_00_x86.cab\",\"md5\":\"65c0d1dd12d57b521553f1c8cfa41ee6\"},{\"path\":\"DXSETUP/DEC2006_d3dx9_32_x64.cab\",\"md5\":\"5e89ea94e146f3ab247ed90b8f262bd0\"},{\"path\":\"DXSETUP/DEC2006_d3dx9_32_x86.cab\",\"md5\":\"cfc6966680df13a72e2284b67119cdaa\"},{\"path\":\"DXSETUP/DEC2006_XACT_x64.cab\",\"md5\":\"e40fa2ae617e4810b457a3e598abb856\"},{\"path\":\"DXSETUP/DEC2006_XACT_x86.cab\",\"md5\":\"e3f08fd568a345b9c9f182e852de219b\"},{\"path\":\"DXSETUP/DSETUP.dll\",\"md5\":\"eb701def7d0809e8da765a752ab42be5\"},{\"path\":\"DXSETUP/dsetup32.dll\",\"md5\":\"d8fa7bb4fe10251a239ed75055dd6f73\"},{\"path\":\"DXSETUP/dxdllreg_x86.cab\",\"md5\":\"4b4f83d1dd86314b65760f44f9c7a9b1\"},{\"path\":\"DXSETUP/DXSETUP.exe\",\"md5\":\"bf3f290275c21bdd3951955c9c3cf32c\"},{\"path\":\"DXSETUP/dxupdate.cab\",\"md5\":\"d495680aba28caafc4c071a6d0fe55ac\"},{\"path\":\"DXSETUP/Feb2005_d3dx9_24_x64.cab\",\"md5\":\"f0215800a0031dd763c3cd7913717587\"},{\"path\":\"DXSETUP/Feb2005_d3dx9_24_x86.cab\",\"md5\":\"7ee3c0e90d831041e6c845672660fe60\"},{\"path\":\"DXSETUP/Feb2006_d3dx9_29_x64.cab\",\"md5\":\"57baca471c5419ca43bfda1b65774406\"},{\"path\":\"DXSETUP/Feb2006_d3dx9_29_x86.cab\",\"md5\":\"09fe2721b43875cc085e0da4470a47ad\"},{\"path\":\"DXSETUP/Feb2006_XACT_x64.cab\",\"md5\":\"733c3a0ada15d096265570e79044d1da\"},{\"path\":\"DXSETUP/Feb2006_XACT_x86.cab\",\"md5\":\"f66725e68f488c4379d8294449773136\"},{\"path\":\"DXSETUP/FEB2007_XACT_x64.cab\",\"md5\":\"b6f585c314d27f04fe709139ee67411c\"},{\"path\":\"DXSETUP/FEB2007_XACT_x86.cab\",\"md5\":\"99bb1dda11228ba060988176e9c9130c\"},{\"path\":\"DXSETUP/Feb2010_X3DAudio_x64.cab\",\"md5\":\"db47136a200e326174ce790359596eb6\"},{\"path\":\"DXSETUP/Feb2010_X3DAudio_x86.cab\",\"md5\":\"88dfbb4c1876e80a1864265c61c7a7fd\"},{\"path\":\"DXSETUP/Feb2010_XACT_x64.cab\",\"md5\":\"d33d49a07398200cd12ad156f04af20f\"},{\"path\":\"DXSETUP/Feb2010_XACT_x86.cab\",\"md5\":\"f86c6b70038028ddea66ed04eb51b50c\"},{\"path\":\"DXSETUP/Feb2010_XAudio_x64.cab\",\"md5\":\"1cbf47b0ae9356d6ec01e4123dee28d4\"},{\"path\":\"DXSETUP/Feb2010_XAudio_x86.cab\",\"md5\":\"64972172a756a0b255dbadb9792d0316\"},{\"path\":\"DXSETUP/Jun2005_d3dx9_26_x64.cab\",\"md5\":\"d795fd4fe6bacecbe706430e16f80a8b\"},{\"path\":\"DXSETUP/Jun2005_d3dx9_26_x86.cab\",\"md5\":\"cc994475277a3d4fb3257c6230d12f8d\"},{\"path\":\"DXSETUP/JUN2006_XACT_x64.cab\",\"md5\":\"36a6006b780b8edbc497cb5acaa13502\"},{\"path\":\"DXSETUP/JUN2006_XACT_x86.cab\",\"md5\":\"23953b5d75968daba6d2639969324853\"},{\"path\":\"DXSETUP/JUN2007_d3dx10_34_x64.cab\",\"md5\":\"1b2084066a0ff3d37530db9f94adb11e\"},{\"path\":\"DXSETUP/JUN2007_d3dx10_34_x86.cab\",\"md5\":\"47a295eecac0609a1956ce3ba68b36af\"},{\"path\":\"DXSETUP/JUN2007_d3dx9_34_x64.cab\",\"md5\":\"cee8dd3db3c4329e494ae15d98f8726b\"},{\"path\":\"DXSETUP/JUN2007_d3dx9_34_x86.cab\",\"md5\":\"c0d871ca7822160afac25814fd8621b7\"},{\"path\":\"DXSETUP/JUN2007_XACT_x64.cab\",\"md5\":\"d819b3a479e14634c88a40cc93d69be8\"},{\"path\":\"DXSETUP/JUN2007_XACT_x86.cab\",\"md5\":\"bdd64c54e351a8716c0ad8a32a1e0241\"},{\"path\":\"DXSETUP/JUN2008_d3dx10_38_x64.cab\",\"md5\":\"64a7277926e965aee05e04e30f6be400\"},{\"path\":\"DXSETUP/JUN2008_d3dx10_38_x86.cab\",\"md5\":\"ba61f34b185e8d528a2bb62530fc6ed6\"},{\"path\":\"DXSETUP/JUN2008_d3dx9_38_x64.cab\",\"md5\":\"e57d72f3aadd87b2b27216952d831e22\"},{\"path\":\"DXSETUP/JUN2008_d3dx9_38_x86.cab\",\"md5\":\"5a7c86ef6f51c4f254c1d8cf0f2cfbe3\"},{\"path\":\"DXSETUP/JUN2008_X3DAudio_x64.cab\",\"md5\":\"f78a206008039dd2e99b190d883fe8eb\"},{\"path\":\"DXSETUP/JUN2008_X3DAudio_x86.cab\",\"md5\":\"9d2f859a3fb01bd9a3a547e04c8b72d1\"},{\"path\":\"DXSETUP/JUN2008_XACT_x64.cab\",\"md5\":\"fc4be653fcfdf85d3c9cd5917945eaba\"},{\"path\":\"DXSETUP/JUN2008_XACT_x86.cab\",\"md5\":\"d00ab15b2a00346d5ecad8d92573cee2\"},{\"path\":\"DXSETUP/JUN2008_XAudio_x64.cab\",\"md5\":\"5afa7d889a3d59436225b2f8c7f1ef30\"},{\"path\":\"DXSETUP/JUN2008_XAudio_x86.cab\",\"md5\":\"a93550acd0621db2b06d9258eae3bb06\"},{\"path\":\"DXSETUP/Jun2010_D3DCompiler_43_x64.cab\",\"md5\":\"0109c2931c4442c8192539f1991b6985\"},{\"path\":\"DXSETUP/Jun2010_D3DCompiler_43_x86.cab\",\"md5\":\"f7f554aa613eccf065575b8c69717ef7\"},{\"path\":\"DXSETUP/Jun2010_d3dcsx_43_x64.cab\",\"md5\":\"850aafddfefea671a2e1bbf1b65f2a8e\"},{\"path\":\"DXSETUP/Jun2010_d3dcsx_43_x86.cab\",\"md5\":\"44dba9557f956787b66f285776c3dccb\"},{\"path\":\"DXSETUP/Jun2010_d3dx10_43_x64.cab\",\"md5\":\"2d9586b276a561924ff2335fccaee914\"},{\"path\":\"DXSETUP/Jun2010_d3dx10_43_x86.cab\",\"md5\":\"a89b98ab89e0d4ff9dae412d49e27c51\"},{\"path\":\"DXSETUP/Jun2010_d3dx11_43_x64.cab\",\"md5\":\"96e7847a914afcb489194940b06a5c23\"},{\"path\":\"DXSETUP/Jun2010_d3dx11_43_x86.cab\",\"md5\":\"758c5a459978cb2c68a300a60da153be\"},{\"path\":\"DXSETUP/Jun2010_d3dx9_43_x64.cab\",\"md5\":\"063fa6f7061324eac1c4de0350c20e80\"},{\"path\":\"DXSETUP/Jun2010_d3dx9_43_x86.cab\",\"md5\":\"7749862c307e527366b6868326db8198\"},{\"path\":\"DXSETUP/Jun2010_XACT_x64.cab\",\"md5\":\"3753ba8fb15e0cb624b8cf1cf36fedb6\"},{\"path\":\"DXSETUP/Jun2010_XACT_x86.cab\",\"md5\":\"02da71bfa4764677ffcb9dcc62714418\"},{\"path\":\"DXSETUP/Jun2010_XAudio_x64.cab\",\"md5\":\"edeb828a8e54a9f3851007d80bc8dd6e\"},{\"path\":\"DXSETUP/Jun2010_XAudio_x86.cab\",\"md5\":\"9d2da3b1055120af7c2995896f5d51ed\"},{\"path\":\"DXSETUP/Mar2008_d3dx10_37_x64.cab\",\"md5\":\"fa287a98400b25f435859f8ac0d4be5b\"},{\"path\":\"DXSETUP/Mar2008_d3dx10_37_x86.cab\",\"md5\":\"3314aa4ef508511b3b5042a7b013329b\"},{\"path\":\"DXSETUP/Mar2008_d3dx9_37_x64.cab\",\"md5\":\"43ec986130c37b0f9d6e847c62e0b1c9\"},{\"path\":\"DXSETUP/Mar2008_d3dx9_37_x86.cab\",\"md5\":\"cec089e2479e50634e1cea97b279eb86\"},{\"path\":\"DXSETUP/Mar2008_X3DAudio_x64.cab\",\"md5\":\"a540e888f2dd2898373416439e6cb057\"},{\"path\":\"DXSETUP/Mar2008_X3DAudio_x86.cab\",\"md5\":\"7a575795249623dde7ed7ccd5e7f63dd\"},{\"path\":\"DXSETUP/Mar2008_XACT_x64.cab\",\"md5\":\"d80f5f61d14479f28b412123ed3b2fd7\"},{\"path\":\"DXSETUP/Mar2008_XACT_x86.cab\",\"md5\":\"b6da8563ed50c905e00dfccaf4b9585d\"},{\"path\":\"DXSETUP/Mar2008_XAudio_x64.cab\",\"md5\":\"d24d3bdbac9931b6dff3fa1401a06ad0\"},{\"path\":\"DXSETUP/Mar2008_XAudio_x86.cab\",\"md5\":\"a641fdff4a0a1f07b76bec7a33673962\"},{\"path\":\"DXSETUP/Mar2009_d3dx10_41_x64.cab\",\"md5\":\"d51867ced279f38c07eba99a3cc3f7c2\"},{\"path\":\"DXSETUP/Mar2009_d3dx10_41_x86.cab\",\"md5\":\"8993e85e24c7b0e40b32fda7c077b27b\"},{\"path\":\"DXSETUP/Mar2009_d3dx9_41_x64.cab\",\"md5\":\"446a3fef812883c260fa062873e3c111\"},{\"path\":\"DXSETUP/Mar2009_d3dx9_41_x86.cab\",\"md5\":\"cea31ebf2225612a2b5ad62eafb01875\"},{\"path\":\"DXSETUP/Mar2009_X3DAudio_x64.cab\",\"md5\":\"523bb7846e3ed4284a3fe718b3e8d529\"},{\"path\":\"DXSETUP/Mar2009_X3DAudio_x86.cab\",\"md5\":\"e6478900ab32d3a47fbb2b1ce92e1a38\"},{\"path\":\"DXSETUP/Mar2009_XACT_x64.cab\",\"md5\":\"e6849d2b74327ea210a722699962aa76\"},{\"path\":\"DXSETUP/Mar2009_XACT_x86.cab\",\"md5\":\"9643fdef5f24c96371f849208c06180a\"},{\"path\":\"DXSETUP/Mar2009_XAudio_x64.cab\",\"md5\":\"948a9bf38221e1dbec758fc10ed4799c\"},{\"path\":\"DXSETUP/Mar2009_XAudio_x86.cab\",\"md5\":\"342af931588924e6dbcb77f65ab5249c\"},{\"path\":\"DXSETUP/Nov2007_d3dx10_36_x64.cab\",\"md5\":\"dfe34e327e3bd4a4f8c81d6b0b6a4aac\"},{\"path\":\"DXSETUP/Nov2007_d3dx10_36_x86.cab\",\"md5\":\"86695c393dae55ce5d72e98e84824320\"},{\"path\":\"DXSETUP/Nov2007_d3dx9_36_x64.cab\",\"md5\":\"c0d93b6491984793d9241f12c27bfcf2\"},{\"path\":\"DXSETUP/Nov2007_d3dx9_36_x86.cab\",\"md5\":\"f6df33e7335dbd4bb9e74b88f66764cf\"},{\"path\":\"DXSETUP/NOV2007_X3DAudio_x64.cab\",\"md5\":\"a8d4c000334afb5ceef9927a176bdcae\"},{\"path\":\"DXSETUP/NOV2007_X3DAudio_x86.cab\",\"md5\":\"a2459739ca33da4cc696d051da9899a6\"},{\"path\":\"DXSETUP/NOV2007_XACT_x64.cab\",\"md5\":\"80f3a65c31cce23327fba416f80792a4\"},{\"path\":\"DXSETUP/NOV2007_XACT_x86.cab\",\"md5\":\"cb47eff7550301cae39f1cb9d7f30d74\"},{\"path\":\"DXSETUP/Nov2008_d3dx10_40_x64.cab\",\"md5\":\"e8d8cf285b1a5b6a256413188bb275cb\"},{\"path\":\"DXSETUP/Nov2008_d3dx10_40_x86.cab\",\"md5\":\"b633956d5274d9b031c8ea55b8f5701b\"},{\"path\":\"DXSETUP/Nov2008_d3dx9_40_x64.cab\",\"md5\":\"7babc8d6f8d4d044db5153a8759a583a\"},{\"path\":\"DXSETUP/Nov2008_d3dx9_40_x86.cab\",\"md5\":\"57176018dd94a20912c4424b1347dcd1\"},{\"path\":\"DXSETUP/Nov2008_X3DAudio_x64.cab\",\"md5\":\"68306f4a7426b8dcbe443d72be0b6eeb\"},{\"path\":\"DXSETUP/Nov2008_X3DAudio_x86.cab\",\"md5\":\"df373f41729508c804a94069c220de15\"},{\"path\":\"DXSETUP/Nov2008_XACT_x64.cab\",\"md5\":\"a495b57568a94b2c3df47f5a64fde631\"},{\"path\":\"DXSETUP/Nov2008_XACT_x86.cab\",\"md5\":\"abb59dd97f8964c2e8e45fbf7518b972\"},{\"path\":\"DXSETUP/Nov2008_XAudio_x64.cab\",\"md5\":\"0552fb71d30788167c1b3a1fbaa59b45\"},{\"path\":\"DXSETUP/Nov2008_XAudio_x86.cab\",\"md5\":\"795e745f1a111cbd6512c4b0360538b4\"},{\"path\":\"DXSETUP/Oct2005_xinput_x64.cab\",\"md5\":\"c39e4358cea9538ab1d4b842da669bc6\"},{\"path\":\"DXSETUP/Oct2005_xinput_x86.cab\",\"md5\":\"b296431a5dfff596fef2f04b4f36362a\"},{\"path\":\"DXSETUP/OCT2006_d3dx9_31_x64.cab\",\"md5\":\"7412c05a3cf0dbf5c1b73d34419ab5b6\"},{\"path\":\"DXSETUP/OCT2006_d3dx9_31_x86.cab\",\"md5\":\"0107ae9ddc91f279966bb74fae686ffc\"},{\"path\":\"DXSETUP/OCT2006_XACT_x64.cab\",\"md5\":\"265e3d8e5635179004742aa3af2e05cf\"},{\"path\":\"DXSETUP/OCT2006_XACT_x86.cab\",\"md5\":\"09ad0f82d92213b7d7eaa9e3b2268fe5\"}]");
                                    }});
                                }}
                        )));
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
     *      - launcher_id: Launcher id<br>
     *      - language: Language<br>
     */
    @GetMapping("getGames")
    public ResponseEntity<LinkedHashMap<String, Object>> SendGames(String launcher_id, String language) {
        if(launcher_id == null || !Config.getPropertiesVar().launcherIds.contains(launcher_id)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "launcher not found", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("games", new ArrayList<>(List.of(
                    new LinkedHashMap<String, Object>() {{
                        put("id","gopR6Cufr3");
                        put("biz", "hk4e_global");
                        put("display", new LinkedHashMap<String, Object>() {{
                            put("language", language);
                            put("name", "Genshin Impact");
                            put("icon", new LinkedHashMap<String, Object>() {{
                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/10/28/2e81b12276a2260c10133124e6ead00e_8488838804331468777.png");
                                put("hover_url", "");
                                put("link", "");
                                put("login_state_in_link", false);
                                put("md5", "");
                                put("size", 0);
                            }});
                            put("title", "");
                            put("subtitle", "");
                            put("background", new LinkedHashMap<String, Object>() {{
                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2024/05/30/a5555e5aa4f4da3d1efc3b86ef65e456_7792457472547838550.webp");
                                put("link", "");
                                put("login_state_in_link", false);
                            }});
                            put("logo", new LinkedHashMap<String, Object>() {{
                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2024/05/30/7f777384d29cac1ea0d34b4e16d4487b_2092553358310716519.png");
                                put("link", "");
                                put("login_state_in_link", false);
                            }});
                            put("thumbnail", new LinkedHashMap<String, Object>() {{
                                put("url", "https://fastcdn.hoyoverse.com/static-resource-v2/2024/05/30/68e8178e137f6b0259e4da49b5a9be8e_8412451782424407383.png");
                                put("link", "");
                                put("login_state_in_link", false);
                            }});
                            put("korea_rating", null);
                            put("shortcut", new LinkedHashMap<String, Object>() {{
                                put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/12/17/329c587494692a8e3724d10deb406844_3402263704887525846.ico");
                                put("hover_url", "");
                                put("link", "");
                                put("login_state_in_link", false);
                                put("md5", "329c587494692a8e3724d10deb406844");
                                put("size", 228875);
                            }});
                        }});
                        put("reservation", null);
                        put("display_status", "LAUNCHER_GAME_DISPLAY_STATUS_AVAILABLE");
                        put("game_server_configs", new ArrayList<>());
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("deprecated_files", new ArrayList<>());
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("version", "2.14.2");
                        put("channel_sdk_pkg", new LinkedHashMap<String, Object>() {{
                            put("url", "https://launcher-webstatic.hoyoverse.com/launcher-public/2024/05/12/12e811ff46561379abff7282a7cd8a10_1858813474598479143.zip");
                            put("md5", "12e811ff46561379abff7282a7cd8a10");
                            put("size", "9692272");
                            put("decompressed_size", "19384544");
                        }});
                        put("pkg_version_file_name", "sdk_pkg_version");
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
                            put("biz", "hk4e_global");
                        }});
                        put("exe_file_name", "GenshinImpact.exe");
                        put("installation_dir", "Genshin Impact game");
                        put("audio_pkg_scan_dir", "GenshinImpact_Data/Persistent/audio_lang_14");
                        put("audio_pkg_res_dir", "GenshinImpact_Data/StreamingAssets/AudioAssets");
                        put("audio_pkg_cache_dir", "GenshinImpact_Data/Persistent/AudioAssets");
                        put("game_cached_res_dir", "GenshinImpact_Data/Persistent");
                        put("game_screenshot_dir", "ScreenShot");
                        put("game_log_gen_dir", "%userprofile%/AppData/LocalLow/miHoYo/Genshin Impact");
                        put("game_crash_file_gen_dir", "%userprofile%/AppData/Local/Temp");
                        put("default_download_mode", "DOWNLOAD_MODE_CHUNK");
                        put("enable_customer_service", false);
                    }}
            )));
        }}));
    }

    /**
     *  Source: <a href="https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAgreementVersion">https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAgreementVersion</a><br><br>
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
     *  Method: GET<br><br>
     *  Parameters:<br><br>
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
            put("notifications", new ArrayList<>());
        }}));
    }
}