DROP TABLE IF EXISTS `customer_admin`;
CREATE TABLE `customer_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `freightcom_staff`;
CREATE TABLE `freightcom_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `customer_staff`;
CREATE TABLE `customer_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`customer_id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
