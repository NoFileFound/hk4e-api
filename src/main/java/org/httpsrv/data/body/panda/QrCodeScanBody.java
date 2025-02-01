package org.httpsrv.data.body.panda;

import lombok.Getter;

@Getter
public class QrCodeScanBody {
    private Integer app_id;
    private String ticket;
    private String device;
}