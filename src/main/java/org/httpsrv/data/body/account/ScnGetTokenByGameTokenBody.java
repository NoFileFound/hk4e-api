package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class ScnGetTokenByGameTokenBody {
    private String account_id;
    private String game_token;
}