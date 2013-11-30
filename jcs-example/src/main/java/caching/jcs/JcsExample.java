package caching.jcs;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class JcsExample {
    public static void main(String[] args) {
        try {
            // デフォルトの定義を元に作成した、JCSのインスタンス 
            JCS defaultJcs = JCS.getInstance("default");
            // 個別に設定したJCSのインスタンス
            JCS smallJcs = JCS.getInstance("small");

            // キャッシュへのへのエントリの登録
            smallJcs.put("key1", "value1");
            smallJcs.put("key2", "value2");

            // キャッシュからのエントリの取得
            String value = (String) smallJcs.get("key1");
            System.out.println(value);

            // キャッシュからのエントリの削除
            smallJcs.remove("key1");

            // キャッシュからのエントリの全削除
            smallJcs.clear();

            // Expireの確認
            smallJcs.put("key3", "value3");
            smallJcs.put("key4", "value4");

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // ひとつだけ、アクセス
            System.out.println(smallJcs.get("key3") != null);

            try { Thread.sleep(2 * 1000L); } catch (InterruptedException e) { }

            // アクセスしたエントリのみ、キャッシュに残ったまま
            System.out.println(smallJcs.get("key3") != null);
            System.out.println(smallJcs.get("key4") == null);

            try { Thread.sleep(7 * 1000L); } catch (InterruptedException e) { }

            // もうひとつのエントリも、有効期限が切れる
            System.out.println(smallJcs.get("key3") == null);
            System.out.println(smallJcs.get("key4") == null);

            defaultJcs.dispose();
            smallJcs.dispose();
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }
}