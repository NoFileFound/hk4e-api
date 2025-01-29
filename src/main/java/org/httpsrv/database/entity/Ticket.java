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
@Entity(value = "tickets", useDiscriminator = false)
public class Ticket {
    @Getter @Setter @Id private String id;
    @Getter private String email;
    private @Setter String type;
    private String author;
    private String reason;
    @Getter private String mobile;
    @Getter @Setter private String code;
    @Getter @Setter private Long time; // for qr
    @Getter @Setter private Integer state; // for qr
    @Getter @Setter private boolean isVerified;

    /// bind, devide grant
    public Ticket(String email, String type, String author, String reason, boolean useMobile) {
        if(useMobile) {
            this.mobile = email;
        }
        else {
            this.email = email;
        }
        this.id = Random.generateStr(10);
        this.type = type;
        this.author = author;
        this.reason = reason;
    }

    /// qr login
    public Ticket(String type, Long time) {
        this.id = Random.generateStr(10);
        this.type = type;
        this.time = time;
        this.author = "System";
        this.reason = "login with qr code";
        this.state = 0;
    }

    public void save() {
        Database.saveAccountAsync(this);
    }

    public void delete() {
        Database.deleteAccountAsync(this);
    }
}