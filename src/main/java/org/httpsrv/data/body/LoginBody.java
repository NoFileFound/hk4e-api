package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class LoginBody {
    private String account;
    private Boolean is_crypto;
    private String password;
    private String game_key;
}
