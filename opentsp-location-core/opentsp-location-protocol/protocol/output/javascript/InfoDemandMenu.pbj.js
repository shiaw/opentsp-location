"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.InfoDemandMenu = PROTO.Message("platform.auth.InfoDemandMenu",{
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.InfoDemandMenu.SettingType;},
		id: 1
	},
	infos: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.InfoDemandMenu.DemandInfo;},
		id: 2
	},
	SettingType: PROTO.Enum("platform.auth.InfoDemandMenu.SettingType",{
		deleteAll :0x00,
		updateMenu :0x01,
		additionalMenu :0x02,
		modifyMenu :0x03	}),
DemandInfo : PROTO.Message("platform.auth.InfoDemandMenu.DemandInfo",{
	infoType: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	infoName: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	}})
});
