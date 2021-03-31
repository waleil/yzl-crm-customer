package cn.net.yzl.crm.customer.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Configuration
@RefreshScope //实时刷新nacos配置中心文件
@ConfigurationProperties(prefix = "app")
public class AppIdsConfig {
    private String ids;
    private Set<String> idsSet;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
        if(StringUtils.isNotEmpty(ids)){
            setIdsSet(org.springframework.util.StringUtils.commaDelimitedListToSet(ids));
        }else{
            setIdsSet(Collections.emptySet());
        }
    }

    public Set<String> getIdsSet() {
        return idsSet;
    }

    private void setIdsSet(Set<String> idsSet) {
        this.idsSet = idsSet;
    }
}
