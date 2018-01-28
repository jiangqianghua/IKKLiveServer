-- live data
CREATE DATABASE IF NOT EXISTS kkdb DEFAULT charset utf8 COLLATE utf8_general_ci;

CREATE TABLE `user_profile`(
    `user_account` varchar(64) not null comment '账号',
    `user_password` varchar(64) not null comment '密码',
    `user_header` varchar(512) comment '头像',
    `user_nick` varchar(64) comment '昵称',
    `user_gender` varchar(32) default "0" comment '性别 1 男，2女， 0未知',
    `user_sign` varchar(512) default '' comment '签名',
    `user_renzheng` varchar(32) default '0'comment '认证 0是未认证',
    `user_location` varchar(512) default '' comment '地区',
    `user_level` int default 0 comment '等级',
    `user_getnum` int default 0 comment '获取票数',
    `user_sendnum` int default 0 comment '送出票数',
     primary key(`user_account`)
 ) comment '用户信息表';

