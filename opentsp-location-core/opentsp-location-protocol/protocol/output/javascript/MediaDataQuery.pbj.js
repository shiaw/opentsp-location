"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.MediaDataQuery = PROTO.Message("platform.auth.MediaDataQuery",{
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return MediaType;},
		id: 1
	},
	isAllChannels: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	channels: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 3
	},
	events: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return EventCode;},
		id: 4
	},
	beginDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 5
	},
	endDate: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int64;},
		id: 6
	}});
