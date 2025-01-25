package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class BindMobileBody {
    private String area_code;
    private String captcha;
    private String mobile;
    private String ticket;
    private String uid;
}