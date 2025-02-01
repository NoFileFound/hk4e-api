package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class PstAppLoginByPasswordBody {
    private String account;
    private String password;
}