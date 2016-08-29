CREATE DATABASE philia;

CREATE USER 'philiauser'@'localhost' IDENTIFIED BY 'pH1Li8';
GRANT ALL PRIVILEGES ON *.* TO 'philiauser'@'localhost';

  drop table if exists authorities;
  drop table if exists group_members;
  drop table if exists group_authorities;
  drop table if exists groups;
  drop table if exists oauth_refresh_token;
  drop table if exists oauth_access_token;
  drop table if exists oauth_client_token;
  drop table if exists oauth_client_details;
  drop table if exists oauth_code;
  drop table if exists ClientDetails;
  drop table if exists user;



CREATE TABLE IF NOT EXISTS user (
  id BIGINT(11) NOT NULL AUTO_INCREMENT,
  age INT(20) DEFAULT 0,
  first_name VARCHAR(100) DEFAULT NULL,
  last_name VARCHAR(100) DEFAULT NULL,
  email VARCHAR(50) unique not null,
  username varchar(50) unique not null,
  password VARCHAR(100) DEFAULT NULL,
  phone VARCHAR(15) DEFAULT NULL,
  city VARCHAR(100) DEFAULT NULL,
  state VARCHAR(100) DEFAULT NULL,
  country VARCHAR(100) DEFAULT NULL,
  zipcode VARCHAR(50) DEFAULT NULL,
  clear_image VARCHAR(100) DEFAULT NULL,
  blurred_image VARCHAR(100) DEFAULT NULL,
  enabled boolean not null,
  version smallint(1) default 0,
  updated DATETIME DEFAULT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS authorities (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    authority VARCHAR(50) NOT NULL,
    version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_authorities_users FOREIGN KEY (username)
        REFERENCES user (username)
        ON DELETE CASCADE,
    CONSTRAINT ix_auth_username UNIQUE (username, authority)
)  ENGINE=INNODB;


	create table if not exists groups (
	  id BIGINT AUTO_INCREMENT  primary key,
	  group_name varchar(50) not null,
	      updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	  version smallint(1) default 0);
	  
	  
CREATE TABLE IF NOT EXISTS group_authorities (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    group_id bigint(11) not null,
    authority VARCHAR(50) NOT NULL,
    version smallint(1) default 0,
        updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id)
        REFERENCES groups (id)
        ON DELETE CASCADE
)  ENGINE=INNODB;


	  
CREATE TABLE IF NOT EXISTS group_members (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    group_id bigint(11) not null,
    username VARCHAR(50) NOT NULL,
    version smallint(1) default 0,
        updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id)
        REFERENCES groups (id)
        ON DELETE CASCADE
)  ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS persistent_logins (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    series varchar(64) unique not null,
    username VARCHAR(50) NOT NULL,
    token VARCHAR(50) NOT NULL,
    last_used DATETIME not null,
    version smallint(1) default 0,
        updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)  ENGINE=INNODB;



CREATE TABLE IF NOT EXISTS oauth_client_details (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    client_id VARCHAR(128) unique  not null,
    client_secret VARCHAR(128),
    resource_ids VARCHAR(128),
    scope VARCHAR(128),
    authorized_grant_types VARCHAR(128),
	  web_server_redirect_uri VARCHAR(128),
	  authorities VARCHAR(128),
	  access_token_validity INT(10),
	  refresh_token_validity INT(10),
	  additional_information VARCHAR(128),
  version smallint(1) default 0,
  updated DATETIME DEFAULT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	  
    PRIMARY KEY (id)
)  ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS oauth_client_token (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    token_id VARCHAR(256),
    token longblob,
    authentication_id VARCHAR(256),
	  user_name VARCHAR(256),
	  client_id VARCHAR(256),
  version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)  ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS oauth_access_token (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    token_id VARCHAR(256),
	  token longblob,
	  authentication_id VARCHAR(256),
	  user_name VARCHAR(256),
	  client_id VARCHAR(256),
	  authentication longblob,
	  refresh_token VARCHAR(256),
  version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	  
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    token_id VARCHAR(256),
	  token longblob,
	  authentication_id VARCHAR(256),
	  user_name VARCHAR(256),
	  client_id VARCHAR(256),
	  authentication longblob,
	  refresh_token VARCHAR(256),
  version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)  ENGINE=INNODB;



CREATE TABLE IF NOT EXISTS oauth_code (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    code VARCHAR(256), authentication longblob,
  version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (id)
)  ENGINE=INNODB;



CREATE TABLE IF NOT EXISTS ClientDetails (
    id BIGINT(11) NOT NULL AUTO_INCREMENT,
    appId VARCHAR(128) UNIQUE NOT NULL,
	  resourceIds VARCHAR(128),
	  appSecret VARCHAR(128),
	  scope VARCHAR(128),
	  grantTypes VARCHAR(128),
	  redirectUrl VARCHAR(128),
	  authorities VARCHAR(128),
	  access_token_validity INTEGER,
	  refresh_token_validity INTEGER,
	  additionalInformation VARCHAR(256),
  version smallint(1) default 0,
    updated DATETIME DEFAULT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    PRIMARY KEY (id)
)  ENGINE=INNODB;

