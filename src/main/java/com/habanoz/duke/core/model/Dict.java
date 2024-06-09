package com.habanoz.duke.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Dict(Map<String, Object> map) implements NodeMessage {

    public static Dict map(Map<String, Object> map) {
        Objects.requireNonNull(map, "Map cannot be Null");

        var dict = new Dict(new HashMap<>());
        dict.map.putAll(map);

        return dict;
    }

    public static Dict map(Object... args) {
        Objects.requireNonNull(args, "Map args cannot be Null");

        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments must be even");
        }

        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put((String) args[i], args[i + 1]);
        }

        return Dict.map(map);
    }

    public Dict extend(String key, Object value) {
        Dict dict = new Dict(new HashMap<>());
        dict.map.putAll(map);
        dict.map.put(key, value);
        return dict;
    }

    public <T> T getVal(String key) {
        return (T) map.get(key);
    }

    public Map<String, Object> map() {
        return Collections.unmodifiableMap(map);
    }

    public Integer getInt(String key) {
        return (Integer) map.get(key);
    }

    public Long getLong(String key) {
        return (Long) map.get(key);
    }

    public String str(String key) {
        return (String) map.get(key);
    }

    public Float getFloat(String key) {
        return (Float) map.get(key);
    }

    public Double getDouble(String key) {
        return (Double) map.get(key);
    }

    @Override
    public String toString() {
        return "Dict{" +
                "map=" + map +
                '}';
    }

}
