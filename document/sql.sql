
-- 创建数据库
create database api_center default character set utfbmb4;

-- 建表
create table service_info
(
    id              int auto_increment
        primary key,
    service_code    varchar(255) null,
    app_name        varchar(255) null,
    app_id          int          null,
    permission_code varchar(255) null,
    `desc`          varchar(255) null
);

