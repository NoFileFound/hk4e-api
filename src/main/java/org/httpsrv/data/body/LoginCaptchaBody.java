package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class LoginCaptchaBody {
    private String area;
    private String mobile;
}