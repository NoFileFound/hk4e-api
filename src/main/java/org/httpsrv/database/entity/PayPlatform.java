package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@Entity(value = "payplatforms", useDiscriminator = false)
public class PayPlatform {
    private String currency;
    private String name;
    private String icon_urls;
    private String redirect_url;
    private Boolean enable;
    private List<Map<Object, Object>> pay_type;
    private final Object event = null;
}