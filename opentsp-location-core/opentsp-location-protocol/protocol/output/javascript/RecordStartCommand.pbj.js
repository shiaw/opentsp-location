"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.RecordStartCommand = PROTO.Message("platform.auth.RecordStartCommand",{
	isRecord: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 1
	},
	isAlways: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	recordTime: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	status: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return SaveStatus;},
		id: 4
	},
	rates: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.RecordStartCommand.AudioRate;},
		id: 5
	},
	AudioRate: PROTO.Enum("platform.auth.RecordStartCommand.AudioRate",{
		rate_8k :0x00,
		rate_11k :0x01,
		rate_23k :0x02,
		rate_32k :0x03	})});
