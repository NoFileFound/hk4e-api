package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class DvcGrantBody {
    private String code;
    private String ticket;
}