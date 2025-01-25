package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class QrCodeQueryBody {
    private Integer app_id;
    private String ticket;
    private String device;
}