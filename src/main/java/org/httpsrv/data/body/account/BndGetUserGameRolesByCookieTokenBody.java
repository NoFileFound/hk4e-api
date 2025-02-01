package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class BndGetUserGameRolesByCookieTokenBody {
    private String t;
    private String game_biz;
    private String region;
}