import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheOffHeapExample {
    public static void main(String[] args) {
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache("offHeapCache");

        cache.put(new Element("key1", "value1"));

        System.out.println("key1 = " + cache.get("key1").getObjectValue());

        stats(cache);
   
        cacheManager.shutdown();
    }

    private static void stats(Ehcache ehcache) {
        System.out.println( "OnHeapSize=" + ehcache.calculateInMemorySize() + ", OnHeapElements="
                            + ehcache.getMemoryStoreSize() );
        System.out.println( "OffHeapSize=" + ehcache.calculateOffHeapSize() + ", OffHeapElements="
                            + ehcache.getOffHeapStoreSize() );
        System.out.println( "DiskStoreSize=" + ehcache.calculateOnDiskSize() + ", DiskStoreElements="
                            + ehcache.getDiskStoreSize() );
    }
}