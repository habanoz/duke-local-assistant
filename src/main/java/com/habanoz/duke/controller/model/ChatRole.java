package com.habanoz.duke.controller.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChatRole {
    USER, ASSISTANT;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
