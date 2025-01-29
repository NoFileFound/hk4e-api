package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.Map;
import lombok.Getter;

@Entity(value = "launchexperiments", useDiscriminator = false)
@Getter
public class LauncherExperiment {
    private Integer code;
    private Integer type;
    private String config_id;
    private String period_id;
    private String version;
    private Map<String, String> configs;
}