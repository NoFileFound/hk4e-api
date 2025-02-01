package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class AthBindMobileBody {
    private String area_code;
    private String captcha;
    private String mobile;
    private String ticket;
    private String uid;
}