"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.VehicleControl = PROTO.Message("platform.auth.VehicleControl",{
	controlStatus: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	ControlStatus: PROTO.Enum("platform.auth.VehicleControl.ControlStatus",{
		doorSwitch :0x01	})});
