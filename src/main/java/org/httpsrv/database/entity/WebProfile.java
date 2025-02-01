package org.httpsrv.database.entity;

import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import org.httpsrv.database.Database;

import java.util.List;
import java.util.Map;

@Getter
public class WebProfile {
    private @Id String Id;
    private String accountId;
    private String username;
    private String ltoken;
    @Setter private String cookieToken;
    @Setter private String ipAddress;
    private String email;
    private String mobile;
    private List<Map<String, Object>> devices;
    private String googleName;
    private String facebookName;
    private String appleName;
    private String GCName;
    private String twitterName;
    private String sonyName; // ps4
    private String xboxName; // microsoft

    // in-game variables
    private String gameNickname;
    private Integer gameLevel;
    @Setter private Boolean enableEmailNotifications;

    public void save() {
        Database.saveObjectAsync(this);
    }

    public void delete() {Database.deleteObjectAsync(this);}
}