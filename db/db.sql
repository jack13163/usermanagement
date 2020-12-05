## 创建用户表
create table t_user(
    uid int(10) not null auto_increment comment '用户编号',
    uname varchar(100) not null comment '用户名',
    pwd varchar(100) not null comment '用户密码',
    salt varchar(100) comment '密码加盐值',
    email varchar(100) not null comment '电子邮箱地址',
    status int(3) not null default -1 comment '用户状态  -1:未通过邮箱认证 0:未锁定状态; -2:代表锁定状态',
    primary key(uid)
);

insert into t_user(uname, pwd, salt, email, status) values('jack','123','dfjaskfjd88jfj','2463246583@qq.comn', -1);
insert into t_user(uname, pwd, salt, email, status) values('root','123','dfasuu8894jkjfd','2463246583@qq.comn', 0);
insert into t_user(uname, pwd, salt, email, status) values('admin','123','dfasuu8894jkjfd','2463246583@qq.comn', 0);

## 用户登录信息记录表
CREATE TABLE `t_user_login`(
    `id` int(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `uid` int(10) UNSIGNED NOT NULL COMMENT '用户编号',
    `ipaddr` varchar(30) NOT NULL COMMENT '用户登陆IP',
    `logintime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户登陆时间',
    `pass_wrong_time_status` tinyint(10) UNSIGNED NOT NULL COMMENT '登陆密码错误状态: 0 正确 2错误'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


