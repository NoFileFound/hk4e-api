package org.httpsrv.data.body.granter;

import lombok.Getter;

@Getter
public class GntLoginV2Body {
    private Integer app_id;
    private Integer channel_id;
    private String data;
    private String device;
    private String sign;
}