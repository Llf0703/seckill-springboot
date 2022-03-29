CREATE TABLE user
(
    id int primary key NOT NULL AUTO_INCREMENT,
    created_at datetime NOT NULL,
    updated_at datetime NOT NULL,
    deleted_at datetime,
    user_name varchar(25) NOT NULL,
    name varchar(20) NOT NULL,
    password varchar(255) NOT NULL,
    email varchar(50),
    phone varchar(50) NOT NULL,
    id_card varchar(50) NOT NULL,
    balance decimal,
    age datetime NOT NULL,
    employment_status int NOT NULL,
    credit_status int NOT NULL
);

CREATE TABLE recharge_record
(
    id int primary key NOT NULL AUTO_INCREMENT,
    created_at datetime NOT NULL,
    updated_at datetime NOT NULL,
    deleted_at datetime,
    user_id int not null,
    FOREIGN KEY fk_user_id(user_id)
    REFERENCES user(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
    recharge_amount decimal NOT NULL,
    recharge_method int NOT NULL
);

CREATE TABLE manager_user
(
    id int primary key NOT NULL AUTO_INCREMENT,
    created_at datetime NOT NULL,
    updated_at datetime NOT NULL,
    deleted_at datetime,
    account varchar(50) NOT NULL,
    password varchar(255) NOT NULL,
    seckill_items_permissions int NOT NULL,
    seckill_record_permissions int NOT NULL,
    recharge_record_permissions int NOT NULL,
    admin_info_permissions int NOT NULL,
    financial_items_permissions int NOT NULL,
    risk_control_permissions int NOT NULL,
    guest_info_permissions int NOT NULL
);

insert into manager_user (created_at, updated_at, account, password, seckill_items_permissions, seckill_record_permissions, recharge_record_permissions, admin_info_permissions, financial_items_permissions, risk_control_permissions, guest_info_permissions)
values
(NOW(), NOW(), 'LLF0703', '15e72c0b5047a5aca24fe15071ffc50a', 2, 0, 0, 0, 0, 0, 0);

CREATE TABLE `seckill_items` (
  `id` int primary key NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `stock` bigint NOT NULL,
  `amount` bigint NOT NULL,
  `description` varchar(255) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `remaining_stock` bigint NOT NULL,
  `financial_item_id` int NOT NULL,
  `risk_control_id` int NOT NULL
);