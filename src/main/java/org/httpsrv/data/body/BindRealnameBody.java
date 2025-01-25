package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class BindRealnameBody {
    private String action_ticket;
    private String realname;
    private String identity_card;
    private Boolean is_crypto;
}