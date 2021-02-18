package cn.net.yzl.crm.customer.collector.utils;

/**
 * 缓存生成key
 * wangzhe
 * 2021-01-25
 */
public class CacheKeyUtil {
    final static String serverName ="crmCustomer";

    /**
     * 生成会员卡最大缓存key
     * wangzhe
     * 2021-01-25
     * @return
     */
    public static String maxMemberCardCacheKey(){
        return new StringBuilder(serverName).append("-").append("maxMemberCard").toString();
    }
}
