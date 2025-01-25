package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class GuestLoginBody {
    private String game_key;
    private Integer client;
    private String device;
    private String sign;
    private String g_version;
}