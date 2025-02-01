package org.httpsrv.data.body.combo;

import lombok.Getter;
@Getter
public class CbmCompareProtocolVersionBody {
    private int app_id;
    private int channel_id;
    private String language;
    private int major;
    private int minimum;
}