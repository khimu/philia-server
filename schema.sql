CREATE DATABASE philia;

CREATE USER 'philiauser'@'localhost' IDENTIFIED BY 'pH1Li8';
GRANT ALL PRIVILEGES ON *.* TO 'philiauser'@'localhost';

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

