package com.stj.khaiman.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BANNED("Banned");

    private final String label;

    UserStatus(String label){
        this.label = label;
    }
}
