package com.ajit.bjp.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by o8428 on 29-07-2017.
 */

public enum AppCache {

    INSTANCE;


    Map<String, Object> appCache = new ConcurrentHashMap<>();

/*   private AppCache() {
    }

    public static synchronized AppCache getInstance() {

        if (AppCache.fabDataStore == null) {
            AppCache.fabDataStore = new AppCache();
        }

        return AppCache.fabDataStore;
    }
*/

    /**
     * This will allow to set shared variable
     */
    public void addToAppCache(final String key, final Object value) {
        if (appCache != null) {
            setData(key, value);
        }
    }

    /**
     * This will give the data mapped with particular key
     */
    public Object getValueOfAppCache(final String key) {
        if (appCache != null) {
            if (getData() != null) {
                return getData().get(key);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * This function clear cache, and will retain the values mentioned in retainData()
     */


    /**
     * Set the data in the hashmap
     *
     * @param className class name where data is located
     * @param data      actual data returned from webservice
     */
    public void setData(final String className, final Object data) {
        if(data != null) {
            appCache.put(className, data);
        }
    }

    /**
     * Thsi method return the data stored in hashmap
     *
     * @return data Actual data
     */
    public Map<String, Object> getData() {
        return appCache;

    }

    /**
     * This function clear whole cache.
     */
    public void clearCache() {
        if (appCache != null) {
            appCache.clear();
        }
    }

    /**
     * This will delete cache object by sending key
     */
    public void deleteCacheObject(final String key) {
        if (appCache != null) {
            appCache.remove(key);
        }
    }


}
