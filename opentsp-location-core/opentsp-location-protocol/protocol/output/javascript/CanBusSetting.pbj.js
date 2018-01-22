"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.CanBusSetting = PROTO.Message("platform.auth.CanBusSetting",{
	settings: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.CanBusSetting.ControlSetting;},
		id: 1
	},
	collects: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.CanBusSetting.SingleCollect;},
		id: 2
	},
	CanPassage: PROTO.Enum("platform.auth.CanBusSetting.CanPassage",{
		firstPassage :0x01,
		secondPassage :0x02	}),
	CanBusType: PROTO.Enum("platform.auth.CanBusSetting.CanBusType",{
		canBus :0x01,
		otherCanBus :0x02	}),
ControlSetting : PROTO.Message("platform.auth.CanBusSetting.ControlSetting",{
	passages: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.CanBusSetting.CanPassage;},
		id: 1
	},
	timingInterval: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 2
	},
	uploadInterval: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 3
	}})
,
SingleCollect : PROTO.Message("platform.auth.CanBusSetting.SingleCollect",{
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.CanBusSetting.CanBusType;},
		id: 1
	},
	timingInterval: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 2
	},
	passages: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return platform.auth.CanBusSetting.CanPassage;},
		id: 3
	},
	frameType: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 4
	},
	collectType: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bool;},
		id: 5
	},
	canBusIdentify: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 6
	}})
});
