CREATE DATABASE IF NOT EXISTS shopping_lists;
USE shopping_lists;

CREATE TABLE IF NOT EXISTS user (
    id int NOT NULL AUTO_INCREMENT,
    is_blocked bit(1) NOT NULL,
    is_enabled bit(1) NOT NULL,
    password varchar(255) NOT NULL,
    role enum('USER','ADMIN') DEFAULT NULL,
    username varchar(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username)
);

CREATE TABLE IF NOT EXISTS shopping_list (
    id int NOT NULL AUTO_INCREMENT,
    created datetime(6) NOT NULL,
    name varchar(255) NOT NULL,
    created_by_id int NOT NULL,
    PRIMARY KEY (id),
    KEY idx_shopping_list_created_by (created_by_id),
    CONSTRAINT fk_shopping_list_created_by FOREIGN KEY (created_by_id) REFERENCES user (id)
);


CREATE TABLE IF NOT EXISTS access_token (
    id int NOT NULL AUTO_INCREMENT,
    is_logged_out bit(1) NOT NULL,
    token varchar(255) NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY (id),
    KEY idx_access_token_user_id (user_id),
    CONSTRAINT fk_access_token_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS forgot_password (
    id int NOT NULL AUTO_INCREMENT,
    token varchar(255) NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY UK_forgot_password_user_id (user_id),
    CONSTRAINT fk_forgot_password_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS item (
    id int NOT NULL AUTO_INCREMENT,
    description varchar(255) DEFAULT NULL,
    is_bought bit(1) NOT NULL,
    name varchar(255) NOT NULL,
    quantity float NOT NULL,
    unit varchar(255) DEFAULT NULL,
    shopping_list_id int NOT NULL,
    PRIMARY KEY (id),
    KEY idx_item_shopping_list_id (shopping_list_id),
    CONSTRAINT fk_item_shopping_list_id FOREIGN KEY (shopping_list_id) REFERENCES shopping_list (id)
);

CREATE TABLE IF NOT EXISTS refresh_token (
    token_id int NOT NULL AUTO_INCREMENT,
    token varchar(500) NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY (token_id),
    UNIQUE KEY UK_refresh_token_user_id (user_id),
    CONSTRAINT fk_refresh_token_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS share (
    id int NOT NULL AUTO_INCREMENT,
    shopping_list_id int NOT NULL,
    user_with_share_id int NOT NULL,
    PRIMARY KEY (id),
    KEY idx_share_shopping_list_id (shopping_list_id),
    KEY idx_share_user_with_share_id (user_with_share_id),
    CONSTRAINT fk_share_user_with_share_id FOREIGN KEY (user_with_share_id) REFERENCES user (id),
    CONSTRAINT fk_share_shopping_list_id FOREIGN KEY (shopping_list_id) REFERENCES shopping_list (id)
);

CREATE DATABASE IF NOT EXISTS tests;