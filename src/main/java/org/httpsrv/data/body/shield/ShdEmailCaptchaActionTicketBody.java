package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdEmailCaptchaActionTicketBody {
    private String action_ticket;
    private String action_type;
}