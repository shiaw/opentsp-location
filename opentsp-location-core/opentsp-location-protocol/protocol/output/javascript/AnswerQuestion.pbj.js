"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.AnswerQuestion = PROTO.Message("platform.auth.AnswerQuestion",{
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
	answerId: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 3
	}});
