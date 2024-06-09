package com.habanoz.duke.core.model;

import java.util.Objects;

public record ANodeMessage(Object value) implements NodeMessage {
    public ANodeMessage(Object value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public <T> T getVal() {
        return (T) value;
    }

    public Integer getInt() {
        return getVal();
    }

    public Long getLong() {
        return getVal();
    }

    public String str() {
        return getVal();
    }

    public Float getFloat() {
        return getVal();
    }

    public Double getDouble() {
        return getVal();
    }

    @Override
    public String toString() {
        return "ANodeMessage{" +
                "value=" + value +
                '}';
    }

}
