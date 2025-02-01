package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import lombok.Getter;

@Entity(value = "firebase", useDiscriminator = false)
@Getter
public class FirebaseBL {
    private String firm;
    private String models;
}