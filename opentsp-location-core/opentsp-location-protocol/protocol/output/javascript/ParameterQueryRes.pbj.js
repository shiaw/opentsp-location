"use strict";
/** @suppress {duplicate}*/var com;
if (typeof(com)=="undefined") {com = {};}
if (typeof(com.lc)=="undefined") {com.lc = {};}
if (typeof(com.navinfo.opentsp.platform.location)=="undefined") {com.navinfo.opentsp.platform.location = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol)=="undefined") {com.navinfo.opentsp.platform.location.protocol = {};}
if (typeof(com.navinfo.opentsp.platform.location.protocol.terminal)=="undefined") {com.navinfo.opentsp.platform.location.protocol.terminal = {};}
com.navinfo.opentsp.platform.location.protocol.terminal._PBJ_Internal="pbj-0.0.3";

com.navinfo.opentsp.platform.location.protocol.terminal.SettingIdentify= PROTO.Enum("com.navinfo.opentsp.platform.location.protocol.terminal.SettingIdentify",{
		trr_int_heartbeat :0x0001,
		trr_int_tcpResTime :0x0002,
		trr_int_tcpRetrans :0x0003,
		trr_int_udpResTime :0x0004,
		trr_int_udpRetrans :0x0005,
		trr_int_smsResTime :0x0006,
		trr_int_smsRetrans :0x0007,
		tcsa_str_masterApn :0x0010,
		tcsa_str_masterName :0x0011,
		tcsa_str_masterPasswd :0x0012,
		tcsa_str_masterIP :0x0013,
		tcsa_str_backupApn :0x0014,
		tcsa_str_backupName :0x0015,
		tcsa_str_backupPasswd :0x0016,
		tcsa_str_backupIP :0x0017,
		tcsa_str_tcpPort :0x0018,
		tcsa_str_udpPort :0x0019,
		rtp_int_reportTactics :0x0020,
		rtp_int_reportProgram :0x0021,
		rtp_int_driverNotLogin :0x0022,
		rtpi_int_sleepTime :0x0027,
		rtpi_int_urgentTime :0x0028,
		rtpi_int_defaultTime :0x0029,
		rtpi_int_defaultDis :0x002C,
		rtpi_int_notLoginDis :0x002D,
		rtpi_int_sleepDis :0x002E,
		rtpi_int_urgentDis :0x002F,
		rtpi_int_inflection :0x0030,
		pns_str_platformPhone :0x0040,
		pns_str_resetPhone :0x0041,
		pns_str_factoryResetPhone :0x0042,
		pns_str_PlatformsmsPhone :0x0043,
		pns_str_smsAlarmPhone :0x0044,
		pns_int_answerTactics :0x0045,
		pns_int_eachTalkTime :0x0046,
		pns_int_monthTalkTime :0x0047,
		pns_str_listenerPhone :0x0048,
		pns_str_privilegeSmsPhone :0x0049,
		aps_int_alarmMask :0x0050,
		aps_int_alarmSmsSwitch :0x0051,
		aps_int_alarmPhotoSwitch :0x0052,
		aps_int_alarmPhotoSave :0x0053,
		aps_int_alarmKeyStatus :0x0054,
		aps_int_maxSpeed :0x0055,
		aps_int_speedingTime :0x0056,
		aps_int_continuousTime :0x0057,
		aps_int_dayContinuousTime :0x0058,
		aps_int_minRestTime :0x0059,
		aps_int_maxParkingTime :0x005A,
		pvps_int_pictureQuality :0x0070,
		pvps_int_brightness :0x0071,
		pvps_int_contrast :0x0072,
		pvps_int_saturation :0x0073,
		pvps_int_chroma :0x0074,
		tis_int_mileageValues :0x0080,
		tis_int_provinceIdentify :0x0081,
		tis_int_cityIdentify :0x0082,
		tis_str_vehicleLicense :0x0083,
		tis_str_licenseColor :0x0084});
com.navinfo.opentsp.platform.location.protocol.terminal.SettingObject = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.SettingObject",{
	setId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.SettingIdentify;},
		id: 1
	},
	intParaContent: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int64;},
		id: 2
	},
	strParaContent: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.string;},
		id: 3
	}});
com.navinfo.opentsp.platform.location.protocol.terminal.ParameterQueryRes = PROTO.Message("com.navinfo.opentsp.platform.location.protocol.terminal.ParameterQueryRes",{
	serialNumber: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	setting: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return com.navinfo.opentsp.platform.location.protocol.terminal.SettingObject;},
		id: 2
	}});
