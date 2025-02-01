package org.httpsrv.data.body.panda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class QrCodeConfirmBody {
    private Integer app_id;
    private String ticket;
    @JsonProperty("proto")
    private String payload;
    private String device;
}