package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class AthModifyRealnameBody {
    private String action_ticket;
    private String realname;
    private String identity_card;
    private Boolean is_crypto;
}