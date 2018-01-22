package com.navinfo.opentsp;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author: ChenJie
 * @date 2017/11/1
 */
public class Test {
    public static void main(String[] args) throws Exception{
        long l = 111111;
        System.out.println((float)l);
        //        System.out.println(9 & 3);
//        LCConcentratedRealTimeData.SpecialRealTimeData.Builder lc = LCConcentratedRealTimeData.SpecialRealTimeData.newBuilder();
//        LCConcentratedRealTimeData.RealTimeDataUnit.Builder data = LCConcentratedRealTimeData.RealTimeDataUnit.newBuilder();
//        data.setBrake(-100);
//        lc.addRealTimeDataUnit(data);

//        String hypb = "";
//        try {
////            LCFaultInfo.FaultInfo faultInfo = LCFaultInfo.FaultInfo.parseFrom(Base64.getDecoder().decode(hypb));
////            LCTerminalTrackRes.TerminalTrackRes builder = LCTerminalTrackRes.TerminalTrackRes.parseFrom(Base64.getDecoder().decode(hypb));
////            System.out.println(builder);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Jedis jedis = new Jedis("192.168.135.156",6379);
        Jedis jedis = new Jedis("10.30.50.21",6389);
//        jedis.auth("dGggMPwR2ZyKlzG3UnwsihYP");
        jedis.auth("123456");

        //写入redis末次位置
//        String content = "{\"key\":\"010086100866\",\"topic\":\"posraw\",\"message\":\"eyJjb21tYW5kIjoxMjI5MCwiZnJvbSI6MCwidG8iOjAsInByb3RvY29sIjoyLCJjaGFubmVscyI6MCwidW5pcXVlTWFyayI6IjAxMDA4NjEwMDg2NiIsInNlcmlhbE51bWJlciI6MiwiY3JlYXRlVGltZSI6MTUxMzY0ODQ3MSwicGFja2V0VG90YWwiOjAsInBhY2tldFNlcmlhbCI6MCwiYmxvY2tJZCI6MCwiY29udGVudCI6IkNBQVFBU2oyOUtNNk1QYmZyZzg0QmtDZ0JrZ01VUHZpNGRFRldBQmcxK0xoMFFWb0FIQXllRkNnQVFDb0FSUzRBZjkvZ2dMWkFnb0dDSzBCRU9nSENnWUlyZ0VRdEVJS0JRZzFFT2dIQ2cwSUlCRDhyZi8vLy8vLy8vOEJDZzBJSVJDMHhmLy8vLy8vLy84QkNnWUlEeENvd3dFS0JRZ1VFUEF1Q2dZSUVoREFtZ3dLQmdnZUVNQ0VQUW9GQ0NRUTBBOEtCQWduRUFBS0JRZ3pFSkJPQ2dRSUtoQUFDZ1FJS1JBQUNnVUlGeERnRWdvRkNCZ1EwQThLQmdpTUFSRFVZUW9GQ0RrUW1IVUtCd2daRUlERXFpSUtDQWdhRUlEWWp1RnZDZ2NJR3hEQWxyRUNDZ1VJTWhEb0J3b0ZDQndRMEE4S0JRZ0JFSWduQ2dZSUFoQ2duQUVLQlFnREVOZzJDZ1lJQkJEd3F3RUtCZ2dGRUtUN0JRb0ZDQVlRaUNjS0JnZ0hFSUNUQWdvR0NBZ1FnSk1DQ2dVSVdSRFFEd29FQ0FvUVpBb0ZDREVRaVhvS0JnZ0xFSVN2WHdvRUNBd1FaQW9GQ0xJQkVHUUtCZ2l6QVJEb0J3b0ZDRFlReUFFS0JRZ09FUEFWQ2dZSURoREFtZ3dLQndpUUFSREFoRDBLQndpa0FSQ0E2akFLQmdpY0FSQ0lKNG9DQmdvRUNBUVFBSm9DQUtBQ0FMQUM2QWZWQWdCQUhFYmRBZ0JBSEViNkFnd0tCQWdFRUFBS0JBZ0JFQUE9Iiwib3JpZ2luYWxQYWNrZXQiOm51bGwsInB1c2hBcmd1bWVudHMiOm51bGwsInJlc3VsdENvZGUiOjAsImRwUHJvY2VzcyI6dHJ1ZSwicXVldWVOYW1lIjpudWxsLCJ1cHBlclVuaXF1ZU1hcmsiOm51bGwsImNvbW1hbmRGb3JIZXgiOiIzMDAyIiwib3JpZ2luYWxQYWNrZXRGb3JIZXgiOm51bGwsImNvbnRlbnRGb3JIZXgiOiIwODAwMTAwMTI4RjZGNEEzM0EzMEY2REZBRTBGMzgwNjQwQTAwNjQ4MEM1MEZCRTJFMUQxMDU1ODAwNjBEN0UyRTFEMTA1NjgwMDcwMzI3ODUwQTAwMTAwQTgwMTE0QjgwMUZGN0Y4MjAyRDkwMjBBMDYwOEFEMDExMEU4MDcwQTA2MDhBRTAxMTBCNDQyMEEwNTA4MzUxMEU4MDcwQTBEMDgyMDEwRkNBREZGRkZGRkZGRkZGRkZGMDEwQTBEMDgyMTEwQjRDNUZGRkZGRkZGRkZGRkZGMDEwQTA2MDgwRjEwQThDMzAxMEEwNTA4MTQxMEYwMkUwQTA2MDgxMjEwQzA5QTBDMEEwNjA4MUUxMEMwODQzRDBBMDUwODI0MTBEMDBGMEEwNDA4MjcxMDAwMEEwNTA4MzMxMDkwNEUwQTA0MDgyQTEwMDAwQTA0MDgyOTEwMDAwQTA1MDgxNzEwRTAxMjBBMDUwODE4MTBEMDBGMEEwNjA4OEMwMTEwRDQ2MTBBMDUwODM5MTA5ODc1MEEwNzA4MTkxMDgwQzRBQTIyMEEwODA4MUExMDgwRDg4RUUxNkYwQTA3MDgxQjEwQzA5NkIxMDIwQTA1MDgzMjEwRTgwNzBBMDUwODFDMTBEMDBGMEEwNTA4MDExMDg4MjcwQTA2MDgwMjEwQTA5QzAxMEEwNTA4MDMxMEQ4MzYwQTA2MDgwNDEwRjBBQjAxMEEwNjA4MDUxMEE0RkIwNTBBMDUwODA2MTA4ODI3MEEwNjA4MDcxMDgwOTMwMjBBMDYwODA4MTA4MDkzMDIwQTA1MDg1OTEwRDAwRjBBMDQwODBBMTA2NDBBMDUwODMxMTA4OTdBMEEwNjA4MEIxMDg0QUY1RjBBMDQwODBDMTA2NDBBMDUwOEIyMDExMDY0MEEwNjA4QjMwMTEwRTgwNzBBMDUwODM2MTBDODAxMEEwNTA4MEUxMEYwMTUwQTA2MDgwRTEwQzA5QTBDMEEwNzA4OTAwMTEwQzA4NDNEMEEwNzA4QTQwMTEwODBFQTMwMEEwNjA4OUMwMTEwODgyNzhBMDIwNjBBMDQwODA0MTAwMDlBMDIwMEEwMDIwMEIwMDJFODA3RDUwMjAwNDAxQzQ2REQwMjAwNDAxQzQ2RkEwMjBDMEEwNDA4MDQxMDAwMEEwNDA4MDExMDAwIn0=\",\"serialNumber\":0,\"sendTime\":1513648471669,\"commandId\":\"3002\"}";
        String content = "{\"key\":\"010086100866\",\"topic\":\"posraw\",\"message\":\"eyJjb21tYW5kIjoxMjI5MCwiZnJvbSI6MCwidG8iOjAsInByb3RvY29sIjoyLCJjaGFubmVscyI6MCwidW5pcXVlTWFyayI6IjAxMDA4NjEwMDg2NiIsInNlcmlhbE51bWJlciI6MiwiY3JlYXRlVGltZSI6MTUxMzY2ODczMCwicGFja2V0VG90YWwiOjAsInBhY2tldFNlcmlhbCI6MCwiYmxvY2tJZCI6MCwiY29udGVudCI6IkNBQVFBU2oyOUtNNk1QYmZyZzg0QmtCR1NBeFFub0hqMFFWWWdKdnVBbUQ2Z09QUkJXZ0FjREo0VUtBQkFLZ0JGTGdCLzMrQ0F0OENDZ1lJclFFUTZBY0tCZ2l1QVJDMFFnb0ZDRFVRNkFjS0JBZ2ZFR1FLRFFnZ0VQeXQvLy8vLy8vLy93RUtEUWdoRUxURi8vLy8vLy8vL3dFS0JnZ1BFS2pEQVFvRkNCUVE4QzRLQmdnU0VNQ2FEQW9HQ0I0UW9NSWVDZ1VJSkJEUUR3b0VDQ2NRQUFvRkNETVFrRTRLQkFncUVBQUtCQWdwRUFBS0JRZ1hFT0FTQ2dVSUdCRFFEd29HQ0l3QkVOUmhDZ1VJT1JDWWRRb0hDQmtRZ01TcUlnb0lDQm9RZ05pTzRXOEtCd2diRU1DV3NRSUtCUWd5RU9nSENnVUlIQkRRRHdvRkNBRVFpQ2NLQmdnQ0VLQ2NBUW9GQ0FNUTJEWUtCZ2dFRVBDckFRb0dDQVVRcFBzRkNnVUlCaENJSndvR0NBY1FnSk1DQ2dZSUNCQ0Frd0lLQlFoWkVOQVBDZ1FJQ2hCa0NnVUlNUkNKZWdvR0NBc1FvSTBHQ2dRSURCQmtDZ1VJc2dFUVpBb0dDTE1CRU9nSENnVUlOaERJQVFvRkNBNFE4QlVLQmdnT0VNQ2FEQW9IQ0pBQkVMMkZQUW9IQ0tRQkVJRHhCQW9HQ0p3QkVJZ25pZ0lHQ2dRSUJCQUFtZ0lBb0FJQXNBTFlOdFVDQUlDN1JkMENBRUNjUmZvQ0RBb0VDQVFRQUFvRUNBRVFBQT09Iiwib3JpZ2luYWxQYWNrZXQiOm51bGwsInB1c2hBcmd1bWVudHMiOm51bGwsInJlc3VsdENvZGUiOjAsImRwUHJvY2VzcyI6dHJ1ZSwicXVldWVOYW1lIjpudWxsLCJ1cHBlclVuaXF1ZU1hcmsiOm51bGwsIm9yaWdpbmFsUGFja2V0Rm9ySGV4IjpudWxsLCJjb21tYW5kRm9ySGV4IjoiMzAwMiIsImNvbnRlbnRGb3JIZXgiOiIwODAwMTAwMTI4RjZGNEEzM0EzMEY2REZBRTBGMzgwNjQwNDY0ODBDNTA5RTgxRTNEMTA1NTg4MDlCRUUwMjYwRkE4MEUzRDEwNTY4MDA3MDMyNzg1MEEwMDEwMEE4MDExNEI4MDFGRjdGODIwMkRGMDIwQTA2MDhBRDAxMTBFODA3MEEwNjA4QUUwMTEwQjQ0MjBBMDUwODM1MTBFODA3MEEwNDA4MUYxMDY0MEEwRDA4MjAxMEZDQURGRkZGRkZGRkZGRkZGRjAxMEEwRDA4MjExMEI0QzVGRkZGRkZGRkZGRkZGRjAxMEEwNjA4MEYxMEE4QzMwMTBBMDUwODE0MTBGMDJFMEEwNjA4MTIxMEMwOUEwQzBBMDYwODFFMTBBMEMyMUUwQTA1MDgyNDEwRDAwRjBBMDQwODI3MTAwMDBBMDUwODMzMTA5MDRFMEEwNDA4MkExMDAwMEEwNDA4MjkxMDAwMEEwNTA4MTcxMEUwMTIwQTA1MDgxODEwRDAwRjBBMDYwODhDMDExMEQ0NjEwQTA1MDgzOTEwOTg3NTBBMDcwODE5MTA4MEM0QUEyMjBBMDgwODFBMTA4MEQ4OEVFMTZGMEEwNzA4MUIxMEMwOTZCMTAyMEEwNTA4MzIxMEU4MDcwQTA1MDgxQzEwRDAwRjBBMDUwODAxMTA4ODI3MEEwNjA4MDIxMEEwOUMwMTBBMDUwODAzMTBEODM2MEEwNjA4MDQxMEYwQUIwMTBBMDYwODA1MTBBNEZCMDUwQTA1MDgwNjEwODgyNzBBMDYwODA3MTA4MDkzMDIwQTA2MDgwODEwODA5MzAyMEEwNTA4NTkxMEQwMEYwQTA0MDgwQTEwNjQwQTA1MDgzMTEwODk3QTBBMDYwODBCMTBBMDhEMDYwQTA0MDgwQzEwNjQwQTA1MDhCMjAxMTA2NDBBMDYwOEIzMDExMEU4MDcwQTA1MDgzNjEwQzgwMTBBMDUwODBFMTBGMDE1MEEwNjA4MEUxMEMwOUEwQzBBMDcwODkwMDExMEJEODUzRDBBMDcwOEE0MDExMDgwRjEwNDBBMDYwODlDMDExMDg4Mjc4QTAyMDYwQTA0MDgwNDEwMDA5QTAyMDBBMDAyMDBCMDAyRDgzNkQ1MDIwMDgwQkI0NUREMDIwMDQwOUM0NUZBMDIwQzBBMDQwODA0MTAwMDBBMDQwODAxMTAwMCJ9\",\"serialNumber\":0,\"sendTime\":1513668730862,\"commandId\":\"3002\"}";
        KafkaCommand kafkaCommand = JSON.parseObject(content, KafkaCommand.class);
        Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
        LCLocationData.LocationData locationData1 = LCLocationData.LocationData.parseFrom(packet.getContent());
        LCLocationData.VehicleStatusAddition statusAddition = locationData1.getStatusAddition();
        if(true){
            System.out.println("标准里程:"+locationData1.getStandardMileage() + "\r\n标准油耗="+locationData1.getStandardFuelCon()
            +"\r\n标准速度="+locationData1.getElectricVehicle());
            return;
        }
//        jedis.hset("LASTEST_LOCATION_DATA_ALL".getBytes("utf-8"),"10086100866".getBytes("utf-8"),locationData1.toByteArray());


        Map<byte[], byte[]> map = jedis.hgetAll("LASTEST_LOCATION_DATA_ALL".getBytes());
        Set<Map.Entry<byte[], byte[]>> entries = map.entrySet();
        Iterator<Map.Entry<byte[], byte[]>> iterator = entries.iterator();
        int size = 0;
        while(iterator.hasNext()){
            Map.Entry<byte[], byte[]> entry = iterator.next();
            byte[] key = entry.getKey();
            System.out.println(new String(key));
            byte[] value = entry.getValue();
//            LCMileageConsumption.MileageConsumption mileageConsumption = LCMileageConsumption.MileageConsumption.parseFrom(value);
//            System.out.println(new String(key) + ",getMeterMileage=="+ mileageConsumption.getMeterMileage()+",getMileage=="+mileageConsumption.getMileage()
//            +",getTerminalMileage=="+mileageConsumption.getTerminalMileage()+",getOilConsumption=="+mileageConsumption.getOilConsumption()+
//            ",getFuelOil=="+mileageConsumption.getFuelOil());

            LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(value);
            if(locationData.hasStatusAddition()){
                if("10086100866".equals(new String(key))){
//                    System.out.println("=========="+locationData.getStatusAddition());
                }
            }
            size ++;
        }
        System.out.println(size);



//        byte [] bytes = jedis.hget("LASTEST_LOCATION_DATA_ALL".getBytes(),"14807390796".getBytes());
//        try {
//            LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(bytes);
//            System.out.println(locationData);
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
    }

