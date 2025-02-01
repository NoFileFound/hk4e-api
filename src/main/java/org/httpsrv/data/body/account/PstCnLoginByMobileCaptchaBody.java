package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class PstCnLoginByMobileCaptchaBody {
    private String area_code;
    private String captcha;
    private String mobile;
}