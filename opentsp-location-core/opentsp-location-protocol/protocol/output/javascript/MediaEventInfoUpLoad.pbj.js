"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.MediaEventInfoUpLoad = PROTO.Message("platform.auth.MediaEventInfoUpLoad",{
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
	encode: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return FormatEncoding;},
		id: 3
	},
	events: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return EventCode;},
		id: 4
	},
	channels: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 5
	}});
