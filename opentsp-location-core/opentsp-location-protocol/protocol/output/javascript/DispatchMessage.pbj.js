"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.DispatchMessage = PROTO.Message("platform.auth.DispatchMessage",{
	messageContent: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 1
	},
	signs: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.DispatchMessage.MessageSign;},
		id: 2
	},
MessageSign : PROTO.Message("platform.auth.DispatchMessage.MessageSign",{
	isUrgent: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 1
	},
	isDisplay: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	isBroadcast: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 3
	},
	isAdvertiseScreen: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 4
	},
	infoType: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 5
	}})
});
