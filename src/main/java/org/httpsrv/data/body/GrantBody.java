package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class GrantBody {
    private String code;
    private String ticket;
}