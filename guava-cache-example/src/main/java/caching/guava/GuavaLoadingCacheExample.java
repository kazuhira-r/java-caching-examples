package caching.guava;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class GuavaLoadingCacheExample {
    private static String getValue(String k) {
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "value" + k.substring(k.length() - 1);
    }

    public static void main(String[] args) {
        LoadingCache<String, String> cache =
            CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(3, TimeUnit.SECONDS)
            .expireAfterWrite(6, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                    public String load(String key) throws Exception {
                        System.out.println("Load Key => " + key);
                        return getValue(key);
                    }
                });

        // キャッシュへのエントリのロードと取得を、同時に行う
        try {
            String value1 = cache.get("key1");
            System.out.println(value1);
            String value2 = cache.get("key2");
            System.out.println(value2);

            value1 = cache.get("key1");
            System.out.println(value1);
            value2 = cache.get("key2");
            System.out.println(value2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}