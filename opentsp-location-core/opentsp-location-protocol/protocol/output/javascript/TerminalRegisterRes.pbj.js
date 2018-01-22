"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
com.navinfo.opentsp.platform.location.protocol.terminal._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.TerminalRegisterRes = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.TerminalRegisterRes",{
	serialNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	results: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.TerminalRegisterRes.RegisterResult;},
		id: 2
	},
	AuthCoding: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 3
	},
	RegisterResult: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.TerminalRegisterRes.RegisterResult",{
		success :0x00,
		vehicleRegistered :0x01,
		vehicleNotRegister :0x02,
		terminalRegistered :0x03,
		terminalNotRegister :0x04	})});
