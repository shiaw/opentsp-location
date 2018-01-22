package com.navinfo.tasktracker.nilocation.entity;

import com.navinfo.tasktracker.nilocation.calculate.InterfaceResultCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用Http code 封装
 *
 * @author zhangy
 * @date 2016-03-02
 * @modify
 * @copyright Navi Tsp
 */
public enum ReturnCode implements InterfaceResultCode {
    /**
     * agree
     */
    OK(200, "OK"), NO_DATA(511, "NO DATA"), CLIENT_ERROR(507, "Client Error"), FORBIDDEN(510, "Forbidden"), SERVER_ERROR(
            506, "Server Error"), LOGIN_FAIL(509, "登录失败！"), USER_DNE(508, "User Does Not Exist"), USER_NOT_ONLINE(509,
            "User Not Online"), VALIDATE_MSG_FAIL(510, "参数校验失败"), BASECOMMAND_NOT_UIL(520,
            "BaseCommand 不允许为空 ."), PARA_MUST_CONTAIN_BASECOMMAND(521, "必须含有类型为BaseCommand的参数 ."), PARA_ERROR(522,
            "参数有误 ."), NI_CLOUD_ERROR(523, "调用位置云接口异常，异常信息："),

    /** 通用业务信息*/
    DEL_FAIL(512, "删除失败"), DATA_EXIST(513, "数据已经存在"), DATA_NO_EXIST(514, "数据不存在"),
    SYN_NO_SIGN_SWITH(515, "同步开关未开启，请联系管理员"), SYN_NO_BOSS_SWITH(516, "同步开关未开启，请联系管理员"),
    /**
     * ************************************************
     * 业务错误提示 Begin
     * **********************************************************************
     */

    // 业务返回信息，6位数，第6位:6 代表业务，第5位代表大分类（比如系统管理模块），第3,4位代表大分类里的菜单（比如用户管理），最后两位代表消息编码（比如用户管理的各种消息提示）
    ROOT_TEAM_CANNOT_DEL(650101, "不允许删除根节点"), DEALER_ALREADY_EXISTS(650102, "经销商代码重复"), ROOT_CAR_TEAM_CANNOT_SAVEORUPDATE(
            650103, "不允许新增或更改根节点"), DEALERNAME_ALREADY_EXISTS(650104, "经销商名称重复"), DEALER_CITYANDPROVICE_NOZERO(650105, "省市编码不能为0"),
    SING_SYSTEM_ERROR(650106, "标签系统服务端异常"), BASE_RUNTIME_EXCEPTIONSS(650107, "同步Tboss系统失败"),

    // 用户鉴权相关
    ERROR_USERNAME(610101, "用户名错误"), ERROR_NO_RIGHT_ACCESS(610102, "没有访问权限"), ACCOUNT_LOCKED(610103, "该账号已锁定，请联系管理员"), ACCOUNT_EXPIRED(
            610104, "该账号已过期，请联系管理员"), LOGIN_OUT_ERROR(610105, "用户登出失败"), TOKEN_FAIL(610106, "登入超时，系统将自动跳转到登录页面，重新登录 "), USER_NOT_EXIST(
            610107, "用户不存在"), OLD_PASSWORD_WRONG(610108, "原密码不正确或者密码已经被其他用户修改,请重新登陆"), GET_USER_INFO_ERROR(610109,
            "获取用户信息失败"), GET_GETVERIFYCODE_ERROR(610110, "获取验证码失败"), UPLOAD_GETVERIFYCODE_ERROR(610111, "上传验证码失败"), GETVERIFYCODE_VALIDATE_ERROR(
            610112, "验证码错误，请重新输入"), ERROR_PASSWORD(610113, "用户密码错误"), LOCK_USER(610114, "用户密码连续错误6次，该用户被锁定,请联系管理员解锁。"), VERIFYCODE_NOT_NULL(
            610115, "验证码不能为空"), CONFIRM_LOGIN(610116, "此账号已在其他地方登陆，确认在此登陆吗？"), OPEN_API_LOGIN_FAIL(610117, "第三方用户登陆失败"), SERVICE_STATION_CLOSED_LOGIN_FAIL(
            610118, "该服务站已被停用，你的账号暂时不能登录，请联系管理员"),

