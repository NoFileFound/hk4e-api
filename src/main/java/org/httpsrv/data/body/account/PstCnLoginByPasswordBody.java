package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class PstCnLoginByPasswordBody {
    private String account;
    private String password;
}