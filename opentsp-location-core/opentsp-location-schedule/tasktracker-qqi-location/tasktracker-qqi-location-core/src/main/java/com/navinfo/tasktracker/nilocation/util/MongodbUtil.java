package com.navinfo.tasktracker.nilocation.util;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class MongodbUtil {
    public static int bathUpdate(MongoTemplate mongoTemplate, String collName,
                                 List<BathUpdateOptions> options, boolean ordered) {
        return doBathUpdate(mongoTemplate.getCollection(collName), collName, options, ordered);
    }

    public static int bathUpdate(DBCollection dbCollection, String collName,
                                 List<BathUpdateOptions> options, boolean ordered) {
        return doBathUpdate(dbCollection, collName, options, ordered);
    }

    public static int bathUpdate(MongoTemplate mongoTemplate, Class<?> entityClass,
                                 List<BathUpdateOptions> options, boolean ordered) {
        String collectionName = determineCollectionName(entityClass);
        return doBathUpdate(mongoTemplate.getCollection(collectionName),
                collectionName, options, ordered);
    }

    public static int bathUpdate(DBCollection dbCollection, Class<?> entityClass,
                                 List<BathUpdateOptions> options, boolean ordered) {
        return doBathUpdate(dbCollection,
                determineCollectionName(entityClass), options, ordered);
    }


    public static int bathUpdate(MongoTemplate mongoTemplate, String collName,
                                 List<BathUpdateOptions> options) {
        return doBathUpdate(mongoTemplate.getCollection(collName), collName, options, true);
    }

    public static int bathUpdate(DBCollection dbCollection, String collName,
                                 List<BathUpdateOptions> options) {
        return doBathUpdate(dbCollection, collName, options, true);
    }

    public static int bathUpdate(MongoTemplate mongoTemplate, Class<?> entityClass,
                                 List<BathUpdateOptions> options) {
        String collectionName = determineCollectionName(entityClass);
        return doBathUpdate(mongoTemplate.getCollection(collectionName),
                collectionName, options, true);
    }

    public static int bathUpdate(DBCollection dbCollection, Class<?> entityClass,
                                 List<BathUpdateOptions> options) {
        return doBathUpdate(dbCollection,
                determineCollectionName(entityClass), options, true);
    }


    private static int doBathUpdate(DBCollection dbCollection, String collName,
                                    List<BathUpdateOptions> options, boolean ordered) {
        DBObject command = new BasicDBObject();
        command.put("update", collName);
        List<BasicDBObject> updateList = new ArrayList<BasicDBObject>();
        for (BathUpdateOptions option : options) {
            BasicDBObject update = new BasicDBObject();
            update.put("q", option.getQuery().getQueryObject());
            update.put("u", option.getUpdate().getUpdateObject());
            update.put("upsert", option.isUpsert());
            update.put("multi", option.isMulti());
            updateList.add(update);
        }
        command.put("updates", updateList);
        command.put("ordered", ordered);
        CommandResult commandResult = dbCollection.getDB().command(command);
        return Integer.parseInt(commandResult.get("n").toString());
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

//    List<BathUpdateOptions> list = new ArrayList<BathUpdateOptions>();
//    list.add(new BathUpdateOptions(Query.query(Criteria.where("trackId").is("7f235d5d2a19410aaacd6f99ae9c3345")),Update.update("fillFlag", "1"),true,true));
//    list.add(new BathUpdateOptions(Query.query(Criteria.where("trackId").is("a2cb7f46e5bc4658ade95a302db26110")), Update.update("fillFlag", "1"),true,true));
//    int n = MongodbUtil.bathUpdate(mongoTemplate,"vehicle_bak_track_20170517", list);
}
