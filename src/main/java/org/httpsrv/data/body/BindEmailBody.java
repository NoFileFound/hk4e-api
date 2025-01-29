package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class BindEmailBody {
    private String action_ticket;
    private String email;
}