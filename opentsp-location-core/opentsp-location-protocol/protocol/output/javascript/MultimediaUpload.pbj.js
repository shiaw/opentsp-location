"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.MultimediaUpload = PROTO.Message("platform.auth.MultimediaUpload",{
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
	},
	locationData: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return LocationData;},
		id: 6
	},
	mediaData: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.bytes;},
		id: 7
	}});
