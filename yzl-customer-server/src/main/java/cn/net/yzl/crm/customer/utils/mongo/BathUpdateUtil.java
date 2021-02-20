package cn.net.yzl.crm.customer.utils.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: BathUpdateUtil
 * @description 批量更新工具类
 * @date: 2021/2/20 4:41 下午
 */
@Slf4j
public class BathUpdateUtil {
    public static int bathUpdate(MongoTemplate mongoTemplate, Class<?> entityClass,
                                 List<BathUpdateOptions> options,boolean ordered) {
        String collectionName = determineCollectionName(entityClass);

        return doBathUpdate(mongoTemplate.getDb(),
                collectionName, options, ordered);
    }
    public static int bathUpdate(MongoTemplate mongoTemplate, String collectionName,
                                 List<BathUpdateOptions> options,boolean ordered) {
        return doBathUpdate(mongoTemplate.getDb(),
                collectionName, options, ordered);
    }

    private static String determineCollectionName(Class<?> entityClass) {
        if (entityClass == null) {
            throw new InvalidDataAccessApiUsageException(
                    "No class parameter provided, entity collection can't be determined!");
        }
        String collName = entityClass.getSimpleName();
        if(entityClass.isAnnotationPresent(Document.class)) {
            Document document = entityClass.getAnnotation(Document.class);
            collName = document.collection();
        } else {
            collName = collName.replaceFirst(collName.substring(0, 1)
                    ,collName.substring(0, 1).toLowerCase()) ;
        }
        return collName;
    }

    private static int doBathUpdate(MongoDatabase database, String collName,
                                    List<BathUpdateOptions> options, boolean ordered) {
        BasicDBObject command = new BasicDBObject();
        command.put("update", collName);
        List<BasicDBObject> updateList = new ArrayList<>();
        for (BathUpdateOptions option : options) {
            BasicDBObject update = new BasicDBObject();
            update.put("q", option.getQuery().getQueryObject());
            update.put("u", option.getUpdate().getUpdateObject());
            update.put("upsert", option.getUpsert());
            update.put("multi", option.getMulti());
            updateList.add(update);
        }
        command.put("updates", updateList);
        command.put("ordered", ordered);
        org.bson.Document document =database.runCommand(command);
        return Integer.parseInt(String.valueOf(document.get("n")));
    }
}
