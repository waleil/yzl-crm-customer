package cn.net.yzl.crm.customer.config.db;


import java.lang.annotation.*;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 17:57
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSourceSelector {
    DynamicDataSourceEnum value() default DynamicDataSourceEnum.master;
    boolean clear() default true;
}
