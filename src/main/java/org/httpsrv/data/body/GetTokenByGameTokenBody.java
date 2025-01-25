package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class GetTokenByGameTokenBody {
    private String account_id;
    private String game_token;
}