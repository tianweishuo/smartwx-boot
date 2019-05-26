/**
  订阅用户列表
 */
create table wxcms_subscribe_user(
  id varchar(64) NOT NULL DEFAULT '' comment '主键id',
  to_user_name varchar(50) not null comment '开发者微信号',
  from_user_name varchar(50) not null comment '发送方帐号（一个OpenID）',
  phone varchar(20) null comment '用户手机号',
  status int not null comment '1:关注,2:取关',
  delete_time datetime comment '上次取消关注时间',
  create_time datetime comment '创建时间(关注时间)',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '订阅用户列表';

/**
  关注取关列表
 */
create table wxcms_msg_event(
  id bigint(11) primary key auto_increment comment '自增id',
  to_user_name varchar(50) not null comment '开发者微信号',
  from_user_name varchar(50) not null comment '发送方帐号（一个OpenID）',
  msg_type varchar(10) not null comment '消息类型，event',
  event varchar(10) not null comment '事件类型，subscribe(订阅)、unsubscribe(取消订阅)',
  create_time datetime comment '创建时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '微信消息列表';


/*呼叫订单*/
create table call_order(
    order_id varchar(64) primary key comment '订单id',
    user_id varchar (50) not null comment '用户id',
    order_status int default 0 comment '订单状态 0-呼叫中,1-司机已接单,2-用户取消订单,3-司机取消订单,4-行程已结束',
    pay_status int default 0 comment '0-呼叫中或已取消,1-等待支付,2-已支付',
    start_label varchar(50) not null comment '开始地理位置信息',
    stat_location_x varchar(50) not null comment '开始地理位置维度',
    start_location_y varchar (50) not null comment '开始地理位置维度',
    end_label varchar(50) default null comment '结束地理位置信息',
    end_location_x varchar(50) default null comment '结束地理位置维度',
    end_location_y varchar (50) default null comment '结束地理位置维度',
    create_time timestamp not null comment '创建时间'
)