package org.httpsrv.data;

import lombok.Getter;

@Getter
public enum ApplicationId {
    GENSHIN_RELEASE(4),
    GENSHIN_SANDBOX_OVERSEAS(9),
    GENSHIN_SANDBOX_CHINA(10),
    HYP_LAUNCHER(20);

    private final int value;
    ApplicationId(int value) {
        this.value = value;
    }
}