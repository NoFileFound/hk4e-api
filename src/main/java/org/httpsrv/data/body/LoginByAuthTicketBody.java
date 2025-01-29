package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class LoginByAuthTicketBody {
    private String ticket;
    private Integer app_id;
}