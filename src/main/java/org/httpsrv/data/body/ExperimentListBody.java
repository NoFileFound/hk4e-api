package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class ExperimentListBody {
    private String app_sign;
    private String scene_id;
    private String uid;
    private String app_id;
}