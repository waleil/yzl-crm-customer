package cn.net.yzl.crm.customer.init;

import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.utils.BeanUtils;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * InitEnv
 * wangzhe
 * 2021-01-25
 */
@Component
@Slf4j
public class InitEnv implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("启动加载...");
        //
        initMemberCard();
    }

    /**
     * 初始化memberCard
     *  (注:memberCard长度必须都为10位，且最高位要相同，因为 数据库中member_Card为varchar类型，长度不一致会导致max(member_Card)不准确)
     * wangzhe
     * 2021-01-25
     * @Return: void
     */
    private void initMemberCard() {
        MemberMapper memberMapper = BeanUtils.getBean(MemberMapper.class);
        //获取数据库中最大的编号
        String maxMemberCardStr = memberMapper.queryMaxMemberCard();
        long maxMemberCard = maxMemberCardStr == null ? 0 : Long.parseLong(maxMemberCardStr);

        //先从redis中获取缓存的数据
        RedisUtil redisUtil = BeanUtils.getBean(RedisUtil.class);
        String cacheKey = CacheKeyUtil.maxMemberCardCacheKey();
        String cacheMaxCard = redisUtil.getStr(cacheKey);
        log.info("启动获取MemberCard,数据库中最大的MemberCard：{}，redis中的MemberCard：{}...",maxMemberCardStr,cacheMaxCard);
        //以最大的为准
        if (StringUtils.isNotEmpty(cacheMaxCard)) {
            long parseLong = Long.parseLong(cacheMaxCard);
            if (maxMemberCard <= parseLong) {
                return;
            }
        }
        //缓存至redis
        redisUtil.set(cacheKey, maxMemberCard);
        log.info("启动设置MemberCard,MemberCard：key:{},value:{}...",cacheKey,maxMemberCard);
    }



}
