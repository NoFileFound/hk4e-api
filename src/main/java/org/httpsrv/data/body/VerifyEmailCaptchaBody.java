package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class VerifyEmailCaptchaBody {
    private String action_ticket;
    private String action_type;
    private String captcha;
}