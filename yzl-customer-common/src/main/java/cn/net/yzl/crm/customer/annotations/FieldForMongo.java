package cn.net.yzl.crm.customer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : 张瑞松
 * 映射mongo实体类的字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldForMongo {
    String PrimaryKey() default "id"; // id 为默认主键
}
