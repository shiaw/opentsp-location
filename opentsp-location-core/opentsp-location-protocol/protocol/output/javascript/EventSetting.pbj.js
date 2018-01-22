"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.EventSetting = PROTO.Message("platform.auth.EventSetting",{
	controlStatus: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.EventSetting.SettingType;},
		id: 1
	},
	events: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.EventSetting.EventObject;},
		id: 2
	},
	SettingType: PROTO.Enum("platform.auth.EventSetting.SettingType",{
		deleteAll :0x00,
		updateAll :0x01,
		Additional :0x02,
		Modify :0x03,
		deletePart :0x04	}),
EventObject : PROTO.Message("platform.auth.EventSetting.EventObject",{
	eventId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	eventContent: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	}})
});