    @org.junit.Test
    public void test0200() throws InvalidProtocolBufferException, Descriptors.DescriptorValidationException {
        LCLocationData.LocationData.Builder builder = LCLocationData.LocationData.newBuilder();
        LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = LCLocationData.VehicleStatusAddition.newBuilder();
        builder.setAlarm(1);
        builder.setHeight(2);
        builder.setStatus(3);
        builder.setSpeed(4);
        builder.setDirection(5);
        builder.setGpsDate(6);
        builder.setReceiveDate(7);
        builder.setIsPatch(false);

        LCVehicleStatusData.VehicleStatusData.Builder builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
        builderData.setStatusValue(12);
        builderData.setTypes(LCStatusType.StatusType.dynamicLoad);
        vsaddBuilder.addStatus(builderData);
        builder.setStatusAddition(vsaddBuilder);


        LCVehicleStatusData.VehicleStatusData.Builder builderDataLamp = LCVehicleStatusData.VehicleStatusData.newBuilder();
        builderDataLamp.setStatusValue(1);
        builderDataLamp.setTypes(LCStatusType.StatusType.lampState);
        vsaddBuilder.addStatus(builderDataLamp);

        builder.setStatusAddition(vsaddBuilder);

        byte[] bytes = builder.build().toByteArray();

        LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(bytes);

        System.out.println(locationData.hasStatusAddition());
        LCLocationData.VehicleStatusAddition statusAddition = locationData.getStatusAddition();


        List<LCVehicleStatusData.VehicleStatusData> statusList = statusAddition.getStatusList();
        System.out.println( statusAddition.getAllFields());

    }

    @org.junit.Test
    public void testPailie(){
        String chs[]={"仪表","车轮","gps"};
        combiantion(chs);
    }

    public static void combiantion(String chs[]){
        if(chs==null||chs.length==0){
            return ;
        }
        List list=new ArrayList();
        for(int i=1;i<=chs.length;i++){
            combine(chs,0,i,list);
        }
    }
    //从字符数组中第begin个字符开始挑选number个字符加入list中
    public static void combine(String [] cs,int begin,int number,List list){
        if(number==0){
            System.out.println(list.toString());
            return ;
        }
        if(begin==cs.length){
            return;
        }
        list.add(cs[begin]);
        combine(cs,begin+1,number-1,list);
        list.remove(cs[begin]);
        combine(cs,begin+1,number,list);
    }




}
