package com.sap.ewm.core.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: ewm
 * @description: 快取工具類
 * @author: syngna
 * @create: 2020-06-28 16:33
 */
public class CacheUtil {

    // 快取map
    private static Map<String, Object> cacheMap = new HashMap<String, Object>();
    // 快取有效期map
    private static Map<String, Long> expireTimeMap = new HashMap<String, Long>();


    /**
     * 獲取指定的value，如果key不存在或者已過期，則返回null
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        if (expireTimeMap.containsKey(key)) {
            if (expireTimeMap.get(key) < System.currentTimeMillis()) { // 快取失效，已過期
                return null;
            }
        }
        return cacheMap.get(key);
    }

    /**
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getT(String key) {
        Object obj = get(key);
        return obj == null ? null : (T) obj;
    }

    /**
     * 設定value（不過期）
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        cacheMap.put(key, value);
    }

    /**
     * 設定value
     *
     * @param key
     * @param value
     * @param millSeconds 過期時間（毫秒）
     */
    public static void set(final String key, Object value, int millSeconds) {
        final long expireTime = System.currentTimeMillis() + millSeconds;
        cacheMap.put(key, value);
        expireTimeMap.put(key, expireTime);
        if (cacheMap.size() > 2) { // 清除過期數據
            new Thread(new Runnable() {
                public void run() {
                    // 此處若使用foreach進行循環遍歷，刪除過期數據，會拋出java.util.ConcurrentModificationException異常
                    Iterator<Map.Entry<String, Object>> iterator = cacheMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();
                        if (expireTimeMap.containsKey(entry.getKey())) {
                            long expireTime = expireTimeMap.get(key);
                            if (System.currentTimeMillis() > expireTime) {
                                iterator.remove();
                                expireTimeMap.remove(entry.getKey());
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public static boolean isExist(String key) {
        return cacheMap.containsKey(key);
    }

    /**
     * 移除快取
     * @param key
     */
    public static void remove(String key) {
        if(isExist(key)) {
            cacheMap.remove(key);
        }
    }

    public static void removeByPerfix(String perfix) {
        cacheMap.keySet().removeIf(key -> key.startsWith(perfix));
    }


    public static void main(String[] args) {
        CacheUtil.set("testKey_1", "testValue_1");
        CacheUtil.set("testKey_2", "testValue_2", 1);
        CacheUtil.set("testKey_3", "testValue_3");
        CacheUtil.set("testKey_4", "testValue_4", 1);
        Object testKey_2 = CacheUtil.get("testKey_1");
        System.out.println(testKey_2);
    }

}
  