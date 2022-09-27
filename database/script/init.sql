-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema oasip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `oasip` DEFAULT CHARACTER SET utf8 ;
USE `oasip` ;

-- -----------------------------------------------------
-- Table `oasip`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oasip`.`user` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `role` ENUM('admin', 'lecturer', 'student') NOT NULL DEFAULT 'student',
  `createdOn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `oasip`.`event_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oasip`.`event_category` (
  `categoryId` INT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(100) NOT NULL,
  `eventCategoryDesc` VARCHAR(500) NULL DEFAULT NULL,
  `eventDuration` INT NOT NULL,
  PRIMARY KEY (`categoryId`),
  UNIQUE INDEX `categoryName` (`categoryName` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `oasip`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oasip`.`event` (
  `bookingId` INT NOT NULL AUTO_INCREMENT,
  `bookingName` VARCHAR(100) NOT NULL,
  `bookingEmail` VARCHAR(50) NOT NULL,
  `categoryId` INT NOT NULL,
  `eventStartTime` DATETIME NOT NULL,
  `eventDuration` INT NOT NULL,
  `eventNotes` LONGTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`bookingId`),
  INDEX `fk_event_event_category` (`categoryId` ASC) VISIBLE,
  CONSTRAINT `fk_event_event_category`
    FOREIGN KEY (`categoryId`)
    REFERENCES `oasip`.`event_category` (`categoryId`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb3;

CREATE USER 'ssi2dev'@'%' IDENTIFIED BY 'ssi2@22@54@109';
GRANT SELECT,UPDATE,INSERT,DELETE ON oasip.* TO 'ssi2dev'@'%';

CREATE USER 'ssi2dbadmin'@'%' IDENTIFIED BY 'ssi2@054';
GRANT ALL PRIVILEGES ON oasip.* TO 'ssi2dbadmin'@'%';

FLUSH PRIVILEGES;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

