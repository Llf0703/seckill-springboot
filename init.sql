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