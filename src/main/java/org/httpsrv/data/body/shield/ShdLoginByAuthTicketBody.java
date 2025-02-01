package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdLoginByAuthTicketBody {
    private String ticket;
    private Integer app_id;
}