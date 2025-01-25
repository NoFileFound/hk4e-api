package org.httpsrv.data.body;

import lombok.Getter;
public class QrCodeFetchBody {
    @Getter private Integer app_id;
    private String device;
}