"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
com.navinfo.opentsp.platform.location.protocol.terminal._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.SetAreaOperation= PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.SetAreaOperation",{
		updateArea :0,
		additionalArea :1,
		modifyArea :2});
com.navinfo.opentsp.platform.location.protocol.terminal.SetCircleAreaAlarm = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.SetCircleAreaAlarm",{
	operations: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.SetAreaOperation;},
		id: 1
	},
	areas: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.CircleArea;},
		id: 2
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.CircleArea = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.CircleArea",{
	areaIdentify: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	areaProperty: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 2
	},
	centerLatitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	centerLongitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	radius: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 5
	},
	beginDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 6
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 7
	},
	maxSpeed: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 8
	},
	speedingContinuousTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 9
	}});
