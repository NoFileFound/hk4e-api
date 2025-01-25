package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class ModifyRealnameBody {
    private String action_ticket;
    private String realname;
    private String identity_card;
}