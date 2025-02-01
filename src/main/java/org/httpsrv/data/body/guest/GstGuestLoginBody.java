package org.httpsrv.data.body.guest;

import lombok.Getter;

@Getter
public class GstGuestLoginBody {
    private String game_key;
    private Integer client;
    private String device;
    private String sign;
    private String g_version;
}