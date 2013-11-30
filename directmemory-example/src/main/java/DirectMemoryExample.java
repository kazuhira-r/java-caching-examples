import java.io.IOException;

import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.apache.directmemory.measures.Ram;

public class DirectMemoryExample {
    public static void main(String[] args) {
        try (CacheService<String, String> cacheService =
             new DirectMemory<String, String>()
             .setNumberOfBuffers(10)
             .setInitialCapacity(100000)
             // キャッシュサイズを10MBに
             // この設定を入れないと、保存されない！！
             .setSize(Ram.Mb(10))
             .setConcurrencyLevel(4)
             .newCacheService()) {

            // キャッシュへのエントリ追加
            cacheService.put("key1", "value1");
            cacheService.put("key2", "value2");

            // キャッシュのエントリ数
            System.out.println("entries = " + cacheService.entries());

            // キャッシュからのエントリ取得
            String value1 = cacheService.retrieve("key1");
            System.out.println("key1 = " + value1);

            // キャッシュからのエントリ全削除
            cacheService.clear();

            // キャッシュのエントリ数
            System.out.println("entries = " + cacheService.entries());

            // Expireの確認
            cacheService.put("key3", "value3", 3 * 1000);
            cacheService.put("key4", "value4", 3 * 1000);

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // キャッシュのエントリ数
            System.out.println("entries = " + cacheService.entries());

            // ひとつだけアクセス
            System.out.println("key3 = " + cacheService.retrieve("key3"));

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // アクセスする、しないに関わらず、両方共有効期限切れになる
            // ＊アクセスによる、有効期限の延長は行われない
            System.out.println("key3 = " + cacheService.retrieve("key3"));
            System.out.println("key4 = " + cacheService.retrieve("key4"));

            // キャッシュのエントリ数
            System.out.println("entries = " + cacheService.entries());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}