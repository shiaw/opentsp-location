

CREATE TABLE task (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  original_id varchar(255) NOT NULL,
  message text,
  router varchar(255) NOT NULL,
  router_argument text,
  persistent bit(1) NOT NULL,
  ttl bigint NOT NULL,
  delivery_methods varchar(1024),
  listener_queue varchar(255),
  ended bit(1),
  stored_time datetime,
  PRIMARY KEY (id),
  CONSTRAINT UK_task__original_id UNIQUE KEY (original_id)
);

CREATE TABLE delivering (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  original_id int NOT NULL,
  task_id bigint(20) NOT NULL REFERENCES task (id),
  recipient varchar(1024) NOT NULL,
  status int,
  last_method varchar(255),
  callback_id varchar(255),
  PRIMARY KEY (id),
  CONSTRAINT UK_delivering__task_id__original_id UNIQUE KEY (task_id, original_id),
  CONSTRAINT FK_delivering__task_id FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE
);

CREATE TABLE `lc_terminal_info` (
  `TI_ID` int(11) NOT NULL COMMENT 'ID自动增长',
  `TERMINAL_ID` bigint(20) NOT NULL COMMENT '终端唯一标识',
  `PROTO_CODE` int(11) NOT NULL COMMENT '协议类型（终端协议编码）',
  `NODE_CODE` int(11) default NULL COMMENT '节点编号，程序控制根据节点类型自增',
  `DISTRICT` int(11) NOT NULL COMMENT '内部编码（服务区域）',
  `CREATE_TIME` int(11) default NULL COMMENT '创建时间',
  `DATA_STATUS` tinyint(4) NOT NULL COMMENT '数据状态 1 正常；0 删除',
  `REGULAR_IN_TERMINAL` tinyint(4) default NULL,
  `DEVICE_ID` varchar(32) default NULL,
  `CHANGE_TID` bigint(20) default NULL,
  `BUSINESS_TYPE` int(11) default '0' COMMENT '0默认，1江淮',
  PRIMARY KEY  (`TI_ID`),
  UNIQUE KEY `TERMINAL_ID` (`TERMINAL_ID`),
  KEY `TI_ID` (`TI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='终端信息表';

CREATE TABLE `lc_terminal_register` (
  `REGISTER_ID` int(11) NOT NULL auto_increment COMMENT 'ID自动增长',
  `TERMINAL_ID` bigint(20) NOT NULL COMMENT '终端唯一标识',
  `AUTH_CODE` varchar(128) default NULL COMMENT '鉴权码',
  `PROVINCE` int(11) default NULL COMMENT '省域 ID',
  `CITY` int(11) default NULL COMMENT '市县域 ID',
  `PRODUCT` varchar(16) default NULL COMMENT '制造商 ID',
  `TERMINAL_TYPE` varchar(16) default NULL COMMENT '终端型号',
  `TERMINAL_SN` varchar(32) default NULL COMMENT '终端编号',
  `LICENSE_COLOR` tinyint(4) default NULL COMMENT '车牌颜色',
  `LICENSE` varchar(32) default NULL COMMENT '车牌',
  PRIMARY KEY  (`REGISTER_ID`),
  UNIQUE KEY `TERMINAL_ID` (`TERMINAL_ID`),
  UNIQUE KEY `AUTH_CODE` (`AUTH_CODE`),
  KEY `REGISTER_ID` (`REGISTER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='终端注册信息表';
