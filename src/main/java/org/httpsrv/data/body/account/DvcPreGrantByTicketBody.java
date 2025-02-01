package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class DvcPreGrantByTicketBody {
    private String action_ticket;
    private String way;
}