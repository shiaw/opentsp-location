"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.SaveMultimediaUpload = PROTO.Message("platform.auth.SaveMultimediaUpload",{
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return MediaType;},
		id: 1
	},
	channels: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 2
	},
	events: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return EventCode;},
		id: 3
	},
	beginDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 4
	},
	endDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 5
	},
	isDelete: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 6
	}});
