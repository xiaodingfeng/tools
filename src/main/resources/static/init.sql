-- ALTER TABLE T_CHAT_CATEGORY   ALTER COLUMN ico VARCHAR(500);
DROP TABLE IF EXISTS t_images;
CREATE TABLE t_images
(
    id             number(20) PRIMARY KEY auto_increment,
    category       VARCHAR(50),
    description    VARCHAR(200),
    file_name      VARCHAR(200),
    real_file_name VARCHAR(200),
    real_url            VARCHAR(200),
    type            VARCHAR(100),
    poster            VARCHAR(200),
    url            VARCHAR(200),
    md5            VARCHAR(100),
    size            VARCHAR(50),
    pixel            VARCHAR(50),
    create_time       datetime,
    update_time  datetime
);
ALTER TABLE t_images  ADD COLUMN poster VARCHAR(200);
ALTER TABLE t_images  ADD COLUMN real_url VARCHAR(200);
ALTER TABLE t_images  ADD COLUMN type VARCHAR(100);
-- 用户信息
DROP TABLE IF EXISTS t_sys_user;
CREATE TABLE t_sys_user
(
    id             number(20) PRIMARY KEY auto_increment,
    user_name       VARCHAR(50),
    nick_name       VARCHAR(50),
    email VARCHAR(50),
    password  VARCHAR(50),
    avatar    VARCHAR(100),
    create_time       datetime,
    update_time       datetime
);
-- chat 系统配置
DROP TABLE IF EXISTS t_chat_config;
CREATE TABLE t_chat_config
(
    id             number(20) PRIMARY KEY auto_increment,
    config_key  VARCHAR(255),
    config_name  VARCHAR(255),
    config_value       VARCHAR(255),
    valid  number(1)
);
-- 用户额度
DROP TABLE IF EXISTS t_chat_user_balance;
CREATE TABLE t_chat_user_balance
(
    id             number(20) PRIMARY KEY auto_increment,
    user_id  number(20),
    chat_balance       number(20),
    create_time       datetime,
    update_time       datetime
);
-- vip信息
DROP TABLE IF EXISTS t_chat_order;
CREATE TABLE t_chat_order
(
    id  number(20) PRIMARY KEY AUTO_INCREMENT,
    user_id  number(20),
    order_date datetime,
    order_status VARCHAR(20),
    vip_price_id number(20),
    quantity INT,
    price INT,
    total_amount INT,
    payment_method VARCHAR(20),
    notes VARCHAR(255),
    cancellation_reason VARCHAR(255),
    completion_date datetime,
    last_updated datetime
);
-- vip信息
DROP TABLE IF EXISTS t_chat_vip;
CREATE TABLE t_chat_vip
(
    id             number(20) PRIMARY KEY auto_increment,
    name  VARCHAR(50),
    description       VARCHAR(50),
    valid  number(1),
    sort       number(2),
    options       VARCHAR(100),
    create_time       datetime,
    update_time       datetime
);
-- vip价格
DROP TABLE IF EXISTS t_chat_vip_price;
CREATE TABLE t_chat_vip_price
(
    id             number(20) PRIMARY KEY auto_increment,
    vip_id  number(20),
    title       VARCHAR(50),
    date_type  VARCHAR(5),
    date_count number(2),
    description       VARCHAR(50),
    old_price  number(20),
    price  number(20),
    sort       number(2),
    valid  number(1)
);
-- chat免费使用配置
DROP TABLE IF EXISTS t_chat_free_config;
CREATE TABLE t_chat_free_config
(
    id             number(20) PRIMARY KEY auto_increment,
    receive_balance  number(20),
    date_type VARCHAR(5),
    valid  number(1)
);
insert into t_chat_config(id, config_key, config_name, config_value, valid)
values (1, 'chat_need_balance','chat balance','2',1 );
insert into t_chat_vip(id, name, description, valid, sort, options, create_time, update_time)
values (1, '王者会员','基础会员',1,1,'{ dateType: "DAY",receiveBalance: "100" }','2023-01-01 00:00:00', '2023-01-01 00:00:00');
insert into t_chat_vip(id, name, description, valid, sort, options, create_time, update_time)
values (2, '荣耀会员','高级会员',1,2,'{ dateType: "DAY",receiveBalance: "200" }','2023-01-01 00:00:00', '2023-01-01 00:00:00');
insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 1, 1, '一周试用套餐', 'WEEK', 1, '一周试用，防止跑路', 1200, 999,1,1 );
insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 2, 1, '月套餐', 'MONTH', 1, '一个月更加优惠', 4800, 2999,2,1 );
insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 3, 1, '季度套餐', 'MONTH', 3, '一个月更加优惠', 14400, 6999,3,1 );

insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 4, 2, '一周试用套餐', 'WEEK', 1, '一周试用，防止跑路', 1400, 1155,1,1 );
insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 5, 2, '月套餐', 'MONTH', 1, '一个月更加优惠', 5600, 4620,2,1 );
insert into t_chat_vip_price(id, vip_id, title, date_type, date_count, description, old_price, price, sort, valid)
values ( 6, 2, '季度套餐', 'MONTH', 3, '一个月更加优惠', 16800, 7999,3,1 );

