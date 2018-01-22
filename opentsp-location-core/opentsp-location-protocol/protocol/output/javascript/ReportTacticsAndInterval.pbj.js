"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting = {};}
com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval",{
	tactics: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval.ReportTactics;},
		id: 1
	},
	program: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval.ReportProgram;},
		id: 2
	},
	notLoginTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 3
	},
	sleepingTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 4
	},
	urgentTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 5
	},
	defaultTime: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 6
	},
	defaultDistance: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 7
	},
	notLoginDistance: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 8
	},
	sleepingDistance: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 9
	},
	urgentDistance: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 10
	},
	inflectionAngle: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 11
	},
	ReportTactics: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval.ReportTactics",{
		timing :0x00,
		distance :0x01,
		timingAndDistance :0x02	}),
	ReportProgram: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.ReportTacticsAndInterval.ReportProgram",{
		accStatus :0x00,
		loginAndAcc :0x01	})});
