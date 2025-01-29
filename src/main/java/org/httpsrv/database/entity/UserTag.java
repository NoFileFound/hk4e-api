package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.httpsrv.database.Database;

@Entity(value = "usertags", useDiscriminator = false)
@Getter
public class UserTag {
    private String alias;
    private String region;
    private List<String> add_tags;
    private List<String> remove_tags;
    @Setter private Long lastUpdateTs;

    public void save() {
        Database.saveAccountAsync(this);
    }

    public void delete() {
        Database.deleteAccountAsync(this);
    }
}