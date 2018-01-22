package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.SpecialCanMongodbService;
import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticData;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author: ChenJie
 * @Description:
 * @Date 2017/11/9
 * @Modified by:
 */
public class SpecialCanMongodbServiceImpl extends MongoDaoImp implements SpecialCanMongodbService {
    private static Logger logger = LoggerFactory.getLogger(SpecialCanMongodbServiceImpl.class);
    private static FastDateFormat fdf = FastDateFormat.getInstance("yyyyMM");

    /**
     * 保存统计信息0F38到mongodb
     * @param terminalId
     * @param statisticData
     * @return WriteResult
     * */
    @Override
    public WriteResult saveTerminalStatisticData(String terminalId,LCTerminalStatisticData.StatisticData statisticData) {

        DBCollection collection;
        WriteResult result = null;
        try {
            String month = fdf.format(statisticData.getTime()*1000);
            collection = MongoManager.start(LCMongo.DB.LC_GPS_CANDATA, LCMongo.Collection.LC_CAN_STATISTIC_DATA+month);
            BasicDBObject dbObject = new BasicDBObject();

            dbObject.put("terminalId",terminalId);
            dbObject.put("beginDate",statisticData.getRouteStartTime());
            dbObject.put("endDate",statisticData.getRouteEndTime());
            dbObject.put("data",statisticData.toByteArray());
            result = collection.save(dbObject);
        } catch (UnknownHostException e) {
            logger.error("saveTerminalStatisticData to mongodb error:",e);
        }

        return result;

    }

    /**
     * 保存故障信息0F39到mongodb
     * @param terminalId
     * @param faultInfo
     * @return WriteResult
     * */
    @Override
    public WriteResult saveFaultData(String terminalId, LCFaultInfo.FaultInfo faultInfo) {
        DBCollection collection;
        WriteResult result = null;
        try {
            String month = fdf.format(faultInfo.getGpsLocationData().getGpsTime()*1000);
            collection = MongoManager.start(LCMongo.DB.LC_GPS_CANDATA, LCMongo.Collection.LC_FAULT_DATA+month);
            BasicDBObject dbObject = new BasicDBObject();

            dbObject.put("terminalId",terminalId);
            dbObject.put("gpsTime",faultInfo.getGpsLocationData().getGpsTime());
            dbObject.put("data",faultInfo.toByteArray());
            result = collection.save(dbObject);
        } catch (UnknownHostException e) {
            logger.error("saveFaultData to mongodb error:",e);
        }

        return result;
    }
}
