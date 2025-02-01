package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.List;
import lombok.Getter;

@Entity(value = "devicexts", useDiscriminator = false)
@Getter
public class DeviceExt {
    private int platform;
    private List<String> ext;
    private List<String> pkgs;
    private String pkg_str;
}