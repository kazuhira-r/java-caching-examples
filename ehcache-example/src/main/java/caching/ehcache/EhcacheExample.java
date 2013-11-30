package caching.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class EhcacheExample {
    public static void main(String[] args) {
        CacheManager cacheManager = CacheManager.newInstance(
            EhcacheExample.class.getResourceAsStream("/ehcache.xml"));
        // 設定ファイルの名前が「ehcache.xml」で、クラスパス上にある場合は以下でもOK
        // CacheManager cacheManager = CacheManager.newInstance();

        try {
            // Ehcacheの場合は、defaultの定義を元にキャッシュを作成する場合は
            // 別途Cacheのインスタンスを作成して、CacheManager#addCacheを呼び出す必要がある
            CacheConfiguration defaultCacheConfiguration
                = cacheManager.getConfiguration().getDefaultCacheConfiguration().clone();

            defaultCacheConfiguration.setName("defaultCache");
            cacheManager.addCache(new Cache(defaultCacheConfiguration));

            Cache defaultCache = cacheManager.getCache("defaultCache");

            // 個別に設定したcache
            Cache cache = cacheManager.getCache("memoryCache");

            // キャッシュへのへのエントリの登録
            Element putEntry = new Element("key1", "value1");
            cache.put(putEntry);
            
            cache.put(new Element("key2", "value2"));

            // キャッシュからのエントリの取得
            Element getElement = cache.get("key1");
            String getValue = (String) getElement.getObjectValue();
            System.out.println(getValue);

            // キャッシュからのエントリの削除
            cache.remove("key1");

            // キャッシュからのエントリの全削除
            cache.removeAll();

            // Expireの確認
            cache.put(new Element("key3", "value3"));
            cache.put(new Element("key4", "value4"));

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // ひとつだけ、アクセス
            System.out.println(cache.get("key3") != null);

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // アクセスしたエントリのみ、キャッシュに残ったまま
            System.out.println(cache.get("key3") != null);
            System.out.println(cache.get("key4") == null);

            try { Thread.sleep(7 * 1000L); } catch (InterruptedException e) { }

            // もうひとつのエントリも、有効期限が切れる
            System.out.println(cache.get("key3") == null);
            System.out.println(cache.get("key4") == null);
        } finally {
            cacheManager.shutdown();
        }
    }
}