    // 终端型号
    TERMINA_MODEL_ALREADY_EXISTS(650201, "终端型号名称已经存在"), TERMINA_MODEL_ALREADY_BIND(650202, "终端型号已绑定，删除无效"), TERMINA_MODEL_NO_DATA(
            650203, "没有查询到终端型号数据"), ERROR_TERMINA_MODEL_TMNAME(650204, "待修改终端型号数据不正确"), ERROR_TERMINA_MODEL_IDNULL(650205,
            "终端型号不能为空"),
    // 销售区域
    TEAM_HAS_CHILDREN_CANNOT_DEL(650301, "该销售区域下有子区域或者经销商，不可以进行删除"), TEAM_HAS_CAR_CANNOT_DEL(650302, "车组下有有车不允许删除"), TEAM_HAS_TERMINAL_CANNOT_DEL(
            650303, "车组下有终端不允许删除"), TEAM_HAS_EMPLOYEE_CANNOT_DEL(650304, "车组下有人员不允许删除"), TEAM_HAS_SECDTEAM_CANNOT_DEL(
            650305, "车组下有二级经销商不允许删除"), TEAM_HAS_EXIST(650306, "该销售区域已经存在"), SECTEAM_SYN_FAIL(650307, "同步电子标签系统失败，请重新修改或联系管理员！"),
    // 客户管理
    BUSINESS_CODE_G_ISNOTNULL(650401, "身份证/驾驶证号不能为空"), BUSINESS_CODE_Q_ISNOTNULL(650402, "统一社会信用代码证号不能为空"), BUSINESS_CODE_G_ALREADY_BIND(
            650403, "身份证/驾驶证号已经存在"), BUSINESS_CODE_Q_ALREADY_BIND(650404, "统一社会信用代码证号已经存在"), BUSINESS_ALREADY_BIND_CAR_OR_ACCOUNT(
            650405, "该客户已创建车辆与账号信息，删除前先解除车辆与账号信息！"), BUSINESS_UPDATE_FAIL(650406, "客户修改失败"), BUSINESS_ADD_FAIL(650407,
            "客户添加失败"), BUSINESS_DELETE_FAIL(650408, "客户删除失败"), ORGANIZATION_CODE_ISNOTNULL(650409, "道路运输经营许可证号已经存在"), CERTIFICATE_CODE_ISNOTNULL(
            6504010, "从业资格证号已经存在"), VECHICLELIC_CODE_ISNOTNULL(6504011, "机动车行驶证号已经存在"), ROADTRANSLIC_CODE_ISNOTNULL(
            6504012, "道路运输证号已经存在"), ORGANIZATION_CODE_LENGTH_OUT(6504013, "道路运输经营许可证号支持1-18位字符"),
    ROADTRANSLIC_CODE_LENGTH_OUT(6504014, "道路运输证号支持1-18位字符"),
    // 终端管理
    EXISTED_TERMINAL_ID(650501, "终端ID已存在"), EXISTED_COMMUNICATION_ID(650502, "终端通讯号已存在"), EXISTED_DEVICE_LABEL_ID(
            650503, "设备标签ID已存在"), NO_AUTH_TO_ACCESS_TERMINAL(650504, "没有终端访问权限"), TERMINAL_NOT_EXISTED(650505, "终端不存在"), NO_TEAM_AUTH_OF_TERMINAL(
            650506, "没有分组访问权限"), HAVE_BIND_WITH_VEHICLE(650507, "终端已和车辆绑定"), NO_SUPPORT_TERMINAL_PROTOCOL(650508,
            "不支持的终端协议"), HAVE_NOT_BIND_WITH_VEHICLE(650509, "终端未和车辆绑定"), ADD_TERMINAL_FAILED(650510, "新增终端失败"), QUERY_TERMINAL_FAILED(
            650511, "查询终端失败"), DELETE_TERMINAL_FAILED(650512, "删除终端失败"), UPDATE_TERMINAL_FAILED(650513, "更新终端失败"), SEARCH_TERMINAL_FAILED(
            650514, "搜索终端失败"), SETTING_TERMINAL_FAILED(650515, "设置终端失败"), QUERY_TERMINAL_PROTOCOL_FAILED(650516, "查询终端协议失败"), SYNC_TERMINAL_FAILED(
            650517, "同步终端信息到位置云失败"), TERMINAL_MODEL_NOT_EXISTED(650518, "终端型号不存在"), DEALER_CODE_NOT_EXISTED(650519,
            "经销商编码没有匹配的经销商"), IMPORT_DATA_IS_NULL(650520, "导入表格数据为空"), IMPORT_TERMINAL_FAILED(650521, "导入终端失败"), TERMINAL_TYPE_IS_NULL(
            650522, "终端类型不能为空"), TERMINAL_OFFLINE(650523, "终端处于离线状态"), POSITION_SETTING_FAILED(650524, "位置汇报设置异常"), CAN_SETTING_FAILED(
            650525, "can汇报设置异常"), DRIVING_BEHAVIOR_FAILED(650526, "驾驶行为设置异常"), TERMINAL_INIT_FAILED(650527, "终端初始化参数设置异常"), TERMINAL_UPGRADE_FAILED(
            650528, "终端升级设置异常"), QUERY_TERMINAL_SETTING_PARAM_FAILED(650529, "查询终端参数设置目录异常"), EXISTED_SIM_NO(650530,
            "终端SIM卡已存在"), CAN_NOT_UPDATE_TEAM(650531, "终端已和车辆绑定,不能修改所属经销商"), TERMINAL_RECOVER(650532, "恢复删除终端"), EXPORT_TERMINAL_FAILED(65053, "导出终端失败"), UNKNOWN_ERROR(650599, "未知异常"), COMMUNICATION_ID_ALREADY_EXIST(
            180200, "终端通讯号已存在"), TERMINAL_ID_ALREADY_EXIST(180201, "终端ID已存在"),
    // 服务站管理
    ADD_STATION_FAILED(650601, "新增服务站失败"), DELETE_STATION_FAILED(650602, "删除服务站失败"), QUERY_STATION_DETAIL_FAILED(
            650603, "查询服务站详情失败"), QUERY_STATION_FAILED(650604, "查询服务站失败"), UPDATE_STATION_FAILED(650605, "更新服务站信息失败"), DELETE_SUBSTATION_FAILED(
            650606, "更新服务站信息失败"), STATION_NOT_EXISTED(650607, "服务站不存在"), HAS_SUBSTATION(650608, "该服务站下有二级服务站网点，不可删除"), EXISTED_STATION_NAME(
            650609, "服务站名称已存在"), EXISTED_STATION_CODE(650610, "服务站编码已存在"), HAS_BIND_TO_USER(650611, "该服务站有用户信息，不可进行删除"), QUERY_AUDIT_OF_STATION_FAILED(
            650612, "查询待审核的服务站信息失败"), UPDATE_AUDIT_OF_STATION_FAILED(650613, "更新服务站审核信息失败"), ENABLE_OR_DISABLE_STATION_FAILED(650614, "启用或停用服务站失败"),
    STATION_ALREADY_DISABLED(650615, "该服务站已经被停用，请联系管理员！"), SWITCH_STATUS_VALUE_EXCEPTION(650616, "switchStatus 值不合法(0：停用 1：启用)"), EXPORT_STATION_FAILED(650617, "导出服务站失败"),
    HAS_VEHICLE_BIND(650618, "该服务站存在车辆信息，不可进行删除"), STATION_SYNC_TO_TBOSS_FAILED(650619, "同步服务站到TBOSS系统失败"),
    STATION_SYNC_TO_TAG_FAILED(650620, "同步服务站信息到同步电子标签系统失败"), STATION_SYNC_FAILED(650621, "同步服务站失败"), BASE_RUNTIME_EXCEPTION(650622, "服务站运行时异常错误"),
    STATION_GETPROVINCEANDCITYCODE_FAILED(650623, "获取省市编码失败"),
    // 目录查询
    QUERY_MENU_FAILED(650701, "获取目录异常"),

