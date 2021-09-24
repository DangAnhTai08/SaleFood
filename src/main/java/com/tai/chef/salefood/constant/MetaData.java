package com.tai.chef.salefood.constant;

import lombok.Getter;

@Getter
public enum MetaData {
    SUCCESS(200, "Success"),
    NOT_FOUND(404, "Not Found");
    private final int metaCode;
    private final String message;

    MetaData(int metaCode, String message) {
        this.metaCode = metaCode;
        this.message = message;
    }
}
