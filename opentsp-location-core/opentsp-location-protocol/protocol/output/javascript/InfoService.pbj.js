"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.InfoService = PROTO.Message("platform.auth.InfoService",{
	infoType: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	infoContent: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	}});
