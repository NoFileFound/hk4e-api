package org.httpsrv.data.body.guest;

import lombok.Getter;

@Getter
public class GstGuestDeleteBody {
    private String game_key;
    private String device_id;
    private String sign;
    private String guest_id;
}