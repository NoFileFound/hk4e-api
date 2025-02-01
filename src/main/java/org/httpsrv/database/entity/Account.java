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
@Getter
@Entity(value = "accounts", useDiscriminator = false)
public class Account {
    @Id private String id;
    private Integer accountType;
    private String name;
    @Setter private String realname;
    private String password; /// sha256
    @Setter private String email;
    @Setter private String mobile;
    @Setter private String mobileArea;
    @Setter private String safeMobile;
    @Setter private String safeMobileArea;
    @Setter private String identityCard;
    @Setter private String currentIP;
    @Setter private String currentDeviceId;
    private FatigueRemind fatigueRemind;
    private Goods goods;

    // Third parties
    private String facebookName;
    private String googleName;
    private String twitterName;
    private String gameCenterName;
    private String appleName;
    private String sonyName;
    private String tapName;
    private String steamName;
    private String cxName;

    @Setter private Boolean isEmailVerified;
    @Setter private Boolean requireDeviceGrant;
    @Setter private Boolean requireSafeMobile;
    @Setter private Boolean requireRealPerson;
    @Setter private String realPersonOperationName;
    @Setter private Boolean requireActivation;

    @Setter private String sessionKey;
    private String stokenKey;
    private List<String> deviceIds;

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
        Database.saveObjectAsync(this);
    }

    public void delete() {Database.deleteObjectAsync(this);}

    @Embedded
    public static class FatigueRemind {
        private String nickname;
        private Integer reset_point;
        private List<Integer> durations;
    }

    @Embedded
    public static class Goods {
        @Id private String goods_id;
        private String goods_name;
        private String goods_name_i18n_key;
        private String goods_desc;
        private String goods_desc_i18n_key;
        private String goods_type; /// Normal, MonthlyCard
        private String goods_unit;
        private String goods_icon;
        private String currency;
        private String price;
        private String symbol;
        private String tier_id;
        private Object bonus_desc;
        private Object once_bonus_desc;
        private boolean available;
        private String tips_desc;
        private String tips_i18n_key;
        private String battle_pass_limit;
    }
}