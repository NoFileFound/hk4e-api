package org.httpsrv.data.body;

import lombok.Getter;
@Getter
public class CompareProtocolVersionBody {
    private int app_id;
    private int channel_id;
    private String language;
    private int major;
    private int minimum;
}