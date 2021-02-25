package cn.net.yzl.crm.customer.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author lichanghong
 * @version 1.0
 * @title: RedissonAutoConfiguration
 * @description todo
 * @date: 2021/1/8 5:09 下午
 */
@Configuration
@ConditionalOnClass(Config.class)
public class RedissonAutoConfiguration {
    @Autowired
    private RedissonProperties redissonProperties;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        String [] addresses = redissonProperties.getAddresses();
        String[] redissonAddr = new String[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            redissonAddr[i] = "redis://" + addresses[i];
        }
        Config config = new Config();
        //设置集群模式
        ClusterServersConfig clusterServersConfig= config.useClusterServers().addNodeAddress(redissonAddr);
        clusterServersConfig.setConnectTimeout(1000);
        clusterServersConfig.setKeepAlive(true);
        clusterServersConfig.setScanInterval(2000);
        clusterServersConfig.setIdleConnectionTimeout(1000);
        clusterServersConfig.setMasterConnectionPoolSize(32);
        clusterServersConfig.setMasterConnectionMinimumIdleSize(8);
        clusterServersConfig.setSlaveConnectionPoolSize(32);
        clusterServersConfig.setSlaveConnectionMinimumIdleSize(8);
        clusterServersConfig.setIdleConnectionTimeout(1000);
        clusterServersConfig.setPingConnectionInterval(1000);
        clusterServersConfig.setRetryAttempts(3);
        clusterServersConfig.setRetryInterval(300);
        if(StringUtils.hasText(redissonProperties.getPassword())){
            clusterServersConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }
}
