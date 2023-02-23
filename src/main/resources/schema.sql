-- MySQL Script generated by MySQL Workbench
-- Tue Feb 21 23:06:47 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -----------------------------------------------------
-- Schema shoplivedb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shoplivedb` DEFAULT CHARACTER SET utf8mb4 ;
USE `shoplivedb` ;

-- -----------------------------------------------------
-- Table `shoplivedb`.`video`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoplivedb`.`video` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `original_filesize` INT(11) NOT NULL,
  `original_width` INT(11) NOT NULL,
  `original_height` INT(11) NOT NULL,
  `original_video_url` VARCHAR(255) NOT NULL,
  `resized_filesize` INT(11) NULL DEFAULT NULL,
  `resized_width` INT(11) NULL DEFAULT NULL,
  `resized_height` INT(11) NULL DEFAULT NULL,
  `resized_video_url` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `thumbnail_url` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
