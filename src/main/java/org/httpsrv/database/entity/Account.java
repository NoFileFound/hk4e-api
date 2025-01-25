package org.httpsrv.database.entity;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import org.httpsrv.algorithms.SHA256;
import org.httpsrv.database.Database;
import java.util.List;

/**
 * Collection: accounts
 */
@Entity(value = "accounts", useDiscriminator = false)
public class Account {
    @Getter @Id private String id;
    @Getter private Integer accountType;
    @Getter private String name;
    @Getter @Setter private String realname;
    @Getter private String password; /// sha256
    @Getter private String email;
    @Getter @Setter private String mobile;
    @Getter @Setter private String mobileArea;
    @Getter @Setter private String safeMobile;
    @Getter @Setter private String safeMobileArea;
    @Getter @Setter private String identityCard;
    @Getter @Setter private String currentIP;
    @Getter private FatigueRemind fatigueRemind;

    // Third parties
    @Getter private String facebookName;
    @Getter private String googleName;
    @Getter private String twitterName;
    @Getter private String gameCenterName;
    @Getter private String appleName;
    @Getter private String sonyName;
    @Getter private String tapName;
    @Getter private String steamName;
    @Getter private String cxName;

    @Getter @Setter private Boolean isEmailVerified;
    @Getter @Setter private Boolean requireDeviceGrant;
    @Getter @Setter private Boolean requireSafeMobile;
    @Getter @Setter private Boolean requireRealPerson;
    @Getter @Setter private String realPersonOperationName;
    @Getter @Setter private Boolean requireActivation;

    @Getter @Setter private String sessionKey;
    @Getter private String stokenKey;
    @Getter @Setter private String deviceId;

    public Account() {
        this.id = String.valueOf(Database.getNextId("accounts"));
        this.requireActivation = false;
        this.requireDeviceGrant = false;
        this.requireSafeMobile = false;
        this.requireRealPerson = false;
        this.realPersonOperationName = "None";
        this.fatigueRemind = null;
    }

    public Account(boolean isGuest) {
        this.id = String.valueOf(Database.getNextId("accounts"));
        this.accountType = isGuest ? 0 : 1;
        this.fatigueRemind = null;
        this.name = "Traveler";
    }

    public boolean checkAuthorizationByPassword(String password) {
        return SHA256.compareString(password, this.password);
    }

    public void save() {
        Database.saveAccountAsync(this);
    }

    public void delete() {Database.deleteAccountAsync(this);}

    @Embedded
    public static class FatigueRemind {
        private String nickname;
        private Integer reset_point;
        private List<Integer> durations;
    }
}