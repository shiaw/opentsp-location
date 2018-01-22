"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.dataaccess)=="undefined") {com.navinfo.opentsp.platform.location.protocol.dataaccess = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.dataaccess.common)=="undefined") {com.navinfo.opentsp.platform.location.protocol.dataaccess.common = {};}
com.navinfo.opentsp.platform.location.protocol.dataaccess.common._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RegularCode= PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RegularCode",{
		speeding :0x2981A,
		inOutArea :0x29824,
		routeDriverTime :0x2982E,
		driverNotCard :0x29838,
		doorOpenOutArea :0x29842,
		drivingBan :0x2984C});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaType= PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaType",{
		noType :0x222E0,
		point :0x222E1,
		circle :0x222E2,
		rectangle :0x222E3,
		polygon :0x222E4,
		route :0x222E5,
		segment :0x222E6});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaSpeeding = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaSpeeding",{
	areaId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaType;},
		id: 2
	},
	maxSpeed: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	continuousTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	startDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 5
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 6
	},
	isEveryDay: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 7
	},
	routeId: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 8
	},
	basedTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 9
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.InOutArea = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.InOutArea",{
	areaId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	inAreaAlarmToDriver: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	inAreaAlarmToPlatform: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 3
	},
	outAreaAlarmToDriver: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 4
	},
	outAreaAlarmToPlatform: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 5
	},
	basedTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 6
	},
	isEveryDay: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 7
	},
	startDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 8
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 9
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RouteDriverTime = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RouteDriverTime",{
	routeId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	lineId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 2
	},
	maxTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	minTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	basedTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 5
	},
	isEveryDay: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 6
	},
	startDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 7
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 8
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DriverNotCard = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DriverNotCard",{
	notCardTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DoorOpenOutArea = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DoorOpenOutArea",{
	areaId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	basedTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	isEveryDay: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 3
	},
	startDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 4
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 5
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DrivingBan = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DrivingBan",{
	isEveryDay: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 1
	},
	startDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 2
	},
	endDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 3
	}});
com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RegularData = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RegularData",{
	terminalId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	regularCode: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RegularCode;},
		id: 2
	},
	lastModifyDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 3
	},
	speeding: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.AreaSpeeding;},
		id: 4
	},
	inOutArea: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.InOutArea;},
		id: 5
	},
	driverTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.RouteDriverTime;},
		id: 6
	},
	driverNotCard: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DriverNotCard;},
		id: 7
	},
	doorOpenOutArea: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DoorOpenOutArea;},
		id: 8
	},
	drivingBan: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.DrivingBan;},
		id: 9
	}});
