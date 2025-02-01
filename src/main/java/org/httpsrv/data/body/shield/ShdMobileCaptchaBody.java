package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdMobileCaptchaBody {
    private String action_ticket;
    private String action_type;
    private String mobile;
    private String area_code;
    private Boolean safe_mobile;
}