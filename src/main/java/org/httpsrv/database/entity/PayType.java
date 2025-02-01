package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@Entity(value = "paytypes", useDiscriminator = false)
public class PayType {
    private String pay_type;
    private String display_name;
    private String icon_url;
    private List<Map<String, Object>> pay_vendors;
}