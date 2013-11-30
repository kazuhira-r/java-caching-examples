package caching.guava;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCacheExample {
    public static void main(String[] args) {
        Cache<String, String> cache =
            CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(3, TimeUnit.SECONDS)
            .expireAfterWrite(6, TimeUnit.SECONDS)
            .build();

        // 上記設定を、文字列で指定することも可能
        // String spec = "maximumSize=100,expireAfterAccess=3s,expireAfterWrite=6s";
        // Cache<String, String> cache = CacheBuilder.from(spec).build();

        // キャッシュへのへのエントリの登録
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // キャッシュからのエントリの取得
        String value = cache.getIfPresent("key1");
        System.out.println(value);

        // キャッシュからのエントリの削除
        cache.invalidate("key1");

        // キャッシュからのエントリの全削除
        cache.invalidateAll();

        // Expireの確認
        cache.put("key3", "value3");
        cache.put("key4", "value4");

        try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

        // ひとつだけ、アクセス
        System.out.println(cache.getIfPresent("key3") != null);

        try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

        // アクセスしたエントリのみ、キャッシュに残ったまま
        System.out.println(cache.getIfPresent("key3") != null);
        System.out.println(cache.getIfPresent("key4") == null);

        try { Thread.sleep(7 * 1000L); } catch (InterruptedException e) { }

        // もうひとつのエントリも、有効期限が切れる
        System.out.println(cache.getIfPresent("key3") == null);
        System.out.println(cache.getIfPresent("key4") == null);
    }
}