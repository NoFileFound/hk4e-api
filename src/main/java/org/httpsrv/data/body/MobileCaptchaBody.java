package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class MobileCaptchaBody {
    private String action_ticket;
    private String action_type;
    private String mobile;
    private String area_code;
    private Boolean safe_mobile;
}