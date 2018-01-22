"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.MediaDataQueryRes = PROTO.Message("platform.auth.MediaDataQueryRes",{
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
	queryData: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.MediaDataQueryRes.MediaQueryData;},
		id: 3
	},
MediaQueryData : PROTO.Message("platform.auth.MediaDataQueryRes.MediaQueryData",{
	mediaId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 1
	},
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return MediaType;},
		id: 2
	},
	channels: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	events: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return EventCode;},
		id: 4
	},
	locationData: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return LocationData;},
		id: 5
	}})
});