    //车辆管理
    SYNC_SAVE_CARINFO_FAILED(650801, "车辆信息不存在或用户类型不存在,车辆云端同步失败 ."), SYNC_SAVE_CARINFO_URL_NOT_EMPTY(650802,
            "同步车辆信息的URl不允许为空"), CAR_MODEL_CODE_NOT_EMPTY(650803, "车型码不允许为空"),
    CLOUDE_MESSAGE_NULL(650804, "云端同步无返回信息"), CHASSIS_EXISTENCE(650805, "底盘号已存在"), DEL_CAR_FAILED(650806, "车辆删除失败"),
    SYNC_VEHICLE_TO_CLOUD_FAILED(650807, "车辆同云端失败"), CARID_NULL(
            650808, "车辆id不能为空"), CAR_UNBOUND_TERMINAL(650809, "车辆未绑定终端"),


    // 消息推送
    MASSAGE_PUSH_ERROR(620101, "消息推送异常"), MASSAGE_PUSH_CLIENT_CON_ERROR(620102, "没有与客户端建立连接"),

    MASSAGE_PUSH_TOKEN_ERROR(620103, "传入的token错误"),
    // 角色管理
    ROLE_LIST_FAILED(680801, "查询角色失败"), ROLE_GET_FAILED(680802, "查询角色详情失败"), ROLE_DEL_FAILED(680803, "删除角色失败"), ROLE_ADD_FAILED(
            680804, "添加角色失败,角色名称重复"), ROLE_EDIT_FAILED(680805, "修改角色失败,角色名称重复"), ROLE_PERMISSION_FAILED(680806, "查询角色权限树失败"),
    // 服务配件预约
    CHANGE_STATUS_FAILED(690901, "更新预约状态失败"), SEND_APP_FAILED(690902, "APP推送异常"), CHANGE_STATUS_ERROR(690903,
            "状态转换时新状态不正确"), CHANGE_INFO_NULL(690904, "更新预约状态时预约记录不存在"), CHANGE_STATUS_EXCEPTION(690905, "更新预约记录异常"), CHANGE_STATUS_WRONG(
            690906, "预约状态不正确"),

