package com.navinfo.opentsp.platform.dp.core.rule;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS,include= JsonTypeInfo.As.PROPERTY,property="@class")
public interface RuleStatus {
	abstract void alarmHandle();
}
