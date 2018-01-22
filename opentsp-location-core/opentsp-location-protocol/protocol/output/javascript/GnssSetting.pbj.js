"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.GnssSetting = PROTO.Message("platform.auth.GnssSetting",{
	locationMode: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return platform.auth.GnssSetting.LocationMode;},
		id: 1
	},
	baudRate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return platform.auth.GnssSetting.BaudRate;},
		id: 2
	},
	locationRate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return platform.auth.GnssSetting.LocationRate;},
		id: 3
	},
	collectRate: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 4
	},
	uploadSetting: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return UpLoadSetting;},
		id: 5
	},
LocationMode : PROTO.Message("platform.auth.GnssSetting.LocationMode",{
	isGps: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 1
	},
	isBeidou: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	isGlonass: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 3
	},
	isGalileo: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 4
	}})
,
	BaudRate: PROTO.Enum("platform.auth.GnssSetting.BaudRate",{
		rate4800 :0x00,
		rate9600 :0x01,
		rate19200 :0x02,
		rate38400 :0x03,
		rate57600 :0x04,
		rate115200 :0x05	}),
	LocationRate: PROTO.Enum("platform.auth.GnssSetting.LocationRate",{
		ms500 :0x00,
		ms1000 :0x01,
		ms2000 :0x02,
		ms3000 :0x03,
		ms4000 :0x04	}),
UploadSetting : PROTO.Message("platform.auth.GnssSetting.UploadSetting",{
	modes: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.GnssSetting.UploadMode;},
		id: 1
	},
	uploadInterval: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 2
	}})
,
	UploadMode: PROTO.Enum("platform.auth.GnssSetting.UploadMode",{
		localSave :0x00,
		timing :0x01,
		distance :0x02,
		accumulatedTiming :0x0B,
		accumulatedDistance :0x0C,
		accumulatedNumber :0x0D	})});
