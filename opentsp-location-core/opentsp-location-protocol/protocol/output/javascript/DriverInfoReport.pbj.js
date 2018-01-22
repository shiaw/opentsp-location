"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
com.navinfo.opentsp.platform.location.protocol.terminal._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.DriverInfoReport = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.DriverInfoReport",{
	cardStatus: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 1
	},
	cardSwitchDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 2
	},
	results: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.DriverInfoReport.ReadResult;},
		id: 3
	},
	name: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 4
	},
	certificateCode: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 5
	},
	organizationName: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 6
	},
	licenseValidDate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 7
	},
	ReadResult: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.DriverInfoReport.ReadResult",{
		success :0,
		passwordError :1,
		cardLocked :2,
		cardPullOut :3,
		cardCheckError :4	})});
