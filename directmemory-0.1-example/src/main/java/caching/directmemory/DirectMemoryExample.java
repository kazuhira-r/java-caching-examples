package caching.directmemory;

import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.apache.directmemory.measures.Ram;
import org.apache.directmemory.memory.Pointer;

public class DirectMemoryExample {
    public static void main(String[] args) {
        CacheService<String, String> cache =
            new DirectMemory<String, String>()
            .setNumberOfBuffers(4)
            .setInitialCapacity(100)
            .setSize(Ram.Mb(1))
            .setDisposalTime(3)  // 有効期限の切れたエントリのチェック間隔
            .newCacheService();

        // cache.scheduleDisposalEvery(1000L);

        // キャッシュへのへのエントリの登録
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // キャッシュからのエントリの取得
        String value = cache.retrieve("key1");
        System.out.println(value);

        // キャッシュからのエントリの全削除
        cache.clear();

        // Expireの確認
        cache.put("key3", "value3", 3 * 1000);  // 有効期限を設定したい場合は、第3引数に指定する
        cache.put("key4", "value4", 3 * 1000);

        try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

        // ひとつだけ、アクセス
        System.out.println(cache.retrieve("key3") != null);

        try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

        // アクセスする、しないに関わらず、両方共有効期限切れになります
        // ＊アクセスによる、有効期限の延長は行われない
        System.out.println(cache.retrieve("key3") == null);
        System.out.println(cache.retrieve("key4") == null);

        // 正しい終了の方法がわかりません…
        // 0.2では、CacheServiceにcloseメソッドが入っているようですが…
        System.exit(0);
    }
}