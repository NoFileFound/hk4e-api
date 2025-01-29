package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.Map;
import lombok.Getter;

@Entity(value = "experiments", useDiscriminator = false)
@Getter
public class Experiment {
    private Integer code;
    private Integer type;
    private String config_id;
    private String period_id;
    private String version;
    private Map<String, String> configs;
    private Boolean sceneWhiteList;
    private Boolean experimentWhiteList;
}