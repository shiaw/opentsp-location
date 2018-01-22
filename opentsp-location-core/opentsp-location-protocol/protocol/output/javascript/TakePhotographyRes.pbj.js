"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.TakePhotographyRes = PROTO.Message("platform.auth.TakePhotographyRes",{
	serialNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	result: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return ResponseResult;},
		id: 2
	},
	results: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.TakePhotographyRes.PhotoResult;},
		id: 3
	},
	mediaIdentify: {
		options: {packed:true},
		multiplicity: PROTO.repeated,
		type: function(){return PROTO.int64;},
		id: 4
	},
	PhotoResult: PROTO.Enum("platform.auth.TakePhotographyRes.PhotoResult",{
		photo_success :0x00,
		photo_failed :0x01,
		photo_channelNotSupport :0x02	})});
