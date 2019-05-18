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