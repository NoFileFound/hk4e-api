package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class EmailCaptchaBody {
    private String action_ticket;
    private String action_type;
    private String email;
}