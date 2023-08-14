package org.xiaobai.core.utils;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xdf
 */
@Service
public class CacheUtil<T> {

    private final Map<String, T> cacheMap = new ConcurrentHashMap<>();

    public T put(String key, T value) {
        return cacheMap.put(key, value);
    }

    public T get(String key) {
        return cacheMap.get(key);
    }

    public T remove(String key) {
        return cacheMap.remove(key);
    }
}
