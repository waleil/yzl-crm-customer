package cn.net.yzl.crm.customer.collector.utils;

import cn.net.yzl.crm.customer.annotations.FieldForMongo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author :张瑞松
 * @descrip: mongo工具类
 */
public class MongoQueryUtil {

    public static <T> QueryUpdate getMongoQueryForUpdate(T model) throws Exception {
        if (model == null) return null;
        Query query = new Query();
        Update update = new Update();
        Class<? extends Object> tClass = model.getClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field f : fields) {
            FieldForMongo filed_annotation = f.getAnnotation(FieldForMongo.class); //获取主键字段，可能存在多个字段确定唯一
            String methodName = getMethodName(f.getName());
            Method m = (Method) model.getClass().getMethod(methodName);
            Object val = m.invoke(model);// 调用getter方法获取属性值
            if (filed_annotation != null) {
                String primaryKey = filed_annotation.PrimaryKey();
                query.addCriteria(Criteria.where(primaryKey).is(val.toString()));
                // todo 当多个字段联合查询唯一时，会有问题，之后调整
            } else {
                update.set(f.getName(), val);
            }
        }
        QueryUpdate queryUpdate = new QueryUpdate();
        queryUpdate.setQuery(query);
        queryUpdate.setUpdate(update);
        return queryUpdate;
    }

    /**
     * 根据id获取一批映射实体，并且返回指定字段(可以避免过多无用字段的返回)
     *
     * @param model
     * @param ids
     * @param ids,class
     * @return
     */
    public static Query getBatchQuery(List<String> ids, Class<?> model) {
        if (ids == null || ids.size() == 0) return null;
        Query query = new Query();
        Class<? extends Object> tClass = model.getClass();
        Field[] fields = model.getDeclaredFields();
        String primaryKeyName = "";
        for (Field f : fields) {
            FieldForMongo filed_annotation = f.getAnnotation(FieldForMongo.class); //获取主键字段
            if (filed_annotation != null) {
                primaryKeyName = f.getName(); //获取主键
            }
            query.fields().include(f.getName());
        }
        query.addCriteria(Criteria.where(primaryKeyName).in(ids));
        return query;
    }

//    public static <T,M> Page findByPage(M model) {
//
//    }

    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        String item = new String(items);

        if (item.toLowerCase().equals("del"))
            return "isDel";
        else if (item.toLowerCase().equals("vip_flag")) {
            return "isVip_flag";
        } else
            return "get" + getMethodName(item);
    }
}
