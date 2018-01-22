"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting = {};}
com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.PrivilegeNumbers = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.PrivilegeNumbers",{
	platformPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 1
	},
	resetPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 2
	},
	factoryResetPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 3
	},
	PlatformsmsPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 4
	},
	smsAlarmPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 5
	},
	tactics: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.PrivilegeNumbers.AnswerTactics;},
		id: 6
	},
	everyTalking: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 7
	},
	monthTalking: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 8
	},
	listenerPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 9
	},
	privilegeSmsPhone: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 10
	},
	AnswerTactics: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.terminalsetting.PrivilegeNumbers.AnswerTactics",{
		autoAnswer :0x00,
		AnswerByAcc :0x01	})});
