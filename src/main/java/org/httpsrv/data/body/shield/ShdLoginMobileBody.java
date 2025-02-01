package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdLoginMobileBody {
    private String action;
    private String area;
    private String captcha;
    private String mobile;
}