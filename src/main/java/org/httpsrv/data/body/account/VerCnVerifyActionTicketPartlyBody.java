package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class VerCnVerifyActionTicketPartlyBody {
    private String action_type;
    private String action_ticket;
    private String email_captcha;
    private Integer verify_method;
}