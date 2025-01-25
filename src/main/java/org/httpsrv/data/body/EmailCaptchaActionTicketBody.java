package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class EmailCaptchaActionTicketBody {
    private String action_ticket;
    private String action_type;
}