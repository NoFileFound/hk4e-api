package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.util.Map;
import lombok.Getter;

@Getter
@Entity(value = "paytiers", useDiscriminator = false)
public class PayTier {
    private @Id String tier_id;
    private Map<String, Object> t_price;
}