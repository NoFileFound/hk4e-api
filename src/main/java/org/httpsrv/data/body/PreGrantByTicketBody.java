package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class PreGrantByTicketBody {
    private String action_ticket;
    private String way;
}