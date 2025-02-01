package org.httpsrv.data.body.panda;

import lombok.Getter;
public class QrCodeFetchBody {
    @Getter private Integer app_id;
    private String device;
}