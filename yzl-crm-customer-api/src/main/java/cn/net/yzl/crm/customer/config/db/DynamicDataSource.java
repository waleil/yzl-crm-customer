package cn.net.yzl.crm.customer.config.db;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 17:56
 * @description:
 */
public class DynamicDataSource  extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }
}
