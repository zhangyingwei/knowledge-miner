package com.zhangyingwei.spiders.konwledge.cache;

import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:29
 * @desc:
 */
public class CacheMap {

    @Getter
    private Map<String, List<Konwledge>> cache;

    public CacheMap() {
        this.cache = new ConcurrentHashMap<String,List<Konwledge>>();
    }

    private static class CacheMapHandler{
        static CacheMap ins = new CacheMap();
    }

    public static CacheMap getIns() {
        return CacheMapHandler.ins;
    }

    public synchronized void put(String key,Konwledge value) {
        List<Konwledge> konwledges = Optional.ofNullable(this.cache.get(key)).orElse(new ArrayList<Konwledge>());
        konwledges.add(value);
        cache.put(key, konwledges);
    }

    public List<Konwledge> get(String key) {
        return this.cache.get(key);
    }
}
