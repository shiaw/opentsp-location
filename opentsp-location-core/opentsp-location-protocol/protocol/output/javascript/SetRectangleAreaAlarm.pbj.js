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
com.navinfo.opentsp.platform.location.protocol.terminal.SetRectangleAreaAlarm = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.SetRectangleAreaAlarm",{
	operations: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.SetAreaOperation;},
		id: 1
	},
	areas: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.RectangleArea;},
		id: 2
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.RectangleArea = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.RectangleArea",{
	areaId: {
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
	leftTopLatitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	leftToplongitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	rightBottomLatitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 5
	},
	rightBottomLongitude: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 6
	},
	beginDate: {
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
