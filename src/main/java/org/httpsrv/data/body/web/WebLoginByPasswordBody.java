package org.httpsrv.data.body.web;

import lombok.Getter;

@Getter
public class WebLoginByPasswordBody {
    private String account;
    private String password;
    private Integer token_type;
}