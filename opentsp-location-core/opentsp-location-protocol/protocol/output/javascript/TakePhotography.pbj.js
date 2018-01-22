"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.TakePhotography = PROTO.Message("platform.auth.TakePhotography",{
	channels: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	commandType: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return CommandType;},
		id: 2
	},
	photoNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	takeInterval: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 4
	},
	saveStatus: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return SaveStatus;},
		id: 5
	},
	resolutions: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return Resolution;},
		id: 6
	},
	photoQuality: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 7
	},
	saturation: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 8
	},
	brightness: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 9
	},
	contrast: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 10
	},
	chroma: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 11
	}});
