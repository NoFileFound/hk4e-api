package org.httpsrv.data.body.web;

import lombok.Getter;

@Getter
public class WebUpdateUserMarketingStatusBody {
    private String entity;
    private Integer marketing_status;
}