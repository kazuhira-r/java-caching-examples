package caching.infinispan;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class InfinispanExample {
    public static void main(String[] args) {
        EmbeddedCacheManager manager = null;
        Cache<String, String> defaultCache = null;
        Cache<String, String> smallCache = null;

        try {
            manager = new DefaultCacheManager("infinispan.xml");

            // defaultの定義を元に作成した、Cacheのインスタンス
            defaultCache = manager.getCache();

            // 個別に設定したcache
            smallCache = manager.getCache("smallCache");

            // キャッシュへのへのエントリの登録
            smallCache.put("key1", "value1");
            smallCache.put("key2", "value2");

            // キャッシュからのエントリの取得
            String value = smallCache.get("key1");
            System.out.println(value);

            // キャッシュからのエントリの削除
            smallCache.remove("key1");

            // キャッシュからのエントリの全削除
            smallCache.clear();

            // Expireの確認
            smallCache.put("key3", "value3");
            smallCache.put("key4", "value4");

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // ひとつだけ、アクセス
            System.out.println(smallCache.get("key3") != null);

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // アクセスしたエントリのみ、キャッシュに残ったまま
            System.out.println(smallCache.get("key3") != null);
            System.out.println(smallCache.get("key4") == null);

            try { Thread.sleep(7 * 1000L); } catch (InterruptedException e) { }

            // もうひとつのエントリも、有効期限が切れる
            System.out.println(smallCache.get("key3") == null);
            System.out.println(smallCache.get("key4") == null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (defaultCache != null) {
                defaultCache.stop();
            }

            if (smallCache != null) {
                smallCache.stop();
            }

            if (manager != null) {
                manager.stop();
            }
        }
    }
}