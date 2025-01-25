package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class ActionTicketBody {
    private String account_id;
    private String action_type;
    private String game_token;
}