package com.surpassun.cash.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {

	private static Map<String, Object> cache = new HashMap<String, Object>();
	
	public static void putCache(String key, Object value) {
		cache.put(key, value);
	}
	
	public static Object getCache(String key) {
		return cache.get(key);
	}
	
	public static void removeCache(String key) {
		cache.remove(key);
	}
	
	public static void clearCache() {
		cache.clear();
	}
}
