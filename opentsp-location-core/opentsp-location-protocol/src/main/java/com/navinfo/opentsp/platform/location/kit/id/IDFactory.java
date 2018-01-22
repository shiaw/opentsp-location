package com.navinfo.opentsp.platform.location.kit.id;

import com.navinfo.opentsp.platform.location.kit.id.builder.SerialNumber;
import com.navinfo.opentsp.platform.location.kit.id.builder.TerminalAuthCode;
import com.navinfo.opentsp.platform.location.kit.id.builder.UniqueMark;

/**
 * ID工厂
 * 
 * @see SerialNumber
 * @see UniqueMark
 * @author lgw
 * 
 */
public final class IDFactory {
	private final static SerialNumber serialNumber = new SerialNumber();
	private final static UniqueMark UNIQUE_MARK = new UniqueMark();
	private final static TerminalAuthCode TERMINAL_AUTH_CODE = new TerminalAuthCode();

	/**
	 * 生成一个ID
	 * 
	 * @param idType
	 *            {@link IDType}
	 * @return Object
	 */
	public static Object builderID(IDType idType) {
		if (idType.ordinal() == IDType.SerialNumber.ordinal()) {
			return serialNumber.next();
		} else if (idType.ordinal() == IDType.ServiceUniqueMark.ordinal()) {
			return UNIQUE_MARK.next();
		} else if(idType.ordinal() == IDType.TerminalAuthCode.ordinal()) {
			return TERMINAL_AUTH_CODE;
		}else{
			return null;
		}
	}

	/**
	 * ID类型
	 * 
	 * @author aerozh-lgw
	 * 
	 */
	public static enum IDType {
		/** 流水号 */
		SerialNumber,
		/** 唯一标识 */
		ServiceUniqueMark,
		/**终端鉴权码*/
		TerminalAuthCode
	}
}
