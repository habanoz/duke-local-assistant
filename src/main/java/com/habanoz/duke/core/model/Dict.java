package com.habanoz.duke.core.model;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.*;

public final class Dict {
    private static final String KEY_SINGULAR = "$singular$";
    private final boolean isSingleton;
    private final HashMap<String, Object> map = new HashMap<>();

    public Dict(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }

    public Dict() {
        this(false);
    }

    public static Dict sin(Object value) {
        Objects.requireNonNull(value, "Singleton value cannot be Null");

        var dict = new Dict(true);
        dict.map.put(KEY_SINGULAR, value);
        return dict;
    }

    public static Dict map(Map<String, Object> map) {
        Objects.requireNonNull(map, "Map cannot be Null");

        var dict = new Dict();
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
        Dict dict = new Dict();
        dict.map.putAll(map);
        dict.map.put(key, value);
        return dict;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public Set<Map.Entry<String, Object>> items() {
        return map.entrySet();
    }

    public <T> T getVal() {
        if (!isSingleton) throw new IllegalStateException("Dict is not singular!");
        return (T) map.get(KEY_SINGULAR);
    }

    public <T> T getVal(String key) {
        return (T) map.get(key);
    }

    public Integer getInt() {
        return getVal();
    }

    public Prompt getPrompt() {
        return getVal();
    }

    public ChatResponse getChatResponse() {
        return getVal();
    }

    public Map<String, Object> map() {
        return Collections.unmodifiableMap(map);
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
