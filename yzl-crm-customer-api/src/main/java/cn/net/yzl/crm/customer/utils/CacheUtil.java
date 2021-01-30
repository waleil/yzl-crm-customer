package cn.net.yzl.crm.customer.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 GUAUA
 * wangzhe
 * 2021-01-29
 */
@Slf4j
public class CacheUtil {

    private final static Integer INITIAL_CAPACITY = 1000;
    private final static Integer MAXIMUM_SIZE = 10_000;
    private final static Integer DURATION = 5;

    /**
     * 声明一个静态的内存块
     * initialCapacity(1000) 设置缓存的初始化容量为1000
     * maximumSize() 缓存的最大容量，当超过这个容量GUAUA的Cache就使用LRU算法，即最少使用算法来移除缓存项，最大值设置成10000
     * expireAfterAccess() 有效期 5个小时
     */
    private static LoadingCache<String, String> MEMBER_CROWD_GROUP_RUN_CACHE =
            CacheBuilder.newBuilder().
                    initialCapacity(INITIAL_CAPACITY).
                    maximumSize(MAXIMUM_SIZE).
                    expireAfterAccess(DURATION, TimeUnit.HOURS).
                    build(new CacheLoader<String, String>() {
                        // 默认的数据加载实现,当调用get取值的时候,如果key没有对应的值
                        // 就调用这个方法进行加载
                        @Override
                        public String load(String key) throws Exception {
                            return "null";
                        }
                    });

    /**
     * 设置缓存
     * wangzhe
     * 2019-01-29
     * @param key
     * @param value
     */
    public static void setKey(String key,String value){
        MEMBER_CROWD_GROUP_RUN_CACHE.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try{
            value = MEMBER_CROWD_GROUP_RUN_CACHE.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.error("localCache get error",e);
        }
        return null;
    }

    /**
     * 是否包含
     * wangzhe
     * 2021-01-29
     * @param key
     * @return
     */
    public static boolean contain(String key){
        try {
            if(!"null".equals(MEMBER_CROWD_GROUP_RUN_CACHE.get(key))){
                return true;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

}
