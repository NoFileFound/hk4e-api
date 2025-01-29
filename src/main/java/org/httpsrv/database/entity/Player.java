package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;

@Entity(value = "players", useDiscriminator = false)
@Getter
public class Player {
    @Id private String id;
    private String accountId;
    private int level;
    private String playerName;
    private String regionId;
    private String regionName;
}