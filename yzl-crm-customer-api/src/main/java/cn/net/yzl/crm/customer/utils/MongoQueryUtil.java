package cn.net.yzl.crm.customer.utils;

import cn.net.yzl.crm.customer.annotations.FieldForMongo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :张瑞松
 * @descrip: mongo修改工具类
 */
public class MongoQueryUtil {

    public static <T> QueryUpdate getMongoQueryForUpdate(T model) throws Exception {
        if (model == null) return null;
        Query query = new Query();
        Update update = new Update();
        Class<? extends Object> tClass=model.getClass();
        Field[] fields =tClass.getDeclaredFields();
        for (Field f : fields) {
            FieldForMongo filed_annotation = f.getAnnotation(FieldForMongo.class); //获取主键字段，可能存在多个字段确定唯一

            String methodName="";
            if(f.getName().toLowerCase().equals("del")) // del 生成的方法特殊处理
                methodName="isDel";
            else
                methodName="get" + getMethodName(f.getName());

            Method m = (Method) model.getClass().getMethod(methodName);
            Object val = m.invoke(model);// 调用getter方法获取属性值
            if (filed_annotation != null) {
                String primaryKey = filed_annotation.PrimaryKey();
                query.addCriteria(Criteria.where(primaryKey).is(val.toString()));
            } else {
                update.set(f.getName(), val);
            }
        }
        QueryUpdate queryUpdate = new QueryUpdate();
        queryUpdate.setQuery(query);
        queryUpdate.setUpdate(update);
        return queryUpdate;
    }

    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
