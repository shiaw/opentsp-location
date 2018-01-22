"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.OilCircuitControl = PROTO.Message("platform.auth.OilCircuitControl",{
	types: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.OilCircuitControl.ControlType;},
		id: 1
	},
	status: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	ControlType: PROTO.Enum("platform.auth.OilCircuitControl.ControlType",{
		oil :0x00,
		circuit :0x01	})});