insert into t_chat_free_config(id, receive_balance, date_type, valid) VALUES (1, 50,'DAY',1);
-- 用户vip信息
DROP TABLE IF EXISTS t_chat_user_vip;
CREATE TABLE t_chat_user_vip
(
    id             number(20) PRIMARY KEY auto_increment,
    vip_id  number(20),
    user_id       number(20),
    start_time  datetime,
    end_time    datetime,
    create_time       datetime,
    update_time       datetime
);
-- 画图用户任务信息
DROP TABLE IF EXISTS t_chat_draw_app;
CREATE TABLE t_chat_draw_app
(
    id             number(20) PRIMARY KEY auto_increment,
    mode       VARCHAR(50),
    config    VARCHAR(2500),
    prompt      VARCHAR(2500),
    status number(1),
    user_id            number(20),
    create_time       datetime,
    update_time  datetime
);
-- 画图图片地址
DROP TABLE IF EXISTS t_chat_draw_picture;
CREATE TABLE t_chat_draw_picture
(
    id             number(20) PRIMARY KEY auto_increment,
    draw_app_id number(20),
    name       VARCHAR(50),
    result    VARCHAR(100),
    prompt      VARCHAR(2500),
    create_time       datetime,
    update_time  datetime
);
-- 真实图片地址
DROP TABLE IF EXISTS t_chat_draw_picture_real;
CREATE TABLE t_chat_draw_picture_real
(
    id             number(20) PRIMARY KEY auto_increment,
    draw_picture_id      VARCHAR(2500),
    result    VARCHAR(100),
    create_time       datetime,
    update_time  datetime
);
-- 模型分类
DROP TABLE IF EXISTS t_chat_category;
CREATE TABLE t_chat_category
(
    id             number(20) PRIMARY KEY auto_increment,
    name      VARCHAR(50),
    description    VARCHAR(500),
    ico    VARCHAR(500),
    sort      number(4),
    create_time       datetime,
    update_time  datetime
);
-- prompts模型列表
DROP TABLE IF EXISTS t_chat_model;
CREATE TABLE t_chat_model
(
    id             number(20) PRIMARY KEY auto_increment,
    cate_id      number(20),
    ico    VARCHAR(500),
    title    VARCHAR(100),
    description    VARCHAR(500),
    system_prompts   VARCHAR(500),
    sort      number(4),
    create_time       datetime,
    update_time  datetime
);

-- 模型初始化 话语提示列表
DROP TABLE IF EXISTS t_chat_model_init_words;
CREATE TABLE t_chat_model_init_words
(
    id             number(20) PRIMARY KEY auto_increment,
    model_id      number(20),
    prompts    VARCHAR(500),
    create_time       datetime,
    update_time  datetime
);
-- 聊天信息
DROP TABLE IF EXISTS t_chat_message;
CREATE TABLE IF NOT EXISTS t_chat_message (
                                            id BIGINT PRIMARY KEY auto_increment,
                                            user_id INT NOT NULL,
                                            message_id VARCHAR(255) NOT NULL,
    parent_message_id VARCHAR(255),
    parent_answer_message_id VARCHAR(255),
    parent_question_message_id VARCHAR(255),
    context_count BIGINT NOT NULL,
    question_context_count BIGINT NOT NULL,
    message_type INT NOT NULL,
    chat_room_id BIGINT NOT NULL,
    conversation_id VARCHAR(255),
    api_type VARCHAR(20) NOT NULL,
    model_name VARCHAR(50),
    api_key VARCHAR(255),
    content CLOB NOT NULL,
    original_data CLOB,
    response_error_data CLOB,
    prompt_tokens BIGINT,
    completion_tokens BIGINT,
    total_tokens BIGINT,
    ip VARCHAR(255),
    status INT NOT NULL,
    is_hide BOOLEAN NOT NULL DEFAULT FALSE,
    create_time TIMESTAMP NOT NULL,
    update_time TIMESTAMP,
    CONSTRAINT uk_message_id UNIQUE (message_id)
    );

-- 会话信息
DROP TABLE IF EXISTS t_chat_room;
CREATE TABLE IF NOT EXISTS t_chat_room (
                                         id BIGINT PRIMARY KEY auto_increment,
                                         user_id INT NOT NULL,
                                         ip VARCHAR(255),
    conversation_id VARCHAR(255),
    first_chat_message_id BIGINT,
    first_message_id VARCHAR(255),
    title VARCHAR(5000) NOT NULL,
    description VARCHAR(5000),
    api_type VARCHAR(20) NOT NULL,
    create_time TIMESTAMP NOT NULL,
    update_time TIMESTAMP,
    model_id INT NOT NULL,
    his_num INT,
    is_hide BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_first_chat_message_id UNIQUE (first_chat_message_id),
    CONSTRAINT uk_first_message_id UNIQUE (first_message_id),
    CONSTRAINT uk_conversation_id UNIQUE (conversation_id)
    );
--敏感词
DROP TABLE IF EXISTS t_sensitive_word;
CREATE TABLE IF NOT EXISTS t_sensitive_word (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              word VARCHAR(255) NOT NULL,
    status INT NOT NULL,
    is_deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

DROP TABLE IF EXISTS t_chat_group_record;
-- 聊天室模块
CREATE TABLE t_chat_group_record (
      id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
      group_id BIGINT DEFAULT NULL COMMENT '群组id',
      user_id BIGINT DEFAULT NULL COMMENT '用户id',
      content CLOB NOT NULL COMMENT '聊天内容',
      ip_address VARCHAR(50) COMMENT 'ip地址',
      ip_source VARCHAR(255) COMMENT 'ip来源',
      type number(1) COMMENT '类型',
      create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
      update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

