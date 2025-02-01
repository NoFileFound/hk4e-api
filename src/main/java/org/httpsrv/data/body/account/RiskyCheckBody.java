package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class RiskyCheckBody {
    private String action_type;
    private String api_name;
    private String username;
    private String mobile;
}