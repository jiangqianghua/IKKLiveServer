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
    `exp` int default 0 comment '经验值',
     primary key(`user_account`)
 ) comment '用户信息表';


CREATE TABLE  `room_info`(
    `room_id` int not nuLL auto_increment comment '房间号',
    `user_id` varchar(64) not null comment '用户id',
    `user_name` varchar(64) default '' comment '用户名称',
    `user_avater` varchar(512) default '' comment '头像',
    `live_cover` varchar(512) default '' comment '直播封面',
    `live_title` varchar(512) not null comment '房间主题',
    `watcher_num` int not null default 0 comment '观看人数',
    primary key(`room_id`)
)comment '房间信息表';
