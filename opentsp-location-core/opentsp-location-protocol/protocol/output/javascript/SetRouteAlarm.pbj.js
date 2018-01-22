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
com.navinfo.opentsp.platform.location.protocol.terminal.SetRouteAlarm = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.SetRouteAlarm",{
	operations: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.SetAreaOperation;},
		id: 1
	},
	routes: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.Route;},
		id: 2
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.Route = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.Route",{
	routeIdentify: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	routeProperty: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 2
	},
	beginDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 3
	},
	endDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 4
	},
	turningPoints: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.TurningPoint;},
		id: 5
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.TurningPoint = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.TurningPoint",{
	turningIdentify: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	lineIdentify: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 2
	},
	turningLatitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	turningLongitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	lineWidth: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 5
	},
	lineProperty: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 6
	},
	drivingOutTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 7
	},
	drivingLackOfTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 8
	},
	maxSpeed: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 9
	},
	speedingContinuousTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 10
	}});