    // 用户管理
    USER_LIST_FAILED(700801, "查询用户失败"), USER_GET_FAILED(700802, "查询用户详情失败"), USER_DEL_FAILED(700803, "删除用户失败"), USER_ADD_FAILED(
            700804, "添加用户失败"), USER_EDIT_FAILED(700805, "修改用户失败"), USER_ROLE_ISNOT_EMPTY(700806, "角色不能为空"), USER_GROUP_ISNOT_EMPTY(
            700807, "组不能为空"), USER_STATION_ISNOT_EMPTY(700808, "服务站不能为空"), USER_CLIENT_ISNOT_EMPTY(700809, "所属客户不能为空"), USER_PASSWORD_INCONSISTENT(
            700810, "密码与确认密码不相同"), USER_PWD_MODIFY_FAILED(700811, "密码修改失败，旧密码错误"), USER_EXIST_FAILED(700812, "用户名已经存在"), USER_JOBTYPE_ISNOT_EMPTY(
            700813, "岗位不能为空"), USER_MAIL_ISNOT_EMPTY(700814, "用户邮箱不能为空"), ADMINISTRATOR_PASSWORD_ISNOT_MODIFY(700815, "超级管理员不可以进行密码修改"),
    // 监控模块
    TIME_BEGIN_AND_END_CHECK(800801, "开始时间不能大于结束时间"),
    // 操作日志
    QUERY_OPERATE_LOG_FAILED(630101, "查询操作日志异常"),
    // 终端日志
    QUERY_TERMINAL_LOG_FAILED(630201, "查询终端日志异常"),
    // 指令下发
    DISPATCH_MESSAGE_ERROR(630301, "发送调度短信异常"), DISPATCH_MESSAGE_TO_TCP_ERROR(630302, "发送消息到TCP模块异常"), OUT_REGION_TO_TCP_ERROR(
            630303, "发送出区域限速到TCP模块异常"), DEL_OUT_REGION_TO_TCP_ERROR(630304, "发送出区域限速删除到TCP模块异常"), MESSAGE_BROADCAST_IN_AREA_TO_TCP_ERROR(
            360305, "区域信息播报设置"), OVERTIME_PARKING_IN_AREA_TO_TCP_ERROR(360306, "区域滞留超时"), VEHICLE_PASS_IN_AREA_TO_TCP_ERROR(
            360307, "区域车次统计"),
    // 风险防控
    ADD_RISK_REGION_FAILED(630301, "新增防控区域失败"), DELETE_RISK_REGION_FAILED(630302, "删除防控区域失败"), QUERY_RISK_REGION_FAILED(
            630303, "查询防控区域失败"), UPDATE_RISK_REGION_FAILED(630304, "更新防控区域失败"), ADD_RISK_RULE_FAILED(630305, "新增防控规则失败"), DELETE_RISK_RULE_FAILED(
            630306, "删除防控规则失败"), QUERY_RISK_RULE_FAILED(630307, "查询防控规则失败"), UPDATE_RISK_RULE_FAILED(630308, "更新防控规则失败"), RISK_REGION_EXISTED(
            630309, "防控区域名称已存在"), NO_AUTH_TO_ACCESS_RISK(6303010, "没有该防控区域访问权限"), NOT_EXISTED_REGION(6303011, "防控区域不存在"), NO_TEAM_AUTH_OF_RISK_REGION(
            6303012, "没有该防控区所在分组访问权限"), ALREADY_EXISTED_RISK_RULE(6303013, "车辆在改防控区域已设置了防控规则"), RISK_RULE_NOT_EXISTED(
            630314, "防控规则不存在"), QUERY_REPORT_OF_RISK_REGION_FAILED(630315, "查询风险防控报表失败"),

