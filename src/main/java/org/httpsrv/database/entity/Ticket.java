package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import org.httpsrv.algorithms.Random;
import org.httpsrv.database.Database;

/**
 * Collection: tickets
 */
@Getter
@Entity(value = "tickets", useDiscriminator = false)
public class Ticket {
    @Setter @Id private String id;
    @Setter private String accountId;
    @Setter private String type;
    @Setter private String code;
    @Setter private Long time; // for qr
    @Setter private Integer state; // for qr
    @Setter private boolean isVerified;
    private String createdAt;
    @Setter private String modifiedAt;

    public Ticket(String accountId, String type) {
        this.id = Random.generateStr(10);
        this.accountId = accountId;
        this.type = type;
    }

    /// qr login
    public Ticket(String type, Long time) {
        this.id = Random.generateStr(10);
        this.type = type;
        this.time = time;
        this.state = 0;
        this.createdAt = String.valueOf(System.currentTimeMillis() / 1000);
    }

    public void save() {
        Database.saveObjectAsync(this);
    }

    public void delete() {
        Database.deleteObjectAsync(this);
    }
}