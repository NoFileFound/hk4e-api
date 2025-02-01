package org.httpsrv.data.body.account;

import lombok.Getter;

@Getter
public class DvcAckNewerDevicesBody {
    private String account_id;
    private String game_token;
    private Integer latest_id;
}