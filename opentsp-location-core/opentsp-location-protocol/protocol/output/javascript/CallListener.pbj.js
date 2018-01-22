"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.CallListener = PROTO.Message("platform.auth.CallListener",{
	phoneNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 1
	},
	status: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.CallListener.ListenerStatus;},
		id: 2
	},
	ListenerStatus: PROTO.Enum("platform.auth.CallListener.ListenerStatus",{
		calling :0x00,
		listening :0x01	})});
