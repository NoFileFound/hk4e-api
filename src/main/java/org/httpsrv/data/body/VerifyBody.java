package org.httpsrv.data.body;

import lombok.Getter;

@Getter
public class VerifyBody {
    private String uid;
    private String token;
}