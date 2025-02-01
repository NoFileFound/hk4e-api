package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdLoginBody {
    private String account;
    private Boolean is_crypto;
    private String password;
    private String game_key;
}