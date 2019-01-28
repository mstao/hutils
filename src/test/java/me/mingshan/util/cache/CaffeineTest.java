package me.mingshan.util.cache;

import me.mingshan.util.cache.caffeine.CaffeineCache;
import org.junit.Assert;
import org.junit.Test;

public class CaffeineTest {

    @Test
    public void test1() {
       Cache cache = new CaffeineCache();
        Object result1 = cache.putIfPresent("zz", "66");
        Assert.assertNull(result1);

        Object result2 = cache.putIfPresent("zz", "88");
        Assert.assertEquals("66", result2);
    }
}
