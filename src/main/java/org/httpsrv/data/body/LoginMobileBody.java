package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class LoginMobileBody {
    private String action;
    private String area;
    private String captcha;
    private String mobile;
}