    // 报表
    QUERY_STATISTIC_OIL_ERROR(670101, "查询油耗报表异常"), QUERY_STATISTIC_OIL_CHART_ERROR(670102, "查询油耗图表数据异常"), EXPORT_STATISTIC_OIL_ERROR(
            670103, "导出油耗图表数据异常"), QUERY_STATISTIC_ONLINE_ERROR(670201, "查询同步上线报表报表异常"), EXPORT_STATISTIC_ONLINE_ERROR(
            670202, "导出同步上线报表数据异常"), IGNORE_STATION_OVERTIME_WARN_ERROR(670203, "忽略操作失败"),
    EXPORT_EMAIL_ISNOT_EMPTY(670205, "您导出的数据条数超过<font color=\"red\">{0}</font>条,为了不影响您的操作,<br>我们会把导出数据链接发送到您的邮箱,您可以选择时间进行下载导出,<br>为了方便您以后导出,请进入用户管理,补充自己的邮箱信息。"),
    EXPORT_ASYNC_SUCCESS(670206, "您导出的数据条数超过<font color=\"red\">{0}</font>条,为了不影响您的操作,<br>我们会把导出数据链接发送到您的邮箱,您可以选择时间进行下载导出,<br>Email:<font color=\"red\">{1}</font>"),
    INVOICE_NUMBER_AREADY_EXIST(851001,
            "发票号已存在"), NO_AUTHORITY_VIEW_SCANREPORT(851002, "当前用户无查看车辆扫码报表权限"), IMPORT_SCANREPORT_FAILED(851003,
            "数据为空,导入空入车辆扫码信息失败"), IMPORT_SCANREPORT_DUPLICATE(851004, "数据重复导入"), NO_IMPORT_SCANREPORT(851005, "没有可导出的数据"),
    IMPORT_EMAIL_ISNOT_EMPTY(670207, "1、你导入的数据条数，超过<font color=\"red\">{0}</font>条，为了不影响你其他的操作，我们会在后台执<br>\n" +
            "行导入，3-10分钟后，我们会把导入的结果发送到你的邮箱中。请及时查看。<br>\n" +
            "2、为了方便以后的导入，请进入用户管理，补充自己的邮箱。"),
    IMPORT_ASYNC_SUCCESS(670208, "您导入的数据条数超过<font color=\"red\">{0}</font>条,为了不影响您其他的操作,我们会在后台执行导<br>入，3-10分钟后，" +
            "我们会把导入的结果发送到您的邮箱中,请及时查看。<br>Email:<font color=\"red\">{1}</font>"),
    IMPORT_SCANCODE_SYNC_SWITCH_OFF(670209, "同步开关未开启，请联系管理员"), IMPORT_SCANCODE_SYNC_DTS_FAILED(670210, "TDS同步失败"),
    IMPORT_SCANCODE_SYNC_DTS_FAILED_DATA_EMPTY(670211, "TDS同步失败,车辆同步信息不存在"), SYNC_MES_ERROR_DATA_SCHEDULE_FAILED(670212, "同步MES错误数据定时任务执行失败"),
    EXPORT_FILE_SUCCESS(670212, "文件下载成功"),
    /*************************************************** 业务错误提示 End ************************************************************************/

    /**
     * used in cases when data was concurrently modified, for example when version value of passed data differ from
     * version value of stored data.
     */
    CONFLICT(409, "Conflict"),;

    private final int code;

    private String message;

    private static Map<Integer, ReturnCode> codes = new ConcurrentHashMap<Integer, ReturnCode>();

    static {
        for (ReturnCode errorCodeEnum : ReturnCode.values()) {
            codes.put(errorCodeEnum.code(), errorCodeEnum);
        }
    }

    ReturnCode(int code) {
        this.code = code;
    }

    ReturnCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public int code() {
        return code;
    }
    @Override
    public String message() {
        return message;
    }

    public static ReturnCode valueOf(int code) {
        if (codes.get(code) != null) {
            return codes.get(code);
        } else {
            return UNKNOWN_ERROR;
        }
    }

}
