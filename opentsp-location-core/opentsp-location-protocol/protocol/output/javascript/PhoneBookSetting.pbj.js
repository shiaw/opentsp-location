"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
com.navinfo.opentsp.platform.location.protocol.terminal._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBookSetting = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBookSetting",{
	SettingType: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBookSetting.SettingType",{
		deleteAll :0X00,
		updateAll :0x01,
		additional :0x02,
		modify :0x03	}),
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBookSetting.SettingType;},
		id: 1
	},
	books: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBook;},
		id: 2
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBook = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBook",{
	CallPermission: PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBook.CallPermission",{
		inPermission :0x00,
		outPermission :0x01,
		allPermission :0x02	}),
	perminssion: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.PhoneBook.CallPermission;},
		id: 1
	},
	phoneNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	},
	connector: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 3
	}});
