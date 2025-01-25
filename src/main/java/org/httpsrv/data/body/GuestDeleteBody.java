package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class GuestDeleteBody {
    private String game_key;
    private String device_id;
    private String sign;
    private String guest_id;
}