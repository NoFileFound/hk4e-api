package org.httpsrv.data.body.shield;

import lombok.Getter;

@Getter
public class ShdActionTicketBody {
    private String account_id;
    private String action_type;
    private String game_token;
}