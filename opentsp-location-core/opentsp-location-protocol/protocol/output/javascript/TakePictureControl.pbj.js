"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.TakePictureControl = PROTO.Message("platform.auth.TakePictureControl",{
	passageStatus: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.TakePictureControl.PassageStatus;},
		id: 1
	},
	status: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	timingInterval: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 3
	},
	unit: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return platform.auth.TakePictureControl.IntervalUnit;},
		id: 4
	},
PassageStatus : PROTO.Message("platform.auth.TakePictureControl.PassageStatus",{
	passageId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	isPhotoing: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 2
	},
	isSaving: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.bool;},
		id: 3
	}})
,
	IntervalUnit: PROTO.Enum("platform.auth.TakePictureControl.IntervalUnit",{
		timing_second :0x01,
		timing_minute :0x02,
		distance_meter :0x03,
		distance_kiloMiter :0x04	})});
