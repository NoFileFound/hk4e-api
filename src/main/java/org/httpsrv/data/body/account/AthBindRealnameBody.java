package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class AthBindRealnameBody {
    private String ticket;
    private String realname;
    private String identity;
    private Boolean is_crypto;
    private String game_biz;
}