-- CREATE USER 'newuser'@'localhost' IDENTIFIED BY 'password';
CREATE USER IF NOT EXISTS gatechUser@localhost IDENTIFIED BY 'gatech123';
DROP DATABASE IF EXISTS `cs6400-2022-01-Team028`; 
SET default_storage_engine=InnoDB;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `cs6400-2022-01-Team028` 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE `cs6400-2022-01-Team028`;
GRANT SELECT, INSERT, UPDATE, DELETE, FILE ON *.* TO 'gatechUser'@'localhost';
GRANT ALL PRIVILEGES ON `gatechuser`.* TO 'gatechUser'@'localhost';
GRANT ALL PRIVILEGES ON `cs6400-2022-01-Team028`.* TO 'gatechUser'@'localhost';
FLUSH PRIVILEGES;

-- Tables
DROP TABLE IF EXISTS `RatedSwap`;
DROP TABLE IF EXISTS `AcknowledgedSwap`;
DROP TABLE IF EXISTS `Swap`;
DROP TABLE IF EXISTS `Browses`;
DROP TABLE IF EXISTS `JigsawPuzzle`;
DROP TABLE IF EXISTS `ComputerGame`;
DROP TABLE IF EXISTS `VideoGame`;
DROP TABLE IF EXISTS `CardGame`;
DROP TABLE IF EXISTS `BoardGame`;
DROP TABLE IF EXISTS `Item`;
DROP TABLE IF EXISTS `Platform`;
DROP TABLE IF EXISTS `PhoneNumber`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Location`;

CREATE TABLE `Location` (
  `postal_code`		VARCHAR(50)			NOT NULL,
  `city`			VARCHAR(50)			NOT NULL,
  `state`			VARCHAR(50)			NOT NULL,
  `latitude`		DECIMAL(20, 15)		NOT NULL,
  `longitude`		DECIMAL(20, 15)		NOT NULL,
  PRIMARY KEY (`postal_code`)
);

CREATE TABLE `User` (
  `email`				VARCHAR(50)		NOT NULL,
  `postal_code`			VARCHAR(50)		NOT NULL,
  `first_name`			VARCHAR(50)		NOT NULL,
  `last_name` 			VARCHAR(50) 	NOT NULL,
  `nickname`			VARCHAR(50) 	NOT NULL,
  `password`			VARCHAR(50) 	NOT NULL,
  PRIMARY KEY (`email`),
  FOREIGN KEY (`postal_code`) REFERENCES `Location` (`postal_code`)
);

CREATE TABLE `PhoneNumber` (
  `phone_number`		VARCHAR(50) 	NOT NULL,
  `email` 				VARCHAR(50) 	NOT NULL	UNIQUE,
  `type` 				VARCHAR(50)		NOT NULL	CHECK (`type` IN ('Home', 'Work', 'Mobile')),
  `share_phone_number` 	BOOLEAN 		NOT NULL,
  PRIMARY KEY (`phone_number`),
  FOREIGN KEY (`email`) REFERENCES `User` (`email`)
);

CREATE TABLE `Platform` (
  `platform_name`	VARCHAR(50) 	NOT NULL,
  PRIMARY KEY (`platform_name`)
);

CREATE TABLE `Item` (
  `item_id`			BIGINT 			NOT NULL	AUTO_INCREMENT,
  `email` 			VARCHAR(50) 	NOT NULL,
  `name` 			VARCHAR(256)	NOT NULL,
  `description`		VARCHAR(1024)	NULL,
  `condition` 		VARCHAR(50) 	NOT NULL	CHECK (`condition` IN ('Mint', 'Like New', 'Lightly Used', 'Moderately Used', 'Heavily Used', 'Damaged/Missing parts')),
  `type`			VARCHAR(50)		NOT NULL	CHECK (`type` IN ('Board Game', 'Card Game', 'Video Game', 'Computer Game', 'Jigsaw Puzzle')),
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`email`) REFERENCES `User` (`email`)
);

CREATE TABLE `BoardGame` (
  `item_id`		BIGINT 			NOT NULL,
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
);

CREATE TABLE `CardGame` (
  `item_id`		BIGINT 			NOT NULL,
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
);

CREATE TABLE `VideoGame` (
  `item_id`			BIGINT 			NOT NULL,
  `platform_name` 	VARCHAR(50) 	NOT NULL,
  `media` 			VARCHAR(50)		NOT NULL	CHECK (`media` IN ('Cartridge', 'Game Card', 'Optical Disc')),
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`),
  FOREIGN KEY (`platform_name`) REFERENCES `Platform` (`platform_name`)
);

CREATE TABLE `ComputerGame` (
  `item_id`		BIGINT 			NOT NULL,
  `platform` 	VARCHAR(50) 	NOT NULL	CHECK (`platform` IN ('Linux', 'Windows', 'MacOS')),
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
);

CREATE TABLE `JigsawPuzzle` (
  `item_id`			BIGINT 			NOT NULL,
  `piece_count` 	INT				NOT NULL,
  PRIMARY KEY (`item_id`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
);

CREATE TABLE `Browses` (
  `email`		VARCHAR(50)		NOT NULL,
  `item_id` 	BIGINT				NOT NULL,
  PRIMARY KEY (`email`, `item_id`),
  FOREIGN KEY (`email`) REFERENCES `User` (`email`),
  FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
);

CREATE TABLE `Swap` (
  `swap_id`					BIGINT			NOT NULL	AUTO_INCREMENT,
  `proposer_email`			VARCHAR(50) 	NOT NULL,
  `counterparty_email`		VARCHAR(50) 	NOT NULL,
  `proposed_item_id`		BIGINT			NOT NULL,
  `counterparty_item_id`	BIGINT 			NOT NULL,
  `proposed_date`			DATE			NOT NULL,
  PRIMARY KEY (`swap_id`),
  FOREIGN KEY (`proposer_email`) REFERENCES `User` (`email`),
  FOREIGN KEY (`counterparty_email`) REFERENCES `User` (`email`),
  FOREIGN KEY (`proposed_item_id`) REFERENCES `Item` (`item_id`),
  FOREIGN KEY (`counterparty_item_id`) REFERENCES `Item` (`item_id`),
  UNIQUE (`proposed_item_id`, `counterparty_item_id`)
);

CREATE TABLE `AcknowledgedSwap` (
  `swap_id`				BIGINT			NOT NULL,
  `status`				VARCHAR(50)		NOT NULL,
  `acknowledged_date`	DATE 			NOT NULL,
  PRIMARY KEY (`swap_id`),
  FOREIGN KEY (`swap_id`) REFERENCES `Swap` (`swap_id`)
);

CREATE TABLE `RatedSwap` (
  `swap_id`		BIGINT			NOT NULL,
  `email`		VARCHAR(50)	 	NOT NULL,
  `rating`		INT 			NOT NULL,
  PRIMARY KEY (`swap_id`, `email`),
  FOREIGN KEY (`swap_id`) REFERENCES `Swap` (`swap_id`),
  FOREIGN KEY (`email`) REFERENCES `User` (`email`)
);
