package org.httpsrv.data.body;

import lombok.Getter;

public class GetFpBody {
    private String device_id;
    private String seed_id;
    private String seed_time;
    private String platform;
    @Getter private String device_fp;
    private String app_name;
    private String ext_fields;
}