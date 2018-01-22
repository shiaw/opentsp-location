package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

public class LCMongo {
    /**
     * MongoDB库名称
     */
    public static class DB {
//		public static final String LC_GPS_LOCATION = "GpsLocationData";
//		public static final String LC_GPS_MULTIMEDIA = "GpsMultimediaData";
//		public static final String LC_GPS_DRIVING_RECORDER = "GpsDrivingRecorderData";
//		public static final String LC_GPS_TRANSFER = "GpsTransferData";
//		public static final String LC_DATA_STATISTIC_ANALYSIS = "DataStatisticAnalysis";
//		public static final String LC_GPS_CANDATA = "GpsCANData";

        public static final String LC_GPS_LOCATION = "GpsZhlcData";
		public static final String LC_GPS_MULTIMEDIA = "GpsZhlcData";
		public static final String LC_GPS_DRIVING_RECORDER = "GpsZhlcData";
		public static final String LC_GPS_TRANSFER = "GpsZhlcData";
		public static final String LC_DATA_STATISTIC_ANALYSIS = "GpsZhlcData";
		public static final String LC_GPS_CANDATA = "GpsZhlcData";
    }

    /**
     * MongoDB集合名称
     */
    public static class Collection {
        /**
         * 位置数据,名称为：GpsDataEntity_YYMM,需要使用者添加日期后缀
         */
        public static final String LC_GPS_DATA_ENTITY = "GpsDataEntity_";
        /**
         * 行驶记录仪数据,名称为：RecorderData_YYMM,需要使用者添加日期后缀
         */
        public static final String LC_RECORDER_ENTITY = "RecorderData_";
        /**
         * 透传数据,名称为：TransferData_YYMM,需要使用者添加日期后缀
         */
        public static final String LC_TRANSFER_ENTITY = "TransferData_";
        /**
         * 多媒体数据,名称为：Multimedia,不需要使用者添加日期后缀
         */
        public static final String LC_MULTIMEDIA_ENTITY = "Multimedia";
        public static final String LC_CANDATA = "CANDataEntityForQuery";
        public static final String LC_CANDATA_REPORT = "CANDataEntityForReport_";
        public static final String LC_CAN_STATISTIC_DATA="can_statistic_data_";
        public static final String LC_FAULT_DATA="lc_fault_data_";

    }
}

