package cn.net.yzl.crm.customer.utils;

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

    /**
     * 生成圈选顾客缓存key
     * wangzhe
     * 2021-01-25
     * @return
     */
    public static String groupRunCacheKey(String suffix){
        return new StringBuilder("all{").append(serverName).append("-").append("memberGroupRun").append("}").append(suffix).toString();
    }



    /**
     * 同步memberLabel缓存key
     * wangzhe
     * 2021-01-25
     * @return
     */
    public static String syncMemberLabelCacheKey(){
        return new StringBuilder("all{").append(serverName).append("-").append("memberLabel-sync").append("}").toString();
    }
}
