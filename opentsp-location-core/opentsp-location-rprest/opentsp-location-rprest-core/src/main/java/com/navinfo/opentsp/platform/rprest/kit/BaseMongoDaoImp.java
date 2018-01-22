package com.navinfo.opentsp.platform.rprest.kit;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HOUQL on 2017/5/23.
 */
public class BaseMongoDaoImp {

    /**
     * 查询结果集并排序，2016年5月25日增加——王景康
     *
     * @param collection
     * @param query
     * @param orderBy
     * @param limit
     * @return
     */
    public List<DBObject> queryByCondition(DBCollection collection, DBObject query, DBObject orderBy, int limit) {
        long time = System.currentTimeMillis();
        DBCursor find = collection.find(query);
        if(limit > 0) {
            find.limit(limit);
        }
        if(orderBy != null) {
            find = find.sort(orderBy);
        }
        List<DBObject> array = find.toArray();
        return array;
    }
}
