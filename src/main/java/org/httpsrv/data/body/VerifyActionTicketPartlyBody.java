package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class VerifyActionTicketPartlyBody {
    private String action_type;
    private String action_ticket;
    private String email_captcha;
    private Integer verify_method;
}