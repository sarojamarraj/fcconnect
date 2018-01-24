-- MySQL dump 10.16  Distrib 10.1.20-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.1.20-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acc_charge`
--

DROP TABLE IF EXISTS `acc_charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acc_charge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(30,2) DEFAULT NULL,
  `description` text,
  `currency` varchar(32) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accessorial_services`
--

DROP TABLE IF EXISTS `accessorial_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessorial_services` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `description` text,
  `type` varchar(64) DEFAULT NULL,
  `rate` decimal(30,2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`name`),
  KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accessorial_type`
--

DROP TABLE IF EXISTS `accessorial_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessorial_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accounts_receivable`
--

DROP TABLE IF EXISTS `accounts_receivable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts_receivable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `action` varchar(128) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_no` varchar(255) DEFAULT NULL,
  `postal_zip` varchar(128) DEFAULT NULL,
  `care_of` varchar(128) DEFAULT NULL,
  `city` varchar(128) DEFAULT NULL,
  `mobile_phone_no` varchar(255) DEFAULT NULL,
  `consignee_name` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `street_name` varchar(128) DEFAULT NULL,
  `unit_number` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `consignee_id` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `province_state` varchar(128) DEFAULT NULL,
  `po_box` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `province_id` bigint(20) DEFAULT NULL,
  `company_name` varchar(128) DEFAULT NULL,
  `street_number` varchar(128) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `fax_no` varchar(255) DEFAULT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79840 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address_book`
--

DROP TABLE IF EXISTS `address_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `consignee_name` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `default_to` tinyint(1) DEFAULT NULL,
  `notify` tinyint(1) DEFAULT NULL,
  `contact_email` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `country_name` varchar(50) DEFAULT NULL,
  `default_from` tinyint(1) DEFAULT NULL,
  `consignee_id` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `distribution_list_name` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `tax_id` varchar(255) DEFAULT NULL,
  `residential` tinyint(1) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `instruction` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1266340 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12663 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` bigint(20) DEFAULT NULL,
  `agent_name` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `parent_sales_agent_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `promo_code` varchar(255) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `commission_percent` float DEFAULT NULL,
  `comm_wo_op_expense` decimal(30,2) DEFAULT NULL,
  `mfw_key` varchar(250) DEFAULT NULL,
  `comm_courier_op_expense` decimal(30,2) DEFAULT NULL,
  `comm_pallet_op_expense` decimal(30,2) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12679 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `closed_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `object_name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `object_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrier`
--

DROP TABLE IF EXISTS `carrier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dba` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `implementing_class` varchar(50) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrier_account`
--

DROP TABLE IF EXISTS `carrier_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrier_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference3` varchar(255) DEFAULT NULL,
  `wb_static_data` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `ftp_username` varchar(255) DEFAULT NULL,
  `reference1` varchar(255) DEFAULT NULL,
  `reference2` varchar(255) DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `current_tracking_no` varchar(255) DEFAULT NULL,
  `web_meter_number` varchar(255) DEFAULT NULL,
  `location_code` varchar(255) DEFAULT NULL,
  `first_tracking_no` varchar(255) DEFAULT NULL,
  `last_manifest_no` varchar(255) DEFAULT NULL,
  `notify_email` varchar(255) DEFAULT NULL,
  `server_meter_number` varchar(255) DEFAULT NULL,
  `manifest_prefix` varchar(255) DEFAULT NULL,
  `third_party_data` tinyint(1) DEFAULT NULL,
  `ftp_password` varchar(255) DEFAULT NULL,
  `current_manifest_no` varchar(255) DEFAULT NULL,
  `ftp_address` varchar(255) DEFAULT NULL,
  `courier` tinyint(1) DEFAULT NULL,
  `last_tracking_no` varchar(255) DEFAULT NULL,
  `first_manifest_no` varchar(255) DEFAULT NULL,
  `ext_carrier_id` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  `server_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2453 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrier_cutofftime`
--

DROP TABLE IF EXISTS `carrier_cutofftime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrier_cutofftime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` int(11) DEFAULT NULL,
  `postal_code` varchar(25) DEFAULT NULL,
  `cutoff_time` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1817801 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrier_data`
--

DROP TABLE IF EXISTS `carrier_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrier_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fedex_can_meter_num` varchar(45) DEFAULT NULL,
  `ups_usa_username` varchar(45) DEFAULT NULL,
  `use_custom_ups_can_acct` tinyint(1) DEFAULT NULL,
  `ups_can_acct_num` varchar(45) DEFAULT NULL,
  `fedex_acct_num_us` varchar(255) DEFAULT NULL,
  `use_custom_fedex_usa_acct` tinyint(1) DEFAULT NULL,
  `use_custom_fedex_can_acct` tinyint(1) DEFAULT NULL,
  `ups_usa_password` varchar(45) DEFAULT NULL,
  `fedex_meter_num` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `fedex_meter_num_us` varchar(255) DEFAULT NULL,
  `ups_can_username` varchar(45) DEFAULT NULL,
  `sfw_user_id` varchar(45) DEFAULT NULL,
  `ups_usa_key` varchar(255) DEFAULT NULL,
  `ups_can_password` varchar(45) DEFAULT NULL,
  `ups_can_key` varchar(255) DEFAULT NULL,
  `ups_usa_acct_num` varchar(45) DEFAULT NULL,
  `use_custom_ups_usa_acct` tinyint(1) DEFAULT NULL,
  `fedex_acct_num` varchar(255) DEFAULT NULL,
  `ups_acct_num` varchar(255) DEFAULT NULL,
  `fedex_usa_acct_num` varchar(45) DEFAULT NULL,
  `fedex_usa_meter_num` varchar(45) DEFAULT NULL,
  `fedex_can_acct_num` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrier_service`
--

DROP TABLE IF EXISTS `carrier_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrier_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cc_transaction`
--

DROP TABLE IF EXISTS `cc_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cc_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(30,2) DEFAULT NULL,
  `charge_type` int(11) DEFAULT NULL,
  `proc_message` varchar(255) DEFAULT NULL,
  `card_num_charged` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `receipt_id` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `processor_tran_id` varchar(255) DEFAULT NULL,
  `ref_num` varchar(255) DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL,
  `merchant_account_id` bigint(20) unsigned DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `authorization_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80323 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `charge`
--

DROP TABLE IF EXISTS `charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baserate` varchar(128) DEFAULT NULL,
  `total_value` decimal(30,2) DEFAULT NULL,
  `code` varchar(128) DEFAULT NULL,
  `total_value_charge` decimal(30,2) DEFAULT NULL,
  `apply_commission` bit(1) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `unit_type` varchar(255) DEFAULT NULL,
  `seq_order` varchar(128) DEFAULT NULL,
  `final_total_value` decimal(30,2) DEFAULT NULL,
  `total` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `charge_id` bigint(20) DEFAULT NULL,
  `unit_value` float DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `charge_tax_id` bigint(20) DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `amount` varchar(128) DEFAULT NULL,
  `markup` varchar(128) DEFAULT NULL,
  `rate_unit` float DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `subtotal` varchar(128) DEFAULT NULL,
  `qty` varchar(128) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `final_total_value_charge` decimal(30,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1405450 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `charge_tax`
--

DROP TABLE IF EXISTS `charge_tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge_tax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tax_number` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `fraction` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `claim`
--

DROP TABLE IF EXISTS `claim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `claim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reason` varchar(128) DEFAULT NULL,
  `private` varchar(128) DEFAULT NULL,
  `carrier_claim_no` bigint(20) DEFAULT NULL,
  `shipment_charge` decimal(30,2) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `desc_shipment` varchar(600) DEFAULT NULL,
  `damaged_value` varchar(128) DEFAULT NULL,
  `checknummailed` varchar(128) DEFAULT NULL,
  `claim_number` varchar(128) DEFAULT NULL,
  `mailed_at` datetime DEFAULT NULL,
  `checknumreceiver` varchar(128) DEFAULT NULL,
  `shipping` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `carrier_status` varchar(128) DEFAULT NULL,
  `phone_num` varchar(45) DEFAULT NULL,
  `date_received` datetime DEFAULT NULL,
  `contact_name` varchar(45) DEFAULT NULL,
  `amount_received` decimal(30,2) DEFAULT NULL,
  `amount_claimed` varchar(128) DEFAULT NULL,
  `desc_claim` varchar(600) DEFAULT NULL,
  `claimsdetails` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `value_of_damaged` decimal(30,2) DEFAULT NULL,
  `check_no_received` varchar(50) DEFAULT NULL,
  `check_no_mailed` varchar(50) DEFAULT NULL,
  `email_address` varchar(45) DEFAULT NULL,
  `amount_mailed` varchar(128) DEFAULT NULL,
  `received_at` datetime DEFAULT NULL,
  `goods_value` varchar(128) DEFAULT NULL,
  `date_mailed` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `claim_date` datetime DEFAULT NULL,
  `value_of_goods_claim` decimal(30,2) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1937 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `claim_docs`
--

DROP TABLE IF EXISTS `claim_docs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `claim_docs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) DEFAULT NULL,
  `claim_id` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `upload_file` mediumblob,
  `upload_doc_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5390 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cod_account`
--

DROP TABLE IF EXISTS `cod_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cod_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cod_transaction`
--

DROP TABLE IF EXISTS `cod_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cod_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cod_account_id` bigint(20) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `acc_recv_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1676 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `codaccount`
--

DROP TABLE IF EXISTS `codaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `codaccount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commission_payable`
--

DROP TABLE IF EXISTS `commission_payable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commission_payable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` varchar(128) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `charge_id` bigint(20) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commission_rate`
--

DROP TABLE IF EXISTS `commission_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commission_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_at` datetime DEFAULT NULL,
  `fixed_amount` varchar(128) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `min_amount` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `fraction` varchar(128) DEFAULT NULL,
  `calc_from` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `max_amount` varchar(128) DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `creator_temp`
--

DROP TABLE IF EXISTS `creator_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creator_temp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_created` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credit`
--

DROP TABLE IF EXISTS `credit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `note` varchar(128) DEFAULT NULL,
  `amount` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `created_by_user_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credit_card`
--

DROP TABLE IF EXISTS `credit_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credit_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `type` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `cc_expiry_year` varchar(255) DEFAULT NULL,
  `expiry_year` varchar(128) DEFAULT NULL,
  `number` varchar(128) DEFAULT NULL,
  `expiry_month` varchar(128) DEFAULT NULL,
  `cvc` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `cc_expiry_month` varchar(255) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `cc_number` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3662 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crm`
--

DROP TABLE IF EXISTS `crm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` varchar(100) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `comments` varchar(765) DEFAULT NULL,
  `address2` varchar(765) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `address1` varchar(765) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deadline_date` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `currency_exchange`
--

DROP TABLE IF EXISTS `currency_exchange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency_exchange` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `us_to_can` double DEFAULT NULL,
  `can_to_us` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_options`
--

DROP TABLE IF EXISTS `custom_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_options` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quote_view_option` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1426 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `invoice_currency_id` int(11) DEFAULT NULL,
  `affiliate_id` bigint(20) DEFAULT NULL,
  `flat_rate` bit(1) DEFAULT NULL,
  `single_shipment_invoicing` tinyint(1) DEFAULT NULL,
  `flat_charge_final` float DEFAULT NULL,
  `is_charge_credit_card` bit(1) DEFAULT NULL,
  `comm_courier_op_expense` float DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `allow_predispatch` bit(1) DEFAULT NULL,
  `affiliate_kick_back` float DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `apply_tax` bit(1) DEFAULT NULL,
  `credit_code` varchar(10) DEFAULT NULL,
  `ship_to_address_id` bigint(20) DEFAULT NULL,
  `aff_reg_page` varchar(30) DEFAULT NULL,
  `comm_pallet_deduct_tran_fee` tinyint(1) DEFAULT NULL,
  `billing_address_id` bigint(20) DEFAULT NULL,
  `dba` varchar(128) DEFAULT NULL,
  `insurance_preference` int(11) DEFAULT NULL,
  `invoice_email` varchar(255) DEFAULT NULL,
  `disable_if_unpaid_invoices` bit(1) DEFAULT NULL,
  `insurance_min` float DEFAULT NULL,
  `apply_currency_exchange` tinyint(1) DEFAULT NULL,
  `pod_reqd` tinyint(1) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `tax_id` varchar(255) DEFAULT NULL,
  `comm_courier_deduct_tran_fee` tinyint(1) DEFAULT NULL,
  `opt_pcs_wgt` int(11) DEFAULT NULL,
  `comm_pallet_op_expense` float DEFAULT NULL,
  `ref_sort_billing` tinyint(1) DEFAULT NULL,
  `commercial_invoice` bit(1) DEFAULT NULL,
  `flat_cost1` float DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `flat_cost2` float DEFAULT NULL,
  `discount_perc` float DEFAULT NULL,
  `cc_receipt` tinyint(1) DEFAULT NULL,
  `preferred_carrier_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `ref_code_mandatory` tinyint(1) DEFAULT NULL,
  `apply_credit_limit` tinyint(1) DEFAULT NULL,
  `freight_class` tinyint(4) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `account_number` varchar(30) DEFAULT NULL,
  `reference3` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `comm_courier_deduct_affiliate_fee` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `reference1` varchar(255) DEFAULT NULL,
  `flat_cost3` float DEFAULT NULL,
  `insurance_percentage` float DEFAULT NULL,
  `reference2` varchar(255) DEFAULT NULL,
  `api_password` varchar(255) DEFAULT NULL,
  `external_affiliate_num` varchar(255) DEFAULT NULL,
  `nmfc_code` tinyint(4) DEFAULT NULL,
  `comm_wo_op_expense` float DEFAULT NULL,
  `api_username` varchar(255) DEFAULT NULL,
  `show_rates` tinyint(1) DEFAULT NULL,
  `sub_agent_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `credit_limit` float DEFAULT NULL,
  `net_terms` int(10) unsigned DEFAULT NULL,
  `ship_from_address_id` bigint(20) DEFAULT NULL,
  `fedex_us_tariff` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `show_news` bit(1) DEFAULT NULL,
  `comm_pallet_deduct_affiliate_fee` tinyint(1) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `flat_charge` float DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `broker_name` varchar(255) DEFAULT NULL,
  `net_terms_warning` int(10) unsigned DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `broker` int(11) DEFAULT NULL,
  `language_preference` varchar(30) DEFAULT NULL,
  `is_web_customer` bit(1) DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `aff_login_page` varchar(30) DEFAULT NULL,
  `package_preference` int(11) DEFAULT NULL,
  `small_package` tinyint(1) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `invoice_currency` varchar(255) DEFAULT NULL,
  `affiliate` bit(1) DEFAULT NULL,
  `suspended_at` datetime DEFAULT NULL,
  `activated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10934 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_admin`
--

DROP TABLE IF EXISTS `customer_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12676 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_carrier`
--

DROP TABLE IF EXISTS `customer_carrier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_carrier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `carrieraccount_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2366 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_comment`
--

DROP TABLE IF EXISTS `customer_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `text` varchar(128) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_comments_log`
--

DROP TABLE IF EXISTS `customer_comments_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_comments_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_timestamp` datetime DEFAULT NULL,
  `comments` varchar(5000) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `comments_by` varchar(75) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1234 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_option`
--

DROP TABLE IF EXISTS `customer_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `value` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `key` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_order`
--

DROP TABLE IF EXISTS `customer_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_cost_franchise` decimal(30,2) DEFAULT NULL,
  `origin_terminal` bigint(20) DEFAULT NULL,
  `origin_close_time_h` varchar(45) DEFAULT NULL,
  `total_cost` decimal(30,2) DEFAULT NULL,
  `eshipper_user` int(11) DEFAULT NULL,
  `submitted_at` datetime DEFAULT NULL,
  `quoted_fuel_cost` decimal(30,2) DEFAULT NULL,
  `package_id` bigint(20) DEFAULT NULL,
  `origin_close_time_m` varchar(45) DEFAULT NULL,
  `ship_to_id` bigint(20) DEFAULT NULL,
  `markup_perc` float DEFAULT NULL,
  `comm_status` int(11) DEFAULT NULL,
  `bol_id` varchar(255) DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `operating_cost_perc` double DEFAULT NULL,
  `distribution_quote` float DEFAULT NULL,
  `is_from_mf_to_f` tinyint(1) DEFAULT NULL,
  `inside_pickup` tinyint(1) DEFAULT NULL,
  `ship_to_address_id` bigint(20) DEFAULT NULL,
  `job_number` varchar(255) DEFAULT NULL,
  `quoted_total_charge` decimal(30,2) DEFAULT NULL,
  `trailer_reference1` varchar(15) DEFAULT NULL,
  `trailer_reference2` varchar(15) DEFAULT NULL,
  `update_color_code` bit(1) DEFAULT NULL,
  `shipment_error` varchar(1000) DEFAULT NULL,
  `homeland_security` tinyint(1) DEFAULT NULL,
  `ship_from_id` bigint(20) DEFAULT NULL,
  `bill_to_id` bigint(20) DEFAULT NULL,
  `frozen` tinyint(1) DEFAULT NULL,
  `trailer_weight2` int(11) DEFAULT NULL,
  `trailer_weight1` int(11) DEFAULT NULL,
  `total_charge` decimal(30,2) DEFAULT NULL,
  `pod_name` varchar(255) DEFAULT NULL,
  `charge_comments` varchar(500) DEFAULT NULL,
  `master_tracking_num_1` varchar(255) DEFAULT NULL,
  `excess_length` tinyint(1) DEFAULT NULL,
  `tax_type` int(11) DEFAULT NULL,
  `quoted_total_cost` decimal(30,2) DEFAULT NULL,
  `cod_value` float DEFAULT NULL,
  `shipment_density` int(11) DEFAULT NULL,
  `tender_error_message` text,
  `tran_fee` decimal(30,2) DEFAULT NULL,
  `reference_name` varchar(255) DEFAULT NULL,
  `partner_markup` float DEFAULT NULL,
  `guaranteed_dropoff_at` datetime DEFAULT NULL,
  `customs_currency` varchar(255) DEFAULT NULL,
  `freight_tariff` decimal(30,2) DEFAULT NULL,
  `customs_contact_name` varchar(255) DEFAULT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `fuel_tariff` decimal(30,2) DEFAULT NULL,
  `order_batch_id` bigint(20) DEFAULT NULL,
  `currency_rate` float DEFAULT NULL,
  `dangerous_goods` varchar(255) DEFAULT NULL,
  `internal_notes` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `workorder_status_id` bigint(20) DEFAULT NULL,
  `tot_act_wgt` float DEFAULT NULL,
  `to_airport_code` varchar(255) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `ship_from_address_id` bigint(20) DEFAULT NULL,
  `tracking_url` varchar(600) DEFAULT NULL,
  `sort_segregate` tinyint(1) DEFAULT NULL,
  `mode_transport` varchar(255) DEFAULT NULL,
  `mark_and_tag_freight` tinyint(1) DEFAULT NULL,
  `mfw_booking_key` varchar(250) DEFAULT NULL,
  `carrier_pick_up_conf` varchar(25) DEFAULT NULL,
  `comm_driver_id` bigint(20) DEFAULT NULL,
  `is_e_manifested` bit(1) DEFAULT NULL,
  `proof_of_delivery` varchar(255) DEFAULT NULL,
  `from_datetime` datetime DEFAULT NULL,
  `customes_freight` tinyint(1) DEFAULT NULL,
  `gross_profit` decimal(30,2) DEFAULT NULL,
  `rate_error` varchar(1000) DEFAULT NULL,
  `destination_terminal` bigint(20) DEFAULT NULL,
  `pod_file_name` varchar(45) DEFAULT NULL,
  `tot_act_quantity` int(11) DEFAULT NULL,
  `hold` tinyint(1) DEFAULT NULL,
  `cod_payment` varchar(255) DEFAULT NULL,
  `commissionable_amount` decimal(30,2) DEFAULT NULL,
  `job_pickup_date` datetime DEFAULT NULL,
  `insurance_value_3rd` float DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `wait_time` int(11) DEFAULT NULL,
  `pod_timestamp` datetime DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `fuel_surcharge` decimal(30,2) DEFAULT NULL,
  `edi_verified` bit(1) DEFAULT NULL,
  `insurance_type` int(11) DEFAULT NULL,
  `to_location_type` int(11) DEFAULT NULL,
  `total_tariff` decimal(30,2) DEFAULT NULL,
  `original_markdown_type` int(10) unsigned DEFAULT NULL,
  `eshipper_order_id` bigint(20) DEFAULT NULL,
  `per_cubic_feet` varchar(21) DEFAULT NULL,
  `adjustment` decimal(30,2) DEFAULT NULL,
  `customs_broker_name` varchar(255) DEFAULT NULL,
  `invoice_status` tinyint(3) unsigned DEFAULT NULL,
  `transit_days` int(11) DEFAULT NULL,
  `bill_to` varchar(45) DEFAULT NULL,
  `reference3` varchar(255) DEFAULT NULL,
  `distribution_service_id` bigint(20) DEFAULT NULL,
  `param_service_id` bigint(20) DEFAULT NULL,
  `expected_delivery_date` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `reference2` varchar(255) DEFAULT NULL,
  `cod_pin_number` varchar(15) DEFAULT NULL,
  `customs_fax_number` varchar(255) DEFAULT NULL,
  `return_service` tinyint(1) DEFAULT NULL,
  `has_been_edited` bit(1) DEFAULT NULL,
  `canadian_customs` tinyint(1) DEFAULT NULL,
  `fuel_perc_charge` float DEFAULT NULL,
  `bill_to_contact` varchar(255) DEFAULT NULL,
  `reference_code` varchar(255) DEFAULT NULL,
  `notes_1` varchar(500) DEFAULT NULL,
  `notes_2` varchar(500) DEFAULT NULL,
  `inbond_fee` tinyint(1) DEFAULT NULL,
  `load_freight_at_pickup` tinyint(1) DEFAULT NULL,
  `reference2_name` varchar(255) DEFAULT NULL,
  `trucking_prealert_id` bigint(20) DEFAULT NULL,
  `stackable` tinyint(1) DEFAULT NULL,
  `trailer_seal_number2` varchar(15) DEFAULT NULL,
  `trailer_seal_number1` varchar(15) DEFAULT NULL,
  `merged` bit(1) DEFAULT NULL,
  `protective_pallet_cover` tinyint(1) DEFAULT NULL,
  `quoted_base_cost` decimal(30,2) DEFAULT NULL,
  `tax_rate_id` bigint(20) DEFAULT NULL,
  `from_airline_name` varchar(255) DEFAULT NULL,
  `ship_date` datetime DEFAULT NULL,
  `shipment_type` varchar(128) DEFAULT NULL,
  `quoted_base_charge` decimal(30,2) DEFAULT NULL,
  `fuel_cost` decimal(30,2) DEFAULT NULL,
  `cod_cheque_date` date DEFAULT NULL,
  `load_id` bigint(20) DEFAULT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `scheduled_ship_date` datetime DEFAULT NULL,
  `from_location_type` int(11) DEFAULT NULL,
  `freeze_protect` tinyint(1) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `trailer_carton_count2` int(11) DEFAULT NULL,
  `trailer_carton_count1` int(11) DEFAULT NULL,
  `cod_payable_to` varchar(255) DEFAULT NULL,
  `pier_charge` tinyint(1) DEFAULT NULL,
  `guarantee_charge` int(11) DEFAULT NULL,
  `inside_delivery` tinyint(1) DEFAULT NULL,
  `sat_delivery` tinyint(1) DEFAULT NULL,
  `payment_type_id` tinyint(1) DEFAULT NULL,
  `military_base_delivery` tinyint(1) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `manifest_number` varchar(255) DEFAULT NULL,
  `mfw_connection_key` text,
  `insured_amount` float DEFAULT NULL,
  `pod_given_to` varchar(255) DEFAULT NULL,
  `trailer_number` varchar(15) DEFAULT NULL,
  `quoted_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `protective_drum_cover` tinyint(1) DEFAULT NULL,
  `reference3_name` varchar(255) DEFAULT NULL,
  `limited_access` tinyint(1) DEFAULT NULL,
  `pickup_time` varchar(255) DEFAULT NULL,
  `req_delivery_date` varchar(255) DEFAULT NULL,
  `vehicle_full` bit(1) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `heated_service` tinyint(1) DEFAULT NULL,
  `dim_type` int(11) DEFAULT NULL,
  `affiliate_fee` decimal(30,2) DEFAULT NULL,
  `batch_id` varchar(50) DEFAULT NULL,
  `claim_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `from_airport_code` varchar(255) DEFAULT NULL,
  `tender_status` text,
  `pod_file` mediumblob,
  `expected_transit` int(11) DEFAULT NULL,
  `job_radio` varchar(255) DEFAULT NULL,
  `master_tracking_num` varchar(255) DEFAULT NULL,
  `tax_type2` int(11) DEFAULT NULL,
  `customs_phone_number` varchar(255) DEFAULT NULL,
  `trailer_dimension_weight` float DEFAULT NULL,
  `bol_number` varchar(250) DEFAULT NULL,
  `billing_status` tinyint(3) unsigned DEFAULT NULL,
  `actual_base_charge` decimal(30,2) DEFAULT NULL,
  `unload_freight_at_del` tinyint(1) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `reconcil_adjustment` decimal(30,2) DEFAULT NULL,
  `date_of_delivery` datetime DEFAULT NULL,
  `vehicle_type` int(11) DEFAULT NULL,
  `destination_close_time_h` varchar(45) DEFAULT NULL,
  `job_quantity` int(11) DEFAULT NULL,
  `destination_close_time_m` varchar(45) DEFAULT NULL,
  `delivery_appt` tinyint(1) DEFAULT NULL,
  `parent_ediwo_id` bigint(20) DEFAULT NULL,
  `to_datetime` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `fuel_perc_cost` float DEFAULT NULL,
  `assign_driver_id` bigint(20) DEFAULT NULL,
  `quoted_fuel_surcharge` decimal(30,2) DEFAULT NULL,
  `charged_weight` float DEFAULT NULL,
  `prev_invoice_number` bigint(20) DEFAULT NULL,
  `paper_manifest_status` int(11) DEFAULT NULL,
  `scheduled_pick_up_id` bigint(20) DEFAULT NULL,
  `quoted_amount` varchar(255) DEFAULT NULL,
  `comm_amount` decimal(30,2) DEFAULT NULL,
  `exibition_site` tinyint(1) DEFAULT NULL,
  `color_code` varchar(30) DEFAULT NULL,
  `is_insurance_manifested` bit(1) DEFAULT NULL,
  `eshipper_oid` bigint(20) DEFAULT NULL,
  `requested_pickup_at` datetime DEFAULT NULL,
  `job_deliv_deadline` datetime DEFAULT NULL,
  `delivery_prealert_id` bigint(20) DEFAULT NULL,
  `insurance_currency` int(3) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `cur_code` int(11) DEFAULT NULL,
  `job_weight` int(11) DEFAULT NULL,
  `customs_value` decimal(30,2) DEFAULT NULL,
  `after_hours` bit(1) DEFAULT NULL,
  `job_content_descr` varchar(255) DEFAULT NULL,
  `operating_cost` decimal(30,2) DEFAULT NULL,
  `pickup_num` int(11) DEFAULT NULL,
  `cod_currency` varchar(255) DEFAULT NULL,
  `to_airline_name` varchar(255) DEFAULT NULL,
  `base_cost` decimal(30,2) DEFAULT NULL,
  `quoted_at` datetime DEFAULT NULL,
  `cross_border_fee` tinyint(1) DEFAULT NULL,
  `signature_required` int(11) DEFAULT NULL,
  `quote_status` int(11) DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `insurance_value` float DEFAULT NULL,
  `single_shipment` tinyint(1) DEFAULT NULL,
  `sub_type` int(10) unsigned DEFAULT NULL,
  `job_quantity_type` int(11) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `base_charge` decimal(30,2) DEFAULT NULL,
  `customer_markdown` float DEFAULT NULL,
  `quantity` bigint(20) DEFAULT NULL,
  `pickup_time_h` varchar(45) DEFAULT NULL,
  `pickup_time_m` varchar(45) DEFAULT NULL,
  `editable` tinyint(1) DEFAULT NULL,
  `customer_markup` float DEFAULT NULL,
  `customer_markup_type` int(10) unsigned DEFAULT NULL,
  `special_equipment` varchar(255) DEFAULT NULL,
  `actual_weight` float DEFAULT NULL,
  `update_status` bit(1) DEFAULT NULL,
  `return_order_id` bigint(20) DEFAULT NULL,
  `distribution_count` int(11) DEFAULT '0',
  `distribution_list_id` int(11) DEFAULT NULL,
  `selected_quote_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `scheduled_ship_date` (`scheduled_ship_date`),
  KEY `reference_code` (`reference_code`),
  KEY `expected_delivery_date` (`expected_delivery_date`),
  KEY `date_of_delivery` (`date_of_delivery`),
  KEY `ship_date` (`ship_date`),
  KEY `eshipper_oid` (`eshipper_oid`),
  KEY `status_id` (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=504687 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_pickup`
--

DROP TABLE IF EXISTS `customer_pickup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_pickup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_location` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_service`
--

DROP TABLE IF EXISTS `customer_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `automatic_pickup` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_staff`
--

DROP TABLE IF EXISTS `customer_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) NOT NULL,
  `perm` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12681 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customeradmin`
--

DROP TABLE IF EXISTS `customeradmin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customeradmin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `perm` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customs_invoice`
--

DROP TABLE IF EXISTS `customs_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customs_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_to` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `third_party_account` varchar(255) DEFAULT NULL,
  `broker_name` varchar(255) DEFAULT NULL,
  `billing_country` varchar(255) DEFAULT NULL,
  `contact_company` varchar(255) DEFAULT NULL,
  `sed_number` varchar(25) DEFAULT NULL,
  `tax_id` varchar(255) DEFAULT NULL,
  `billing_province` varchar(255) DEFAULT NULL,
  `billing_city` varchar(255) DEFAULT NULL,
  `billing_company` varchar(255) DEFAULT NULL,
  `billing_country_name` varchar(50) DEFAULT NULL,
  `billing_postal` varchar(255) DEFAULT NULL,
  `consignee_account` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `billing_address1` varchar(255) DEFAULT NULL,
  `recipient_tax_id` varchar(255) DEFAULT NULL,
  `billing_address2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customs_invoice_product`
--

DROP TABLE IF EXISTS `customs_invoice_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customs_invoice_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) DEFAULT NULL,
  `country_of_origin` varchar(255) DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `harmonized_code` varchar(255) DEFAULT NULL,
  `unit_price` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52676 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribution`
--

DROP TABLE IF EXISTS `distribution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribution` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distribution_name` varchar(255) DEFAULT NULL,
  `batch_id` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `distribution_charge` float DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `shipment_count` bigint(20) DEFAULT NULL,
  `service_type` varchar(20) DEFAULT NULL,
  `rate_count` bigint(20) DEFAULT NULL,
  `rate_error_count` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `shipment_error_count` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribution_address`
--

DROP TABLE IF EXISTS `distribution_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribution_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distribution_group_id` bigint(20) DEFAULT NULL,
  `shipto_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `has_error` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `distribution_group_id` (`distribution_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribution_carrier`
--

DROP TABLE IF EXISTS `distribution_carrier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribution_carrier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `distribution_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribution_group`
--

DROP TABLE IF EXISTS `distribution_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribution_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `file_name` varchar(256) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `is_driver` bit(1) DEFAULT NULL,
  `commission_percent` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `freightcom_staff`
--

DROP TABLE IF EXISTS `freightcom_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `freightcom_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12675 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gojit_interlines_quebec_ontario_10lb_rates`
--

DROP TABLE IF EXISTS `gojit_interlines_quebec_ontario_10lb_rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gojit_interlines_quebec_ontario_10lb_rates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `1_499_rate` decimal(30,2) DEFAULT NULL,
  `10000_19999_rate` decimal(30,2) DEFAULT NULL,
  `destination_zone` varchar(25) DEFAULT NULL,
  `origin_zone` varchar(25) DEFAULT NULL,
  `2000_4999_rate` decimal(30,2) DEFAULT NULL,
  `500_999_rate` decimal(30,2) DEFAULT NULL,
  `lane` varchar(25) DEFAULT NULL,
  `min_rate` decimal(30,2) DEFAULT NULL,
  `1000_1999_rate` decimal(30,2) DEFAULT NULL,
  `5000_9999_rate` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gojit_zone_transitday_mapping`
--

DROP TABLE IF EXISTS `gojit_zone_transitday_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gojit_zone_transitday_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transit_days` int(11) DEFAULT NULL,
  `destination_zone` varchar(25) DEFAULT NULL,
  `origin_zone` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2338 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instant_quote`
--

DROP TABLE IF EXISTS `instant_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(300) DEFAULT NULL,
  `from_country` varchar(150) DEFAULT NULL,
  `business_name` varchar(600) DEFAULT NULL,
  `to_zip` varchar(150) DEFAULT NULL,
  `from_city` varchar(150) DEFAULT NULL,
  `to_country` varchar(150) DEFAULT NULL,
  `last_name` varchar(600) DEFAULT NULL,
  `to_city` varchar(150) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `to_state` varchar(150) DEFAULT NULL,
  `total_weight` double DEFAULT NULL,
  `from_state` varchar(150) DEFAULT NULL,
  `phone` varchar(150) DEFAULT NULL,
  `pkg_quantity` bigint(10) DEFAULT NULL,
  `from_zip` varchar(150) DEFAULT NULL,
  `first_name` varchar(600) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3467 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instant_quote_package`
--

DROP TABLE IF EXISTS `instant_quote_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_quote_package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quote_id` bigint(20) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `freight_class` varchar(10) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6001 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `us_state_tax` decimal(30,2) DEFAULT NULL,
  `notes` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `discount` decimal(30,2) DEFAULT NULL,
  `qst_tax` decimal(30,2) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `date_paid` datetime DEFAULT NULL,
  `terms` varchar(128) DEFAULT NULL,
  `gst_tax` decimal(30,2) DEFAULT NULL,
  `modified_due_date` datetime DEFAULT NULL,
  `hst_tax` decimal(30,2) DEFAULT NULL,
  `amount` decimal(30,2) DEFAULT NULL,
  `date_generated` datetime DEFAULT NULL,
  `cost` decimal(30,2) DEFAULT NULL,
  `payment_status` int(11) DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `tax` decimal(30,2) DEFAULT NULL,
  `other_taxes` decimal(30,2) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `cur_id` int(11) DEFAULT NULL,
  `paid_at` datetime DEFAULT NULL,
  `us_fed_tax` decimal(30,2) DEFAULT NULL,
  `payment_type` int(11) DEFAULT NULL,
  `paid_amount` decimal(30,2) DEFAULT NULL,
  `adjustment` decimal(30,2) DEFAULT NULL,
  `pst_tax` decimal(30,2) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `exclude_from_claim` tinyint(1) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110434 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice_comment`
--

DROP TABLE IF EXISTS `invoice_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `internal_only` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `text` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice_status_change`
--

DROP TABLE IF EXISTS `invoice_status_change`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_status_change` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `old_status` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `new_status` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lead`
--

DROP TABLE IF EXISTS `lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lead` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comments` text,
  `owner_user_id` bigint(20) DEFAULT NULL,
  `city` varchar(128) DEFAULT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `cell` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `company_name` varchar(128) DEFAULT NULL,
  `first_name` varchar(128) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logged_event`
--

DROP TABLE IF EXISTS `logged_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logged_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `private` tinyint(1) DEFAULT NULL,
  `entity_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `message_type` varchar(255) DEFAULT NULL,
  `comment` varchar(750) DEFAULT NULL,
  `entity_id` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5583979 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `market`
--

DROP TABLE IF EXISTS `market`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `closed_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `markup_id` bigint(20) DEFAULT NULL,
  `rate_adjustments_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `open_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `markup`
--

DROP TABLE IF EXISTS `markup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `markup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_at` datetime DEFAULT NULL,
  `fixed_amount` varchar(128) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `accessorial_type_id` bigint(20) DEFAULT NULL,
  `min_amount` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `fraction` varchar(128) DEFAULT NULL,
  `calc_from` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `max_amount` varchar(128) DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `merchant_account`
--

DROP TABLE IF EXISTS `merchant_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `merchant_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_property1` varchar(50) DEFAULT NULL,
  `base_country` varchar(10) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `processor` varchar(50) DEFAULT NULL,
  `property5` varchar(50) DEFAULT NULL,
  `property2` varchar(50) DEFAULT NULL,
  `property1` varchar(50) DEFAULT NULL,
  `property4` varchar(50) DEFAULT NULL,
  `property3` varchar(50) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `test_property4` varchar(50) DEFAULT NULL,
  `test_property5` varchar(50) DEFAULT NULL,
  `test_property2` varchar(50) DEFAULT NULL,
  `test_property3` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_msgbox_id` bigint(20) DEFAULT NULL,
  `subject` varchar(128) DEFAULT NULL,
  `to_msgbox_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `priority` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `sent_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `to_user_id` bigint(20) DEFAULT NULL,
  `is_draft` varchar(128) DEFAULT NULL,
  `read_at` datetime DEFAULT NULL,
  `from_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `msgbox`
--

DROP TABLE IF EXISTS `msgbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `msgbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `text` text,
  `title` varchar(255) DEFAULT NULL,
  `date_expired` datetime DEFAULT NULL,
  `location_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nmfc_code`
--

DROP TABLE IF EXISTS `nmfc_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nmfc_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(33) DEFAULT NULL,
  `description` text,
  `category` text,
  `freight_class` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `opp_stage`
--

DROP TABLE IF EXISTS `opp_stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `opp_stage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `close_likelihood` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `opp_stage_opportunity`
--

DROP TABLE IF EXISTS `opp_stage_opportunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `opp_stage_opportunity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `opportunity_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `oppstage_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `opportunity`
--

DROP TABLE IF EXISTS `opportunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `opportunity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `closed_at` datetime DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `owner_user_id` bigint(20) DEFAULT NULL,
  `value_estd` decimal(20,2) DEFAULT NULL,
  `value_booked` decimal(20,2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `stage` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `read_at` datetime DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_cost_franchise` decimal(30,2) DEFAULT NULL,
  `origin_terminal` bigint(20) DEFAULT NULL,
  `origin_close_time_h` varchar(45) DEFAULT NULL,
  `total_cost` decimal(30,2) DEFAULT NULL,
  `eshipper_user` int(11) DEFAULT NULL,
  `submitted_at` datetime DEFAULT NULL,
  `quoted_fuel_cost` decimal(30,2) DEFAULT NULL,
  `package_id` bigint(20) DEFAULT NULL,
  `origin_close_time_m` varchar(45) DEFAULT NULL,
  `ship_to_id` bigint(20) DEFAULT NULL,
  `markup_perc` float DEFAULT NULL,
  `comm_status` int(11) DEFAULT NULL,
  `bol_id` varchar(255) DEFAULT NULL,
  `status_id` bigint(20) DEFAULT NULL,
  `operating_cost_perc` double DEFAULT NULL,
  `distribution_quote` float DEFAULT NULL,
  `is_from_mf_to_f` tinyint(1) DEFAULT NULL,
  `inside_pickup` tinyint(1) DEFAULT NULL,
  `ship_to_address_id` bigint(20) DEFAULT NULL,
  `job_number` varchar(255) DEFAULT NULL,
  `quoted_total_charge` decimal(30,2) DEFAULT NULL,
  `trailer_reference1` varchar(15) DEFAULT NULL,
  `trailer_reference2` varchar(15) DEFAULT NULL,
  `update_color_code` bit(1) DEFAULT NULL,
  `shipment_error` varchar(1000) DEFAULT NULL,
  `homeland_security` tinyint(1) DEFAULT NULL,
  `ship_from_id` bigint(20) DEFAULT NULL,
  `bill_to_id` bigint(20) DEFAULT NULL,
  `frozen` tinyint(1) DEFAULT NULL,
  `trailer_weight2` int(11) DEFAULT NULL,
  `trailer_weight1` int(11) DEFAULT NULL,
  `total_charge` decimal(30,2) DEFAULT NULL,
  `pod_name` varchar(255) DEFAULT NULL,
  `charge_comments` varchar(500) DEFAULT NULL,
  `master_tracking_num_1` varchar(255) DEFAULT NULL,
  `excess_length` tinyint(1) DEFAULT NULL,
  `tax_type` int(11) DEFAULT NULL,
  `quoted_total_cost` decimal(30,2) DEFAULT NULL,
  `cod_value` float DEFAULT NULL,
  `shipment_density` int(11) DEFAULT NULL,
  `tender_error_message` text,
  `tran_fee` decimal(30,2) DEFAULT NULL,
  `reference_name` varchar(255) DEFAULT NULL,
  `partner_markup` float DEFAULT NULL,
  `guaranteed_dropoff_at` datetime DEFAULT NULL,
  `customs_currency` varchar(255) DEFAULT NULL,
  `freight_tariff` decimal(30,2) DEFAULT NULL,
  `customs_contact_name` varchar(255) DEFAULT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `fuel_tariff` decimal(30,2) DEFAULT NULL,
  `order_batch_id` bigint(20) DEFAULT NULL,
  `currency_rate` float DEFAULT NULL,
  `dangerous_goods` varchar(255) DEFAULT NULL,
  `internal_notes` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tot_act_wgt` float DEFAULT NULL,
  `to_airport_code` varchar(255) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `ship_from_address_id` bigint(20) DEFAULT NULL,
  `tracking_url` varchar(600) DEFAULT NULL,
  `sort_segregate` tinyint(1) DEFAULT NULL,
  `mode_transport` varchar(255) DEFAULT NULL,
  `mark_and_tag_freight` tinyint(1) DEFAULT NULL,
  `mfw_booking_key` varchar(250) DEFAULT NULL,
  `carrier_pick_up_conf` varchar(25) DEFAULT NULL,
  `comm_driver_id` bigint(20) DEFAULT NULL,
  `is_e_manifested` bit(1) DEFAULT NULL,
  `proof_of_delivery` varchar(255) DEFAULT NULL,
  `franchise_markup` float DEFAULT NULL,
  `from_datetime` datetime DEFAULT NULL,
  `customes_freight` tinyint(1) DEFAULT NULL,
  `gross_profit` decimal(30,2) DEFAULT NULL,
  `rate_error` varchar(1000) DEFAULT NULL,
  `destination_terminal` bigint(20) DEFAULT NULL,
  `pod_file_name` varchar(45) DEFAULT NULL,
  `tot_act_quantity` int(11) DEFAULT NULL,
  `hold` tinyint(1) DEFAULT NULL,
  `cod_payment` varchar(255) DEFAULT NULL,
  `commissionable_amount` decimal(30,2) DEFAULT NULL,
  `job_pickup_date` datetime DEFAULT NULL,
  `insurance_value_3rd` float DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `wait_time` int(11) DEFAULT NULL,
  `pod_timestamp` datetime DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `fuel_surcharge` decimal(30,2) DEFAULT NULL,
  `edi_verified` bit(1) DEFAULT NULL,
  `insurance_type` int(11) DEFAULT NULL,
  `to_location_type` int(11) DEFAULT NULL,
  `total_tariff` decimal(30,2) DEFAULT NULL,
  `original_markdown_type` int(10) unsigned DEFAULT NULL,
  `eshipper_order_id` bigint(20) DEFAULT NULL,
  `per_cubic_feet` varchar(21) DEFAULT NULL,
  `adjustment` decimal(30,2) DEFAULT NULL,
  `customs_broker_name` varchar(255) DEFAULT NULL,
  `invoice_status` tinyint(3) unsigned DEFAULT NULL,
  `transit_days` int(11) DEFAULT NULL,
  `bill_to` varchar(45) DEFAULT NULL,
  `reference3` varchar(255) DEFAULT NULL,
  `distribution_service_id` bigint(20) DEFAULT NULL,
  `param_service_id` bigint(20) DEFAULT NULL,
  `expected_delivery_date` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `reference2` varchar(255) DEFAULT NULL,
  `cod_pin_number` varchar(15) DEFAULT NULL,
  `customs_fax_number` varchar(255) DEFAULT NULL,
  `return_service` tinyint(1) DEFAULT NULL,
  `has_been_edited` bit(1) DEFAULT NULL,
  `canadian_customs` tinyint(1) DEFAULT NULL,
  `fuel_perc_charge` float DEFAULT NULL,
  `bill_to_contact` varchar(255) DEFAULT NULL,
  `reference_code` varchar(255) DEFAULT NULL,
  `notes_1` varchar(500) DEFAULT NULL,
  `notes_2` varchar(500) DEFAULT NULL,
  `load_freight_at_pickup` tinyint(1) DEFAULT NULL,
  `reference2_name` varchar(255) DEFAULT NULL,
  `trucking_prealert_id` bigint(20) DEFAULT NULL,
  `stackable` tinyint(1) DEFAULT NULL,
  `trailer_seal_number2` varchar(15) DEFAULT NULL,
  `trailer_seal_number1` varchar(15) DEFAULT NULL,
  `merged` bit(1) DEFAULT NULL,
  `protective_pallet_cover` tinyint(1) DEFAULT NULL,
  `quoted_base_cost` decimal(30,2) DEFAULT NULL,
  `tax_rate_id` bigint(20) DEFAULT NULL,
  `from_airline_name` varchar(255) DEFAULT NULL,
  `ship_date` datetime DEFAULT NULL,
  `shipment_type` varchar(128) DEFAULT NULL,
  `reconcil__adjustment` decimal(30,2) DEFAULT NULL,
  `quoted_base_charge` decimal(30,2) DEFAULT NULL,
  `fuel_cost` decimal(30,2) DEFAULT NULL,
  `cod_cheque_date` date DEFAULT NULL,
  `load_id` bigint(20) DEFAULT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `scheduled_ship_date` datetime DEFAULT NULL,
  `package_type_name` varchar(255) DEFAULT NULL,
  `from_location_type` int(11) DEFAULT NULL,
  `freeze_protect` tinyint(1) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `trailer_carton_count2` int(11) DEFAULT NULL,
  `trailer_carton_count1` int(11) DEFAULT NULL,
  `cod_payable_to` varchar(255) DEFAULT NULL,
  `pier_charge` tinyint(1) DEFAULT NULL,
  `guarantee_charge` int(11) DEFAULT NULL,
  `inside_delivery` tinyint(1) DEFAULT NULL,
  `sat_delivery` tinyint(1) DEFAULT NULL,
  `payment_type_id` tinyint(1) DEFAULT NULL,
  `military_base_delivery` tinyint(1) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `manifest_number` varchar(255) DEFAULT NULL,
  `mfw_connection_key` text,
  `insured_amount` float DEFAULT NULL,
  `pod_given_to` varchar(255) DEFAULT NULL,
  `trailer_number` varchar(15) DEFAULT NULL,
  `quoted_by` varchar(255) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `protective_drum_cover` tinyint(1) DEFAULT NULL,
  `reference3_name` varchar(255) DEFAULT NULL,
  `limited_access` tinyint(1) DEFAULT NULL,
  `pickup_time` varchar(255) DEFAULT NULL,
  `req_delivery_date` varchar(255) DEFAULT NULL,
  `vehicle_full` bit(1) DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `dim_type` int(11) DEFAULT NULL,
  `affiliate_fee` decimal(30,2) DEFAULT NULL,
  `batch_id` varchar(50) DEFAULT NULL,
  `claim_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `from_airport_code` varchar(255) DEFAULT NULL,
  `tender_status` text,
  `pod_file` mediumblob,
  `expected_transit` int(11) DEFAULT NULL,
  `job_radio` varchar(255) DEFAULT NULL,
  `master_tracking_num` varchar(255) DEFAULT NULL,
  `tax_type2` int(11) DEFAULT NULL,
  `customs_phone_number` varchar(255) DEFAULT NULL,
  `trailer_dimension_weight` float DEFAULT NULL,
  `bol_number` varchar(250) DEFAULT NULL,
  `billing_status` tinyint(3) unsigned DEFAULT NULL,
  `actual_base_charge` decimal(30,2) DEFAULT NULL,
  `unload_freight_at_del` tinyint(1) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `date_of_delivery` datetime DEFAULT NULL,
  `vehicle_type` int(11) DEFAULT NULL,
  `destination_close_time_h` varchar(45) DEFAULT NULL,
  `job_quantity` int(11) DEFAULT NULL,
  `destination_close_time_m` varchar(45) DEFAULT NULL,
  `delivery_appt` tinyint(1) DEFAULT NULL,
  `parent_ediwo_id` bigint(20) DEFAULT NULL,
  `to_datetime` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `fuel_perc_cost` float DEFAULT NULL,
  `assign_driver_id` bigint(20) DEFAULT NULL,
  `quoted_fuel_surcharge` decimal(30,2) DEFAULT NULL,
  `charged_weight` float DEFAULT NULL,
  `prev_invoice_number` bigint(20) DEFAULT NULL,
  `paper_manifest_status` int(11) DEFAULT NULL,
  `scheduled_pick_up_id` bigint(20) DEFAULT NULL,
  `quoted_amount` varchar(255) DEFAULT NULL,
  `comm_amount` decimal(30,2) DEFAULT NULL,
  `exibition_site` tinyint(1) DEFAULT NULL,
  `color_code` varchar(30) DEFAULT NULL,
  `is_insurance_manifested` bit(1) DEFAULT NULL,
  `eshipper_oid` bigint(20) DEFAULT NULL,
  `requested_pickup_at` datetime DEFAULT NULL,
  `job_deliv_deadline` datetime DEFAULT NULL,
  `delivery_prealert_id` bigint(20) DEFAULT NULL,
  `insurance_currency` int(3) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `cur_code` int(11) DEFAULT NULL,
  `job_weight` int(11) DEFAULT NULL,
  `customs_value` decimal(30,2) DEFAULT NULL,
  `after_hours` bit(1) DEFAULT NULL,
  `job_content_descr` varchar(255) DEFAULT NULL,
  `operating_cost` decimal(30,2) DEFAULT NULL,
  `pickup_num` int(11) DEFAULT NULL,
  `cod_currency` varchar(255) DEFAULT NULL,
  `to_airline_name` varchar(255) DEFAULT NULL,
  `base_cost` decimal(30,2) DEFAULT NULL,
  `quoted_at` datetime DEFAULT NULL,
  `cross_border_fee` tinyint(1) DEFAULT NULL,
  `signature_required` int(11) DEFAULT NULL,
  `quote_status` int(11) DEFAULT NULL,
  `heated__service` tinyint(1) DEFAULT NULL,
  `in_bond_fee` tinyint(1) DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `insurance_value` float DEFAULT NULL,
  `single_shipment` tinyint(1) DEFAULT NULL,
  `franchise_id` bigint(20) DEFAULT NULL,
  `sub_type` int(10) unsigned DEFAULT NULL,
  `job_quantity_type` int(11) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `base_charge` decimal(30,2) DEFAULT NULL,
  `customer_markdown` float DEFAULT NULL,
  `quantity` bigint(20) DEFAULT NULL,
  `pickup_time_h` varchar(45) DEFAULT NULL,
  `pickup_time_m` varchar(45) DEFAULT NULL,
  `editable` tinyint(1) DEFAULT NULL,
  `customer_markup` float DEFAULT NULL,
  `customer_markup_type` int(10) unsigned DEFAULT NULL,
  `special_equipment` varchar(255) DEFAULT NULL,
  `actual_weight` float DEFAULT NULL,
  `update_status` bit(1) DEFAULT NULL,
  `return_order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=501274 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accessorials`
--

DROP TABLE IF EXISTS `order_accessorials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accessorials` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL,
  `accessorial_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_payable_category`
--

DROP TABLE IF EXISTS `order_accounts_payable_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_payable_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_payable_cheque`
--

DROP TABLE IF EXISTS `order_accounts_payable_cheque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_payable_cheque` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(100) DEFAULT NULL,
  `cur_code` int(11) DEFAULT NULL,
  `cheque_number` varchar(255) DEFAULT NULL,
  `wire_trans_number` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `payable_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4438 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_payable_payables`
--

DROP TABLE IF EXISTS `order_accounts_payable_payables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_payable_payables` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=676 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_payable_payables_category`
--

DROP TABLE IF EXISTS `order_accounts_payable_payables_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_payable_payables_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_id` bigint(20) DEFAULT NULL,
  `payable_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=555 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_payable_transactions`
--

DROP TABLE IF EXISTS `order_accounts_payable_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_payable_transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tax_amount` decimal(30,2) DEFAULT NULL,
  `amount` decimal(30,2) DEFAULT NULL,
  `pay_idate` datetime DEFAULT NULL,
  `pay_paid` bit(1) DEFAULT NULL,
  `pay_ref_id` varchar(2000) DEFAULT NULL,
  `pay_desc` varchar(2000) DEFAULT NULL,
  `pay_pdate` datetime DEFAULT NULL,
  `invoice_amount` decimal(30,2) DEFAULT NULL,
  `tax_amount2` decimal(30,2) DEFAULT NULL,
  `tax_amount3` decimal(30,2) DEFAULT NULL,
  `pay_cat_id` bigint(20) DEFAULT NULL,
  `pay_cur_id` int(11) DEFAULT NULL,
  `tax_type3` varchar(10) DEFAULT NULL,
  `tax_type2` varchar(10) DEFAULT NULL,
  `tax_type` varchar(10) DEFAULT NULL,
  `pay_payable_id` bigint(20) DEFAULT NULL,
  `pay_ddate` datetime DEFAULT NULL,
  `pay_cheque` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9573 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_receivable`
--

DROP TABLE IF EXISTS `order_accounts_receivable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_receivable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comments` varchar(255) DEFAULT NULL,
  `update_ar_currency_id` int(1) DEFAULT NULL,
  `transaction_number` varchar(255) DEFAULT NULL,
  `amount_received` decimal(30,2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `date_received` datetime DEFAULT NULL,
  `cn` varchar(255) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `reversed` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74917 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_accounts_receivable_details`
--

DROP TABLE IF EXISTS `order_accounts_receivable_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_accounts_receivable_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount_paid` decimal(30,2) DEFAULT NULL,
  `check_date` datetime DEFAULT NULL,
  `rec_id1` bigint(20) DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `check_num` varchar(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rec_id` bigint(20) DEFAULT NULL,
  `reversed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109062 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_agent`
--

DROP TABLE IF EXISTS `order_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_agent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `other_number` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `fax_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_ap_cheque_trans`
--

DROP TABLE IF EXISTS `order_ap_cheque_trans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_ap_cheque_trans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cheque_id` bigint(20) DEFAULT NULL,
  `pay_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6809 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_batch`
--

DROP TABLE IF EXISTS `order_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_business`
--

DROP TABLE IF EXISTS `order_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_business` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_charges`
--

DROP TABLE IF EXISTS `order_charges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_charges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_comm_report`
--

DROP TABLE IF EXISTS `order_comm_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_comm_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(30,2) DEFAULT NULL,
  `direct_report` int(11) DEFAULT NULL,
  `sales_comm_perc` double DEFAULT NULL,
  `sub_sales_comm_perc` double DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `gst_tax` decimal(30,2) DEFAULT NULL,
  `sales_agent_id` bigint(20) DEFAULT NULL,
  `cur_id` int(11) DEFAULT NULL,
  `comm_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5356 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_comm_report_customer`
--

DROP TABLE IF EXISTS `order_comm_report_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_comm_report_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `elt` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_comm_report_workorder`
--

DROP TABLE IF EXISTS `order_comm_report_workorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_comm_report_workorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155802 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_credit_note`
--

DROP TABLE IF EXISTS `order_credit_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_credit_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(30,2) DEFAULT NULL,
  `private` tinyint(1) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7030 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_delivery_pre_alert`
--

DROP TABLE IF EXISTS `order_delivery_pre_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_delivery_pre_alert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent` varchar(255) DEFAULT NULL,
  `special_instructions` varchar(255) DEFAULT NULL,
  `agent_fax` varchar(255) DEFAULT NULL,
  `agent_datetime` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `agent_phone` varchar(255) DEFAULT NULL,
  `delivery_address_id` bigint(20) DEFAULT NULL,
  `delivery_datetime` datetime DEFAULT NULL,
  `second_agent_used` bit(1) DEFAULT NULL,
  `number_of_pieces` int(11) DEFAULT NULL,
  `handling_information` varchar(255) DEFAULT NULL,
  `agent_contact` varchar(255) DEFAULT NULL,
  `consignee_address_id` bigint(20) DEFAULT NULL,
  `shipper_address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2536 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_docs`
--

DROP TABLE IF EXISTS `order_docs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_docs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `private_mode` tinyint(1) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `upload_file` mediumblob,
  `upload_by` varchar(50) DEFAULT NULL,
  `upload_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2637 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_document`
--

DROP TABLE IF EXISTS `order_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webpal_id` bigint(20) DEFAULT NULL,
  `other` varchar(128) DEFAULT NULL,
  `viewed_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_charge`
--

DROP TABLE IF EXISTS `order_edi_charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_charge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20) DEFAULT NULL,
  `charge_group_id` int(11) DEFAULT NULL,
  `eshipper_surcharge_id` int(11) DEFAULT NULL,
  `charge_name` varchar(255) DEFAULT NULL,
  `tax_type` int(11) DEFAULT NULL,
  `carrier_charge_code` varchar(255) DEFAULT NULL,
  `tax_rate` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3457 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_charge_group`
--

DROP TABLE IF EXISTS `order_edi_charge_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_charge_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `formula_rate` float DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `group_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_canadapost`
--

DROP TABLE IF EXISTS `order_edi_detail_canadapost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_canadapost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `promotnl_discnt2_amt` varchar(20) DEFAULT NULL,
  `hst_amt` varchar(20) DEFAULT NULL,
  `adcard_diecut` varchar(20) DEFAULT NULL,
  `wght_per_piece` varchar(20) DEFAULT NULL,
  `dlvy_confirm_charge` varchar(20) DEFAULT NULL,
  `promotnl_discnt2_rate` varchar(20) DEFAULT NULL,
  `sold_to_cust_id` varchar(30) DEFAULT NULL,
  `rate_code` varchar(10) DEFAULT NULL,
  `on_dmnd_pkp_value` varchar(20) DEFAULT NULL,
  `sold_to_cust_number` varchar(10) DEFAULT NULL,
  `cod_charge` varchar(20) DEFAULT NULL,
  `sold_to_cust_pcode` varchar(10) DEFAULT NULL,
  `product_id` varchar(40) DEFAULT NULL,
  `payment` varchar(20) DEFAULT NULL,
  `ship_to_cust_name` varchar(40) DEFAULT NULL,
  `auto_discnt_value` varchar(20) DEFAULT NULL,
  `invoice_number` varchar(10) DEFAULT NULL,
  `orig_invoice_no` varchar(20) DEFAULT NULL,
  `on_dmnd_pkp_qnty` varchar(20) DEFAULT NULL,
  `quantity_add_covrge` varchar(20) DEFAULT NULL,
  `cust_number` varchar(10) DEFAULT NULL,
  `qnty_dlvy` varchar(20) DEFAULT NULL,
  `emrgncy_order_fee` varchar(20) DEFAULT NULL,
  `promotnl_discnt3_rate` varchar(20) DEFAULT NULL,
  `addr_acurcy_exp_date` varchar(10) DEFAULT NULL,
  `addr_acurcy_charge` varchar(20) DEFAULT NULL,
  `ovr_size_not_pkg_charge` varchar(20) DEFAULT NULL,
  `invoice_due_date` varchar(10) DEFAULT NULL,
  `future_use20` varchar(20) DEFAULT NULL,
  `metered_amt` varchar(20) DEFAULT NULL,
  `cust_order_ref` varchar(20) DEFAULT NULL,
  `future_use21` varchar(20) DEFAULT NULL,
  `cntct_discnt2_amt` varchar(20) DEFAULT NULL,
  `machnblty_charge` varchar(20) DEFAULT NULL,
  `add_covrge_charge` varchar(20) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `qnty_transptn` varchar(20) DEFAULT NULL,
  `tot_charges` varchar(20) DEFAULT NULL,
  `qnty_subsidy` varchar(20) DEFAULT NULL,
  `transptn_charge` varchar(20) DEFAULT NULL,
  `sold_to_cust_addr2` varchar(40) DEFAULT NULL,
  `sold_to_cust_addr1` varchar(40) DEFAULT NULL,
  `qnty_advc_rcpt` varchar(20) DEFAULT NULL,
  `net_charge` varchar(20) DEFAULT NULL,
  `ship_to_cust_addr1` varchar(40) DEFAULT NULL,
  `ship_to_cust_addr2` varchar(40) DEFAULT NULL,
  `invoice_date` varchar(10) DEFAULT NULL,
  `dlvy_opt_desc` varchar(20) DEFAULT NULL,
  `qnty_dlvy_confirm` varchar(20) DEFAULT NULL,
  `qnty_fsm_adjstmt` varchar(20) DEFAULT NULL,
  `line_number` varchar(10) DEFAULT NULL,
  `qnty_addr_acurcy` varchar(20) DEFAULT NULL,
  `addr_acurcy_percntge` varchar(10) DEFAULT NULL,
  `prov_tax_code` varchar(2) DEFAULT NULL,
  `psg_tax_status` varchar(30) DEFAULT NULL,
  `future_use8` varchar(20) DEFAULT NULL,
  `restock_fee` varchar(20) DEFAULT NULL,
  `future_use7` varchar(20) DEFAULT NULL,
  `po_date` varchar(10) DEFAULT NULL,
  `future_use9` varchar(20) DEFAULT NULL,
  `future_use4` varchar(20) DEFAULT NULL,
  `future_use6` varchar(20) DEFAULT NULL,
  `future_use5` varchar(20) DEFAULT NULL,
  `interfac_sys_date` varchar(20) DEFAULT NULL,
  `future_use1` varchar(20) DEFAULT NULL,
  `promotnl_discnt1_amt` varchar(20) DEFAULT NULL,
  `ocr_adjstmt_fee` varchar(20) DEFAULT NULL,
  `ship_to_cust_pcode` varchar(10) DEFAULT NULL,
  `ship_to_cust_prov` varchar(2) DEFAULT NULL,
  `article_no` varchar(60) DEFAULT NULL,
  `ship_to_cust_city` varchar(30) DEFAULT NULL,
  `adcard_8point` varchar(20) DEFAULT NULL,
  `gst_amt` varchar(20) DEFAULT NULL,
  `mail_behalf_cust_no` varchar(10) DEFAULT NULL,
  `promotnl_discnt3_amt` varchar(20) DEFAULT NULL,
  `gst_hst_tax_status` varchar(30) DEFAULT NULL,
  `svc_desc` varchar(40) DEFAULT NULL,
  `mail_by_cust_number` varchar(10) DEFAULT NULL,
  `man_som_ord_proces_fee` varchar(20) DEFAULT NULL,
  `qnty_regstrd` varchar(20) DEFAULT NULL,
  `agree_number` varchar(10) DEFAULT NULL,
  `dlvy_date` varchar(10) DEFAULT NULL,
  `regstrd_charge` varchar(20) DEFAULT NULL,
  `price_per_piece` varchar(20) DEFAULT NULL,
  `qnty_ovr_size_not_pkg` varchar(20) DEFAULT NULL,
  `adcard_scratch_win` varchar(20) DEFAULT NULL,
  `auto_discnt_rate` varchar(20) DEFAULT NULL,
  `mail_tube_charge` varchar(20) DEFAULT NULL,
  `quantity_cod` varchar(20) DEFAULT NULL,
  `adcard_fifth_color` varchar(20) DEFAULT NULL,
  `dlvy_number` varchar(10) DEFAULT NULL,
  `pap_reg_number` varchar(20) DEFAULT NULL,
  `adcard_10point` varchar(20) DEFAULT NULL,
  `min_order_fee` varchar(20) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `dlvy_charge` varchar(20) DEFAULT NULL,
  `cntct_discnt1_amt` varchar(20) DEFAULT NULL,
  `cust_addr2` varchar(40) DEFAULT NULL,
  `cust_addr1` varchar(40) DEFAULT NULL,
  `qnty_sign_option` varchar(20) DEFAULT NULL,
  `phntm_surcharge` varchar(20) DEFAULT NULL,
  `addtn_info2` varchar(80) DEFAULT NULL,
  `quantity_shipped` varchar(20) DEFAULT NULL,
  `qnty_mail_tube` varchar(20) DEFAULT NULL,
  `order_number` varchar(10) DEFAULT NULL,
  `adcard_perforatn` varchar(20) DEFAULT NULL,
  `future_use17` varchar(20) DEFAULT NULL,
  `sold_to_cust_name` varchar(40) DEFAULT NULL,
  `future_use16` varchar(20) DEFAULT NULL,
  `future_use19` varchar(20) DEFAULT NULL,
  `doc_type` varchar(10) DEFAULT NULL,
  `sign_option_charge` varchar(20) DEFAULT NULL,
  `future_use18` varchar(20) DEFAULT NULL,
  `fsm_adjstmt_fee` varchar(20) DEFAULT NULL,
  `cntct_discnt1_rate` varchar(20) DEFAULT NULL,
  `msg_box_lop` varchar(360) DEFAULT NULL,
  `po_number` varchar(20) DEFAULT NULL,
  `future_use11` varchar(20) DEFAULT NULL,
  `wght_price` varchar(20) DEFAULT NULL,
  `future_use10` varchar(20) DEFAULT NULL,
  `future_use13` varchar(20) DEFAULT NULL,
  `future_use12` varchar(20) DEFAULT NULL,
  `future_use15` varchar(20) DEFAULT NULL,
  `future_use14` varchar(20) DEFAULT NULL,
  `mail_by_cust_name` varchar(40) DEFAULT NULL,
  `base_charge` varchar(20) DEFAULT NULL,
  `fuel_surcharge_amt` varchar(20) DEFAULT NULL,
  `cust_name` varchar(40) DEFAULT NULL,
  `cntct_discnt2_rate` varchar(20) DEFAULT NULL,
  `pst_amt` varchar(20) DEFAULT NULL,
  `cost_center_ref` varchar(30) DEFAULT NULL,
  `qnty_phntm_surcharge` varchar(20) DEFAULT NULL,
  `sold_to_cust_prov` varchar(2) DEFAULT NULL,
  `subsidy_value` varchar(20) DEFAULT NULL,
  `mail_behalf_cust_name` varchar(40) DEFAULT NULL,
  `fuel_surcharge_rate` varchar(20) DEFAULT NULL,
  `sold_to_cust_city` varchar(30) DEFAULT NULL,
  `cust_pcode` varchar(10) DEFAULT NULL,
  `record_type` varchar(2) DEFAULT NULL,
  `ship_to_cust_no` varchar(10) DEFAULT NULL,
  `advc_rcpt_surcharge` varchar(20) DEFAULT NULL,
  `pay_terms` varchar(40) DEFAULT NULL,
  `qnty_machnblty` varchar(20) DEFAULT NULL,
  `cust_prov` varchar(2) DEFAULT NULL,
  `qnty_ocr_adjstmt` varchar(20) DEFAULT NULL,
  `ind_site_name` varchar(30) DEFAULT NULL,
  `cust_city` varchar(30) DEFAULT NULL,
  `addtn_info1` varchar(80) DEFAULT NULL,
  `promotnl_discnt1_rate` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_cts`
--

DROP TABLE IF EXISTS `order_edi_detail_cts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_cts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `gl13_amt` varchar(50) DEFAULT NULL,
  `accl2` varchar(50) DEFAULT NULL,
  `cartons20` varchar(50) DEFAULT NULL,
  `accl1` varchar(50) DEFAULT NULL,
  `audit_code` varchar(50) DEFAULT NULL,
  `accl4` varchar(50) DEFAULT NULL,
  `accl3` varchar(50) DEFAULT NULL,
  `con_city` varchar(50) DEFAULT NULL,
  `accl6` varchar(50) DEFAULT NULL,
  `accl5` varchar(50) DEFAULT NULL,
  `division` varchar(50) DEFAULT NULL,
  `cartons4` varchar(50) DEFAULT NULL,
  `weight13` varchar(50) DEFAULT NULL,
  `cartons5` varchar(50) DEFAULT NULL,
  `weight14` varchar(50) DEFAULT NULL,
  `cartons6` varchar(50) DEFAULT NULL,
  `weight11` varchar(50) DEFAULT NULL,
  `cartons7` varchar(50) DEFAULT NULL,
  `weight12` varchar(50) DEFAULT NULL,
  `sh_zip` varchar(50) DEFAULT NULL,
  `cartons8` varchar(50) DEFAULT NULL,
  `weight17` varchar(50) DEFAULT NULL,
  `cartons9` varchar(50) DEFAULT NULL,
  `weight18` varchar(50) DEFAULT NULL,
  `weight15` varchar(50) DEFAULT NULL,
  `weight16` varchar(50) DEFAULT NULL,
  `del_date` varchar(50) DEFAULT NULL,
  `weight10` varchar(50) DEFAULT NULL,
  `invoice_number` varchar(50) DEFAULT NULL,
  `gl3_amt` varchar(50) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `master_pro` varchar(50) DEFAULT NULL,
  `weight19` varchar(50) DEFAULT NULL,
  `cartons1` varchar(50) DEFAULT NULL,
  `cartons2` varchar(50) DEFAULT NULL,
  `cartons3` varchar(50) DEFAULT NULL,
  `con_state` varchar(50) DEFAULT NULL,
  `gl1_amt` varchar(50) DEFAULT NULL,
  `weight20` varchar(50) DEFAULT NULL,
  `gl15_amt` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `class19` varchar(50) DEFAULT NULL,
  `scac` varchar(50) DEFAULT NULL,
  `con_zip` varchar(50) DEFAULT NULL,
  `gl18_amt` varchar(50) DEFAULT NULL,
  `paid_amt` varchar(50) DEFAULT NULL,
  `invoice_date` varchar(50) DEFAULT NULL,
  `gl8_amt` varchar(50) DEFAULT NULL,
  `accl2_amt` varchar(50) DEFAULT NULL,
  `class20` varchar(50) DEFAULT NULL,
  `sh_city` varchar(50) DEFAULT NULL,
  `seq` varchar(50) DEFAULT NULL,
  `recd_date` varchar(50) DEFAULT NULL,
  `gl6_amt` varchar(50) DEFAULT NULL,
  `accl6_amt` varchar(50) DEFAULT NULL,
  `sh_state` varchar(50) DEFAULT NULL,
  `prono` varchar(50) DEFAULT NULL,
  `accl4_amt` varchar(50) DEFAULT NULL,
  `class10` varchar(50) DEFAULT NULL,
  `gl11_amt` varchar(50) DEFAULT NULL,
  `class12` varchar(50) DEFAULT NULL,
  `class11` varchar(50) DEFAULT NULL,
  `class14` varchar(50) DEFAULT NULL,
  `class13` varchar(50) DEFAULT NULL,
  `class16` varchar(50) DEFAULT NULL,
  `class15` varchar(50) DEFAULT NULL,
  `class18` varchar(50) DEFAULT NULL,
  `class17` varchar(50) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `gl12_amt` varchar(50) DEFAULT NULL,
  `gl4_amt` varchar(50) DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `weight9` varchar(50) DEFAULT NULL,
  `weight8` varchar(50) DEFAULT NULL,
  `batch` varchar(50) DEFAULT NULL,
  `gl2_amt` varchar(50) DEFAULT NULL,
  `gl16_amt` varchar(50) DEFAULT NULL,
  `class7` varchar(50) DEFAULT NULL,
  `gl14_amt` varchar(50) DEFAULT NULL,
  `gl20_amt` varchar(50) DEFAULT NULL,
  `class6` varchar(50) DEFAULT NULL,
  `class5` varchar(50) DEFAULT NULL,
  `class4` varchar(50) DEFAULT NULL,
  `class3` varchar(50) DEFAULT NULL,
  `class2` varchar(50) DEFAULT NULL,
  `class1` varchar(50) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `billed_amt` varchar(50) DEFAULT NULL,
  `accl1_amt` varchar(50) DEFAULT NULL,
  `bol` varchar(50) DEFAULT NULL,
  `gl9_amt` varchar(50) DEFAULT NULL,
  `custno` varchar(50) DEFAULT NULL,
  `accl3_amt` varchar(50) DEFAULT NULL,
  `class9` varchar(50) DEFAULT NULL,
  `class8` varchar(50) DEFAULT NULL,
  `gl20` varchar(50) DEFAULT NULL,
  `gl7_amt` varchar(50) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `gl17_amt` varchar(50) DEFAULT NULL,
  `cartons10` varchar(50) DEFAULT NULL,
  `cartons12` varchar(50) DEFAULT NULL,
  `gl10_amt` varchar(50) DEFAULT NULL,
  `cartons11` varchar(50) DEFAULT NULL,
  `cartons14` varchar(50) DEFAULT NULL,
  `cartons13` varchar(50) DEFAULT NULL,
  `weight7` varchar(50) DEFAULT NULL,
  `cartons16` varchar(50) DEFAULT NULL,
  `weight6` varchar(50) DEFAULT NULL,
  `cartons15` varchar(50) DEFAULT NULL,
  `weight5` varchar(50) DEFAULT NULL,
  `cartons18` varchar(50) DEFAULT NULL,
  `weight4` varchar(50) DEFAULT NULL,
  `cartons17` varchar(50) DEFAULT NULL,
  `gl2` varchar(50) DEFAULT NULL,
  `weight3` varchar(50) DEFAULT NULL,
  `gl1` varchar(50) DEFAULT NULL,
  `weight2` varchar(50) DEFAULT NULL,
  `cartons19` varchar(50) DEFAULT NULL,
  `gl4` varchar(50) DEFAULT NULL,
  `weight1` varchar(50) DEFAULT NULL,
  `gl3` varchar(50) DEFAULT NULL,
  `gl6` varchar(50) DEFAULT NULL,
  `gl5` varchar(50) DEFAULT NULL,
  `gl19_amt` varchar(50) DEFAULT NULL,
  `gl8` varchar(50) DEFAULT NULL,
  `gl7` varchar(50) DEFAULT NULL,
  `gl10` varchar(50) DEFAULT NULL,
  `gl9` varchar(50) DEFAULT NULL,
  `gl11` varchar(50) DEFAULT NULL,
  `gl12` varchar(50) DEFAULT NULL,
  `gl13` varchar(50) DEFAULT NULL,
  `gl14` varchar(50) DEFAULT NULL,
  `gl15` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `gl16` varchar(50) DEFAULT NULL,
  `gl17` varchar(50) DEFAULT NULL,
  `gl18` varchar(50) DEFAULT NULL,
  `return_code` varchar(50) DEFAULT NULL,
  `gl5_amt` varchar(50) DEFAULT NULL,
  `gl19` varchar(50) DEFAULT NULL,
  `accl5_amt` varchar(50) DEFAULT NULL,
  `ship_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10615 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_cts2`
--

DROP TABLE IF EXISTS `order_edi_detail_cts2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_cts2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `gl13_amt` varchar(50) DEFAULT NULL,
  `accl2` varchar(50) DEFAULT NULL,
  `cartons20` varchar(50) DEFAULT NULL,
  `accl1` varchar(50) DEFAULT NULL,
  `audit_code` varchar(50) DEFAULT NULL,
  `accl4` varchar(50) DEFAULT NULL,
  `accl3` varchar(50) DEFAULT NULL,
  `con_city` varchar(50) DEFAULT NULL,
  `accl6` varchar(50) DEFAULT NULL,
  `accl5` varchar(50) DEFAULT NULL,
  `division` varchar(50) DEFAULT NULL,
  `cartons4` varchar(50) DEFAULT NULL,
  `weight13` varchar(50) DEFAULT NULL,
  `cartons5` varchar(50) DEFAULT NULL,
  `weight14` varchar(50) DEFAULT NULL,
  `cartons6` varchar(50) DEFAULT NULL,
  `weight11` varchar(50) DEFAULT NULL,
  `cartons7` varchar(50) DEFAULT NULL,
  `weight12` varchar(50) DEFAULT NULL,
  `sh_zip` varchar(50) DEFAULT NULL,
  `cartons8` varchar(50) DEFAULT NULL,
  `weight17` varchar(50) DEFAULT NULL,
  `cartons9` varchar(50) DEFAULT NULL,
  `weight18` varchar(50) DEFAULT NULL,
  `weight15` varchar(50) DEFAULT NULL,
  `weight16` varchar(50) DEFAULT NULL,
  `del_date` varchar(50) DEFAULT NULL,
  `weight10` varchar(50) DEFAULT NULL,
  `invoice_number` varchar(50) DEFAULT NULL,
  `gl3_amt` varchar(50) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `master_pro` varchar(50) DEFAULT NULL,
  `weight19` varchar(50) DEFAULT NULL,
  `cartons1` varchar(50) DEFAULT NULL,
  `cartons2` varchar(50) DEFAULT NULL,
  `cartons3` varchar(50) DEFAULT NULL,
  `con_state` varchar(50) DEFAULT NULL,
  `gl1_amt` varchar(50) DEFAULT NULL,
  `weight20` varchar(50) DEFAULT NULL,
  `gl15_amt` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `class19` varchar(50) DEFAULT NULL,
  `scac` varchar(50) DEFAULT NULL,
  `con_zip` varchar(50) DEFAULT NULL,
  `gl18_amt` varchar(50) DEFAULT NULL,
  `paid_amt` varchar(50) DEFAULT NULL,
  `invoice_date` varchar(50) DEFAULT NULL,
  `gl8_amt` varchar(50) DEFAULT NULL,
  `accl2_amt` varchar(50) DEFAULT NULL,
  `class20` varchar(50) DEFAULT NULL,
  `sh_city` varchar(50) DEFAULT NULL,
  `seq` varchar(50) DEFAULT NULL,
  `recd_date` varchar(50) DEFAULT NULL,
  `gl6_amt` varchar(50) DEFAULT NULL,
  `accl6_amt` varchar(50) DEFAULT NULL,
  `sh_state` varchar(50) DEFAULT NULL,
  `prono` varchar(50) DEFAULT NULL,
  `accl4_amt` varchar(50) DEFAULT NULL,
  `class10` varchar(50) DEFAULT NULL,
  `gl11_amt` varchar(50) DEFAULT NULL,
  `class12` varchar(50) DEFAULT NULL,
  `class11` varchar(50) DEFAULT NULL,
  `class14` varchar(50) DEFAULT NULL,
  `class13` varchar(50) DEFAULT NULL,
  `class16` varchar(50) DEFAULT NULL,
  `class15` varchar(50) DEFAULT NULL,
  `class18` varchar(50) DEFAULT NULL,
  `class17` varchar(50) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `gl12_amt` varchar(50) DEFAULT NULL,
  `gl4_amt` varchar(50) DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `weight9` varchar(50) DEFAULT NULL,
  `weight8` varchar(50) DEFAULT NULL,
  `batch` varchar(50) DEFAULT NULL,
  `gl2_amt` varchar(50) DEFAULT NULL,
  `gl16_amt` varchar(50) DEFAULT NULL,
  `class7` varchar(50) DEFAULT NULL,
  `gl14_amt` varchar(50) DEFAULT NULL,
  `gl20_amt` varchar(50) DEFAULT NULL,
  `class6` varchar(50) DEFAULT NULL,
  `class5` varchar(50) DEFAULT NULL,
  `class4` varchar(50) DEFAULT NULL,
  `class3` varchar(50) DEFAULT NULL,
  `class2` varchar(50) DEFAULT NULL,
  `class1` varchar(50) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `billed_amt` varchar(50) DEFAULT NULL,
  `accl1_amt` varchar(50) DEFAULT NULL,
  `bol` varchar(50) DEFAULT NULL,
  `gl9_amt` varchar(50) DEFAULT NULL,
  `custno` varchar(50) DEFAULT NULL,
  `accl3_amt` varchar(50) DEFAULT NULL,
  `class9` varchar(50) DEFAULT NULL,
  `class8` varchar(50) DEFAULT NULL,
  `gl20` varchar(50) DEFAULT NULL,
  `gl7_amt` varchar(50) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `gl17_amt` varchar(50) DEFAULT NULL,
  `cartons10` varchar(50) DEFAULT NULL,
  `cartons12` varchar(50) DEFAULT NULL,
  `gl10_amt` varchar(50) DEFAULT NULL,
  `cartons11` varchar(50) DEFAULT NULL,
  `cartons14` varchar(50) DEFAULT NULL,
  `cartons13` varchar(50) DEFAULT NULL,
  `weight7` varchar(50) DEFAULT NULL,
  `cartons16` varchar(50) DEFAULT NULL,
  `weight6` varchar(50) DEFAULT NULL,
  `cartons15` varchar(50) DEFAULT NULL,
  `weight5` varchar(50) DEFAULT NULL,
  `cartons18` varchar(50) DEFAULT NULL,
  `weight4` varchar(50) DEFAULT NULL,
  `cartons17` varchar(50) DEFAULT NULL,
  `gl2` varchar(50) DEFAULT NULL,
  `weight3` varchar(50) DEFAULT NULL,
  `gl1` varchar(50) DEFAULT NULL,
  `weight2` varchar(50) DEFAULT NULL,
  `cartons19` varchar(50) DEFAULT NULL,
  `gl4` varchar(50) DEFAULT NULL,
  `weight1` varchar(50) DEFAULT NULL,
  `gl3` varchar(50) DEFAULT NULL,
  `gl6` varchar(50) DEFAULT NULL,
  `gl5` varchar(50) DEFAULT NULL,
  `gl19_amt` varchar(50) DEFAULT NULL,
  `gl8` varchar(50) DEFAULT NULL,
  `gl7` varchar(50) DEFAULT NULL,
  `gl10` varchar(50) DEFAULT NULL,
  `gl9` varchar(50) DEFAULT NULL,
  `gl11` varchar(50) DEFAULT NULL,
  `gl12` varchar(50) DEFAULT NULL,
  `gl13` varchar(50) DEFAULT NULL,
  `gl14` varchar(50) DEFAULT NULL,
  `gl15` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `gl16` varchar(50) DEFAULT NULL,
  `gl17` varchar(50) DEFAULT NULL,
  `gl18` varchar(50) DEFAULT NULL,
  `return_code` varchar(50) DEFAULT NULL,
  `gl5_amt` varchar(50) DEFAULT NULL,
  `gl19` varchar(50) DEFAULT NULL,
  `accl5_amt` varchar(50) DEFAULT NULL,
  `ship_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13113 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_dhl`
--

DROP TABLE IF EXISTS `order_edi_detail_dhl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_dhl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `width_2` varchar(3) DEFAULT NULL,
  `width_1` varchar(3) DEFAULT NULL,
  `pickup_date` varchar(8) DEFAULT NULL,
  `width_4` varchar(3) DEFAULT NULL,
  `width_3` varchar(3) DEFAULT NULL,
  `accessorial_description_06` varchar(25) DEFAULT NULL,
  `accessorial_description_05` varchar(25) DEFAULT NULL,
  `receiver_reference_number` varchar(25) DEFAULT NULL,
  `accessorial_description_04` varchar(25) DEFAULT NULL,
  `accessorial_description_03` varchar(25) DEFAULT NULL,
  `accessorial_description_09` varchar(25) DEFAULT NULL,
  `accessorial_description_08` varchar(25) DEFAULT NULL,
  `accessorial_description_07` varchar(25) DEFAULT NULL,
  `accessorial_description_02` varchar(25) DEFAULT NULL,
  `accessorial_description_01` varchar(25) DEFAULT NULL,
  `assessee_name_extension` varchar(25) DEFAULT NULL,
  `price_zone` varchar(2) DEFAULT NULL,
  `invoice_due_date` varchar(8) DEFAULT NULL,
  `shipment_total_other_charges` varchar(12) DEFAULT NULL,
  `receiver_name_extension` varchar(25) DEFAULT NULL,
  `unit_of_measurement_dimensions` varchar(1) DEFAULT NULL,
  `height_1` varchar(3) DEFAULT NULL,
  `airbill_type_code` varchar(1) DEFAULT NULL,
  `height_2` varchar(3) DEFAULT NULL,
  `height_3` varchar(3) DEFAULT NULL,
  `height_4` varchar(3) DEFAULT NULL,
  `national_discount_amount` varchar(12) DEFAULT NULL,
  `invoice_unit_of_measurement_weight` varchar(2) DEFAULT NULL,
  `fee_basis_amount` varchar(14) DEFAULT NULL,
  `total_item_count` varchar(5) DEFAULT NULL,
  `invoice_date` varchar(8) DEFAULT NULL,
  `attention_of` varchar(25) DEFAULT NULL,
  `sent_by` varchar(25) DEFAULT NULL,
  `bill_to_state` varchar(2) DEFAULT NULL,
  `assessee_city` varchar(20) DEFAULT NULL,
  `freight_charge_amount` varchar(11) DEFAULT NULL,
  `assessee_state` varchar(2) DEFAULT NULL,
  `service_type_code` varchar(2) DEFAULT NULL,
  `shipper_address_2` varchar(25) DEFAULT NULL,
  `length_4` varchar(3) DEFAULT NULL,
  `shipper_address_1` varchar(25) DEFAULT NULL,
  `freight_rate` varchar(10) DEFAULT NULL,
  `length_3` varchar(3) DEFAULT NULL,
  `length_2` varchar(3) DEFAULT NULL,
  `destination_station_code` varchar(3) DEFAULT NULL,
  `length_1` varchar(3) DEFAULT NULL,
  `invoice_payment_currency_code` varchar(3) DEFAULT NULL,
  `weight_code` varchar(2) DEFAULT NULL,
  `delivery_time` varchar(4) DEFAULT NULL,
  `freight_description` varchar(40) DEFAULT NULL,
  `fee_date` varchar(8) DEFAULT NULL,
  `iso_currency_code` varchar(3) DEFAULT NULL,
  `shipper_city` varchar(20) DEFAULT NULL,
  `third_party_customer_number` varchar(11) DEFAULT NULL,
  `trading_partner_id` varchar(12) DEFAULT NULL,
  `chargeable_weight` varchar(9) DEFAULT NULL,
  `bill_to_country_code` varchar(3) DEFAULT NULL,
  `bill_to_address_1` varchar(25) DEFAULT NULL,
  `bill_to_address_2` varchar(25) DEFAULT NULL,
  `shipper_name_extension` varchar(25) DEFAULT NULL,
  `fee_beginning_date` varchar(8) DEFAULT NULL,
  `piece_count_4` varchar(3) DEFAULT NULL,
  `piece_count_3` varchar(3) DEFAULT NULL,
  `receiver_city` varchar(20) DEFAULT NULL,
  `piece_count_2` varchar(3) DEFAULT NULL,
  `piece_count_1` varchar(3) DEFAULT NULL,
  `receiver_country_code` varchar(3) DEFAULT NULL,
  `invoice_total_piece_count` varchar(5) DEFAULT NULL,
  `invoice_total_adjustment` varchar(13) DEFAULT NULL,
  `assessee_country_code` varchar(3) DEFAULT NULL,
  `shipment_unit_of_measurement_weight` varchar(2) DEFAULT NULL,
  `invoice_total_paid_amount` varchar(13) DEFAULT NULL,
  `bill_to_customer_number` varchar(11) DEFAULT NULL,
  `shipper_reference_number` varchar(35) DEFAULT NULL,
  `assessee_address_2` varchar(25) DEFAULT NULL,
  `bill_to_name` varchar(25) DEFAULT NULL,
  `assessee_address_1` varchar(25) DEFAULT NULL,
  `fee_period` varchar(1) DEFAULT NULL,
  `seq_number` int(8) DEFAULT NULL,
  `fee_description` varchar(15) DEFAULT NULL,
  `shipper_country_code` varchar(3) DEFAULT NULL,
  `shipper_number` varchar(11) DEFAULT NULL,
  `shipment_unit_of_measurement_volume` varchar(1) DEFAULT NULL,
  `fee_end_date` varchar(8) DEFAULT NULL,
  `pick_up_location` varchar(2) DEFAULT NULL,
  `item_total_paid_amount` varchar(13) DEFAULT NULL,
  `origin_station_code` varchar(3) DEFAULT NULL,
  `exchange_rate` varchar(11) DEFAULT NULL,
  `dimensional_weight` varchar(9) DEFAULT NULL,
  `bill_to_city` varchar(20) DEFAULT NULL,
  `value_amount` varchar(12) DEFAULT NULL,
  `consol_data_1` varchar(18) DEFAULT NULL,
  `invoice_total_actual_weight` varchar(9) DEFAULT NULL,
  `consol_data_3` varchar(18) DEFAULT NULL,
  `invoice_billing_currency_code` varchar(3) DEFAULT NULL,
  `consol_data_2` varchar(18) DEFAULT NULL,
  `consol_data_5` varchar(18) DEFAULT NULL,
  `consol_data_4` varchar(18) DEFAULT NULL,
  `consol_data_7` varchar(18) DEFAULT NULL,
  `consol_data_6` varchar(18) DEFAULT NULL,
  `receiver_number` varchar(11) DEFAULT NULL,
  `consol_data_8` varchar(18) DEFAULT NULL,
  `fee_text_4` varchar(50) DEFAULT NULL,
  `fee_text_3` varchar(50) DEFAULT NULL,
  `fee_text_2` varchar(50) DEFAULT NULL,
  `pickup_time` varchar(4) DEFAULT NULL,
  `fee_text_1` varchar(50) DEFAULT NULL,
  `fee_text_8` varchar(50) DEFAULT NULL,
  `fee_text_7` varchar(50) DEFAULT NULL,
  `fee_text_6` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `fee_text_5` varchar(50) DEFAULT NULL,
  `item_number` varchar(11) DEFAULT NULL,
  `accessorial_charge_02` varchar(12) DEFAULT NULL,
  `accessorial_charge_01` varchar(12) DEFAULT NULL,
  `net_days` varchar(3) DEFAULT NULL,
  `accessorial_charge_06` varchar(12) DEFAULT NULL,
  `assessee_name` varchar(25) DEFAULT NULL,
  `accessorial_charge_05` varchar(12) DEFAULT NULL,
  `accessorial_charge_04` varchar(12) DEFAULT NULL,
  `accessorial_charge_03` varchar(12) DEFAULT NULL,
  `receiver_state` varchar(2) DEFAULT NULL,
  `receiver_name` varchar(25) DEFAULT NULL,
  `shipment_volume` varchar(7) DEFAULT NULL,
  `bill_to_accounting_control_data` varchar(25) DEFAULT NULL,
  `contact_name` varchar(25) DEFAULT NULL,
  `accessorial_charge_13` varchar(12) DEFAULT NULL,
  `accessorial_charge_12` varchar(12) DEFAULT NULL,
  `accessorial_charge_11` varchar(12) DEFAULT NULL,
  `accessorial_charge_10` varchar(12) DEFAULT NULL,
  `item_open_amount` varchar(12) DEFAULT NULL,
  `accessorial_charge_17` varchar(12) DEFAULT NULL,
  `accessorial_charge_16` varchar(12) DEFAULT NULL,
  `accessorial_charge_15` varchar(12) DEFAULT NULL,
  `invoice_open_amount` varchar(12) DEFAULT NULL,
  `accessorial_charge_14` varchar(12) DEFAULT NULL,
  `accessorial_charge_09` varchar(12) DEFAULT NULL,
  `accessorial_charge_08` varchar(12) DEFAULT NULL,
  `batch_control_num` varchar(9) DEFAULT NULL,
  `accessorial_charge_07` varchar(12) DEFAULT NULL,
  `shipment_accounting_control_data` varchar(25) DEFAULT NULL,
  `delay_reason_code` varchar(3) DEFAULT NULL,
  `accessorial_code_07` varchar(3) DEFAULT NULL,
  `accessorial_code_06` varchar(3) DEFAULT NULL,
  `accessorial_code_05` varchar(3) DEFAULT NULL,
  `accessorial_code_04` varchar(3) DEFAULT NULL,
  `accessorial_code_03` varchar(3) DEFAULT NULL,
  `accessorial_charge_20` varchar(12) DEFAULT NULL,
  `accessorial_code_02` varchar(3) DEFAULT NULL,
  `accessorial_code_01` varchar(3) DEFAULT NULL,
  `accessorial_charge_24` varchar(12) DEFAULT NULL,
  `accessorial_charge_23` varchar(12) DEFAULT NULL,
  `accessorial_description_30` varchar(25) DEFAULT NULL,
  `accessorial_charge_22` varchar(12) DEFAULT NULL,
  `accessorial_charge_21` varchar(12) DEFAULT NULL,
  `package_type` varchar(1) DEFAULT NULL,
  `accessorial_charge_28` varchar(12) DEFAULT NULL,
  `accessorial_charge_27` varchar(12) DEFAULT NULL,
  `item_total_adjustment` varchar(13) DEFAULT NULL,
  `accessorial_code_09` varchar(3) DEFAULT NULL,
  `accessorial_charge_26` varchar(12) DEFAULT NULL,
  `accessorial_code_08` varchar(3) DEFAULT NULL,
  `accessorial_charge_25` varchar(12) DEFAULT NULL,
  `accessorial_charge_19` varchar(12) DEFAULT NULL,
  `accessorial_charge_18` varchar(12) DEFAULT NULL,
  `consol_to_customer_number` varchar(11) DEFAULT NULL,
  `accessorial_code_10` varchar(3) DEFAULT NULL,
  `fee_text_15` varchar(50) DEFAULT NULL,
  `fee_text_14` varchar(50) DEFAULT NULL,
  `fee_text_17` varchar(50) DEFAULT NULL,
  `fee_text_16` varchar(50) DEFAULT NULL,
  `fee_text_11` varchar(50) DEFAULT NULL,
  `fee_text_10` varchar(50) DEFAULT NULL,
  `fee_text_13` varchar(50) DEFAULT NULL,
  `fee_text_12` varchar(50) DEFAULT NULL,
  `accessorial_code_18` varchar(3) DEFAULT NULL,
  `bill_to_zip` varchar(9) DEFAULT NULL,
  `accessorial_code_17` varchar(3) DEFAULT NULL,
  `fee_basis_count` varchar(13) DEFAULT NULL,
  `invoice_total_other_charges` varchar(12) DEFAULT NULL,
  `accessorial_code_16` varchar(3) DEFAULT NULL,
  `accessorial_code_15` varchar(3) DEFAULT NULL,
  `piece_count` varchar(5) DEFAULT NULL,
  `accessorial_code_14` varchar(3) DEFAULT NULL,
  `accessorial_code_13` varchar(3) DEFAULT NULL,
  `accessorial_charge_30` varchar(12) DEFAULT NULL,
  `bill_to_code` varchar(1) DEFAULT NULL,
  `accessorial_code_12` varchar(3) DEFAULT NULL,
  `service_level` varchar(2) DEFAULT NULL,
  `accessorial_code_11` varchar(3) DEFAULT NULL,
  `item_billing_currency_code` varchar(3) DEFAULT NULL,
  `accessorial_code_19` varchar(3) DEFAULT NULL,
  `fee_rate` varchar(6) DEFAULT NULL,
  `invoice_total_freight_charge` varchar(12) DEFAULT NULL,
  `delivery_date` varchar(8) DEFAULT NULL,
  `accessorial_charge_29` varchar(12) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `accessorial_code_21` varchar(3) DEFAULT NULL,
  `accessorial_code_20` varchar(3) DEFAULT NULL,
  `shipper_name` varchar(25) DEFAULT NULL,
  `invoice_type_code` varchar(2) DEFAULT NULL,
  `accessorial_description_17` varchar(25) DEFAULT NULL,
  `accessorial_code_29` varchar(3) DEFAULT NULL,
  `accessorial_description_16` varchar(25) DEFAULT NULL,
  `accessorial_code_28` varchar(3) DEFAULT NULL,
  `accessorial_description_15` varchar(25) DEFAULT NULL,
  `accessorial_code_27` varchar(3) DEFAULT NULL,
  `accessorial_description_14` varchar(25) DEFAULT NULL,
  `accessorial_code_26` varchar(3) DEFAULT NULL,
  `fee_text_9` varchar(50) DEFAULT NULL,
  `accessorial_code_25` varchar(3) DEFAULT NULL,
  `accessorial_code_24` varchar(3) DEFAULT NULL,
  `system_date` varchar(8) DEFAULT NULL,
  `accessorial_description_19` varchar(25) DEFAULT NULL,
  `accessorial_code_23` varchar(3) DEFAULT NULL,
  `fee_accounting_control_data` varchar(25) DEFAULT NULL,
  `accessorial_description_18` varchar(25) DEFAULT NULL,
  `accessorial_code_22` varchar(3) DEFAULT NULL,
  `scale` varchar(7) DEFAULT NULL,
  `payment_terms` varchar(2) DEFAULT NULL,
  `shipment_type_code` varchar(1) DEFAULT NULL,
  `accessorial_description_13` varchar(25) DEFAULT NULL,
  `consol_rate_message_code` varchar(2) DEFAULT NULL,
  `cod_amount` varchar(12) DEFAULT NULL,
  `accessorial_description_12` varchar(25) DEFAULT NULL,
  `accessorial_description_11` varchar(25) DEFAULT NULL,
  `accessorial_description_10` varchar(25) DEFAULT NULL,
  `receiver_zip` varchar(9) DEFAULT NULL,
  `assessee_zip` varchar(9) DEFAULT NULL,
  `rcv_sig` varchar(25) DEFAULT NULL,
  `invoice_num` varchar(13) DEFAULT NULL,
  `dutiable_shipment_code` varchar(1) DEFAULT NULL,
  `shipper_state` varchar(2) DEFAULT NULL,
  `accessorial_code_30` varchar(3) DEFAULT NULL,
  `consol_code` varchar(2) DEFAULT NULL,
  `fee_calculation_code` varchar(2) DEFAULT NULL,
  `accessorial_description_28` varchar(25) DEFAULT NULL,
  `accessorial_description_27` varchar(25) DEFAULT NULL,
  `accessorial_description_26` varchar(25) DEFAULT NULL,
  `accessorial_description_25` varchar(25) DEFAULT NULL,
  `fee_text_19` varchar(50) DEFAULT NULL,
  `fee_text_18` varchar(50) DEFAULT NULL,
  `accessorial_description_29` varchar(25) DEFAULT NULL,
  `accessorial_description_20` varchar(25) DEFAULT NULL,
  `fee_type_code` varchar(3) DEFAULT NULL,
  `accessorial_description_24` varchar(25) DEFAULT NULL,
  `shipper_zip` varchar(9) DEFAULT NULL,
  `accessorial_description_23` varchar(25) DEFAULT NULL,
  `accessorial_description_22` varchar(25) DEFAULT NULL,
  `accessorial_description_21` varchar(25) DEFAULT NULL,
  `invoice_total_chargeable_weight` varchar(9) DEFAULT NULL,
  `actual_weight` varchar(9) DEFAULT NULL,
  `fee_text_20` varchar(50) DEFAULT NULL,
  `assessee_number` varchar(11) DEFAULT NULL,
  `receiver_address_2` varchar(25) DEFAULT NULL,
  `receiver_address_1` varchar(25) DEFAULT NULL,
  `bill_to_name_extension` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_fedex`
--

DROP TABLE IF EXISTS `order_edi_detail_fedex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_fedex` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `type` varchar(1) DEFAULT NULL,
  `dim_unit` varchar(1) DEFAULT NULL,
  `original_city` varchar(30) DEFAULT NULL,
  `addn_hndlg_amt` varchar(16) DEFAULT NULL,
  `customs` varchar(16) DEFAULT NULL,
  `exc_curr` varchar(3) DEFAULT NULL,
  `freight_amt` varchar(16) DEFAULT NULL,
  `curr` varchar(3) DEFAULT NULL,
  `invoice_number` varchar(9) DEFAULT NULL,
  `resi_amt` varchar(16) DEFAULT NULL,
  `non_dup` varchar(1) DEFAULT NULL,
  `on_call_amt` varchar(16) DEFAULT NULL,
  `height` varchar(3) DEFAULT NULL,
  `grd_svc` varchar(3) DEFAULT NULL,
  `grd_po_no` varchar(30) DEFAULT NULL,
  `misc_1_amt` varchar(16) DEFAULT NULL,
  `consolidated_acct` varchar(9) DEFAULT NULL,
  `duty_amt` varchar(16) DEFAULT NULL,
  `master_edi_no` varchar(9) DEFAULT NULL,
  `misc_3_amt` varchar(16) DEFAULT NULL,
  `grd_dept_no` varchar(25) DEFAULT NULL,
  `not_applicable` varchar(1) DEFAULT NULL,
  `dlvry_date` varchar(8) DEFAULT NULL,
  `device` varchar(5) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `earned_disc_amt` varchar(16) DEFAULT NULL,
  `adv_fee_amt` varchar(16) DEFAULT NULL,
  `eu_vat_no` varchar(25) DEFAULT NULL,
  `d_v__amt` varchar(16) DEFAULT NULL,
  `signature` varchar(22) DEFAULT NULL,
  `shipper_company` varchar(30) DEFAULT NULL,
  `settle` varchar(1) DEFAULT NULL,
  `exceptn` varchar(2) DEFAULT NULL,
  `chrg_9` varchar(3) DEFAULT NULL,
  `chrg_7` varchar(3) DEFAULT NULL,
  `chrg_8` varchar(3) DEFAULT NULL,
  `net_chrg` varchar(16) DEFAULT NULL,
  `chrg_5` varchar(3) DEFAULT NULL,
  `invoice_date` varchar(8) DEFAULT NULL,
  `chrg_6` varchar(3) DEFAULT NULL,
  `chrg_3` varchar(3) DEFAULT NULL,
  `chrg_4` varchar(3) DEFAULT NULL,
  `bundle_no` varchar(9) DEFAULT NULL,
  `chrg_1` varchar(3) DEFAULT NULL,
  `divisor` varchar(3) DEFAULT NULL,
  `chrg_2` varchar(3) DEFAULT NULL,
  `us_origin` varchar(3) DEFAULT NULL,
  `inv_charge` varchar(16) DEFAULT NULL,
  `msg_cd` varchar(5) DEFAULT NULL,
  `auto_disc_amt` varchar(16) DEFAULT NULL,
  `multi_wt` varchar(9) DEFAULT NULL,
  `svc` varchar(2) DEFAULT NULL,
  `sign_svc_amt` varchar(16) DEFAULT NULL,
  `st` varchar(2) DEFAULT NULL,
  `recipient_company` varchar(30) DEFAULT NULL,
  `pdue_inv` varchar(9) DEFAULT NULL,
  `bill_wt` varchar(8) DEFAULT NULL,
  `postal3` varchar(10) DEFAULT NULL,
  `das_amt` varchar(16) DEFAULT NULL,
  `postal2` varchar(10) DEFAULT NULL,
  `sat_amt` varchar(16) DEFAULT NULL,
  `shipper_address_2` varchar(30) DEFAULT NULL,
  `shipper_address_1` varchar(30) DEFAULT NULL,
  `shipper_dept` varchar(25) DEFAULT NULL,
  `width` varchar(3) DEFAULT NULL,
  `c_l_cnt` varchar(5) DEFAULT NULL,
  `perf_price_amt` varchar(16) DEFAULT NULL,
  `cod_trkg_no` varchar(12) DEFAULT NULL,
  `gst_amt` varchar(16) DEFAULT NULL,
  `call_tag` varchar(1) DEFAULT NULL,
  `cus_curr` varchar(3) DEFAULT NULL,
  `store_no` varchar(10) DEFAULT NULL,
  `threshold` varchar(16) DEFAULT NULL,
  `orig_recip_adr_1` varchar(30) DEFAULT NULL,
  `orig_recip_adr_2` varchar(30) DEFAULT NULL,
  `cust_inv_no` varchar(25) DEFAULT NULL,
  `svc_area` varchar(2) DEFAULT NULL,
  `dec_value` varchar(16) DEFAULT NULL,
  `shipper_city` varchar(30) DEFAULT NULL,
  `recipient_city` varchar(30) DEFAULT NULL,
  `entry_no` varchar(15) DEFAULT NULL,
  `cod_amt` varchar(16) DEFAULT NULL,
  `vol_disc_amt` varchar(16) DEFAULT NULL,
  `rma_no` varchar(20) DEFAULT NULL,
  `grd_misc_2` varchar(3) DEFAULT NULL,
  `grd_misc_1` varchar(3) DEFAULT NULL,
  `grd_misc_3` varchar(3) DEFAULT NULL,
  `pdue` varchar(1) DEFAULT NULL,
  `fuel_pct` varchar(5) DEFAULT NULL,
  `co_cd` varchar(1) DEFAULT NULL,
  `chrg_21` varchar(3) DEFAULT NULL,
  `chrg_20` varchar(3) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `ipd_adr` varchar(5) DEFAULT NULL,
  `grd_prefix` varchar(8) DEFAULT NULL,
  `misc_2_amt` varchar(16) DEFAULT NULL,
  `final` varchar(2) DEFAULT NULL,
  `postal` varchar(10) DEFAULT NULL,
  `shipper_name` varchar(30) DEFAULT NULL,
  `region` varchar(2) DEFAULT NULL,
  `recipient_name` varchar(30) DEFAULT NULL,
  `bill_to_account` varchar(9) DEFAULT NULL,
  `scale` varchar(7) DEFAULT NULL,
  `device_no` varchar(7) DEFAULT NULL,
  `attempt_time` varchar(4) DEFAULT NULL,
  `pkg` varchar(2) DEFAULT NULL,
  `exchg_rate` varchar(19) DEFAULT NULL,
  `attempt_date` varchar(8) DEFAULT NULL,
  `orig_wt` varchar(8) DEFAULT NULL,
  `wt_unit` varchar(1) DEFAULT NULL,
  `chrg_13` varchar(3) DEFAULT NULL,
  `chrg_12` varchar(3) DEFAULT NULL,
  `chrg_11` varchar(3) DEFAULT NULL,
  `payor` varchar(1) DEFAULT NULL,
  `chrg_10` varchar(3) DEFAULT NULL,
  `cntry` varchar(2) DEFAULT NULL,
  `fuel_amt` varchar(16) DEFAULT NULL,
  `tracking_number` varchar(12) DEFAULT NULL,
  `chrg_19` varchar(3) DEFAULT NULL,
  `chrg_18` varchar(3) DEFAULT NULL,
  `chrg_17` varchar(3) DEFAULT NULL,
  `chrg_16` varchar(3) DEFAULT NULL,
  `chrg_15` varchar(3) DEFAULT NULL,
  `chrg_14` varchar(3) DEFAULT NULL,
  `pcs` varchar(5) DEFAULT NULL,
  `adr_corr_amt` varchar(16) DEFAULT NULL,
  `length` varchar(3) DEFAULT NULL,
  `trans_cnt` varchar(5) DEFAULT NULL,
  `svc_pct` varchar(3) DEFAULT NULL,
  `ref_1` varchar(24) DEFAULT NULL,
  `recipient_address_2` varchar(30) DEFAULT NULL,
  `st2` varchar(2) DEFAULT NULL,
  `recipient_address_1` varchar(30) DEFAULT NULL,
  `ref_3` varchar(24) DEFAULT NULL,
  `ref_2` varchar(24) DEFAULT NULL,
  `st3` varchar(2) DEFAULT NULL,
  `eu_bd` varchar(2) DEFAULT NULL,
  `hndlg` varchar(1) DEFAULT NULL,
  `cntry1` varchar(2) DEFAULT NULL,
  `cntry2` varchar(2) DEFAULT NULL,
  `time` varchar(4) DEFAULT NULL,
  `orig_vat_amt` varchar(16) DEFAULT NULL,
  `rebill` varchar(1) DEFAULT NULL,
  `ship_date` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_mfw`
--

DROP TABLE IF EXISTS `order_edi_detail_mfw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_mfw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `accl2` varchar(50) DEFAULT NULL,
  `cartons20` varchar(50) DEFAULT NULL,
  `accl1` varchar(50) DEFAULT NULL,
  `audit_code` varchar(50) DEFAULT NULL,
  `adj9_amt` varchar(50) DEFAULT NULL,
  `accl4` varchar(50) DEFAULT NULL,
  `accl3` varchar(50) DEFAULT NULL,
  `con_city` varchar(50) DEFAULT NULL,
  `accl6` varchar(50) DEFAULT NULL,
  `accl5` varchar(50) DEFAULT NULL,
  `division` varchar(50) DEFAULT NULL,
  `cartons4` varchar(50) DEFAULT NULL,
  `weight13` varchar(50) DEFAULT NULL,
  `cartons5` varchar(50) DEFAULT NULL,
  `weight14` varchar(50) DEFAULT NULL,
  `cartons6` varchar(50) DEFAULT NULL,
  `weight11` varchar(50) DEFAULT NULL,
  `cartons7` varchar(50) DEFAULT NULL,
  `weight12` varchar(50) DEFAULT NULL,
  `sh_zip` varchar(50) DEFAULT NULL,
  `cartons8` varchar(50) DEFAULT NULL,
  `weight17` varchar(50) DEFAULT NULL,
  `cartons9` varchar(50) DEFAULT NULL,
  `weight18` varchar(50) DEFAULT NULL,
  `weight15` varchar(50) DEFAULT NULL,
  `weight16` varchar(50) DEFAULT NULL,
  `del_date` varchar(50) DEFAULT NULL,
  `weight10` varchar(50) DEFAULT NULL,
  `invoice_number` varchar(50) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `master_pro` varchar(50) DEFAULT NULL,
  `weight19` varchar(50) DEFAULT NULL,
  `cartons1` varchar(50) DEFAULT NULL,
  `cartons2` varchar(50) DEFAULT NULL,
  `cartons3` varchar(50) DEFAULT NULL,
  `adj7_amt` varchar(50) DEFAULT NULL,
  `con_state` varchar(50) DEFAULT NULL,
  `weight20` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `adj4` varchar(50) DEFAULT NULL,
  `class19` varchar(50) DEFAULT NULL,
  `adj3` varchar(50) DEFAULT NULL,
  `adj6` varchar(50) DEFAULT NULL,
  `adj5` varchar(50) DEFAULT NULL,
  `adj2` varchar(50) DEFAULT NULL,
  `scac` varchar(50) DEFAULT NULL,
  `adj1` varchar(50) DEFAULT NULL,
  `con_zip` varchar(50) DEFAULT NULL,
  `adj5_amt` varchar(50) DEFAULT NULL,
  `paid_amt` varchar(50) DEFAULT NULL,
  `invoice_date` varchar(50) DEFAULT NULL,
  `accl2_amt` varchar(50) DEFAULT NULL,
  `class20` varchar(50) DEFAULT NULL,
  `sh_city` varchar(50) DEFAULT NULL,
  `adj8` varchar(50) DEFAULT NULL,
  `adj7` varchar(50) DEFAULT NULL,
  `seq` varchar(50) DEFAULT NULL,
  `recd_date` varchar(50) DEFAULT NULL,
  `adj9` varchar(50) DEFAULT NULL,
  `accl6_amt` varchar(50) DEFAULT NULL,
  `adj3_amt` varchar(50) DEFAULT NULL,
  `sh_state` varchar(50) DEFAULT NULL,
  `adj1_amt` varchar(50) DEFAULT NULL,
  `accl4_amt` varchar(50) DEFAULT NULL,
  `class10` varchar(50) DEFAULT NULL,
  `class12` varchar(50) DEFAULT NULL,
  `class11` varchar(50) DEFAULT NULL,
  `class14` varchar(50) DEFAULT NULL,
  `class13` varchar(50) DEFAULT NULL,
  `class16` varchar(50) DEFAULT NULL,
  `class15` varchar(50) DEFAULT NULL,
  `class18` varchar(50) DEFAULT NULL,
  `class17` varchar(50) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `adj10` varchar(50) DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `weight9` varchar(50) DEFAULT NULL,
  `weight8` varchar(50) DEFAULT NULL,
  `adj8_amt` varchar(50) DEFAULT NULL,
  `batch` varchar(50) DEFAULT NULL,
  `adj10_amt` varchar(50) DEFAULT NULL,
  `class7` varchar(50) DEFAULT NULL,
  `adj6_amt` varchar(50) DEFAULT NULL,
  `class6` varchar(50) DEFAULT NULL,
  `class5` varchar(50) DEFAULT NULL,
  `class4` varchar(50) DEFAULT NULL,
  `class3` varchar(50) DEFAULT NULL,
  `class2` varchar(50) DEFAULT NULL,
  `class1` varchar(50) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `billed_amt` varchar(50) DEFAULT NULL,
  `accl1_amt` varchar(50) DEFAULT NULL,
  `bol` varchar(50) DEFAULT NULL,
  `custno` varchar(50) DEFAULT NULL,
  `accl3_amt` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `textbox51` varchar(50) DEFAULT NULL,
  `textbox52` varchar(50) DEFAULT NULL,
  `class9` varchar(50) DEFAULT NULL,
  `textbox55` varchar(50) DEFAULT NULL,
  `class8` varchar(50) DEFAULT NULL,
  `textbox56` varchar(50) DEFAULT NULL,
  `textbox47` varchar(50) DEFAULT NULL,
  `textbox48` varchar(50) DEFAULT NULL,
  `adj4_amt` varchar(50) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `cartons10` varchar(50) DEFAULT NULL,
  `cartons12` varchar(50) DEFAULT NULL,
  `adj2_amt` varchar(50) DEFAULT NULL,
  `cartons11` varchar(50) DEFAULT NULL,
  `cartons14` varchar(50) DEFAULT NULL,
  `cartons13` varchar(50) DEFAULT NULL,
  `weight7` varchar(50) DEFAULT NULL,
  `cartons16` varchar(50) DEFAULT NULL,
  `textbox60` varchar(50) DEFAULT NULL,
  `weight6` varchar(50) DEFAULT NULL,
  `cartons15` varchar(50) DEFAULT NULL,
  `weight5` varchar(50) DEFAULT NULL,
  `cartons18` varchar(50) DEFAULT NULL,
  `weight4` varchar(50) DEFAULT NULL,
  `cartons17` varchar(50) DEFAULT NULL,
  `weight3` varchar(50) DEFAULT NULL,
  `weight2` varchar(50) DEFAULT NULL,
  `cartons19` varchar(50) DEFAULT NULL,
  `weight1` varchar(50) DEFAULT NULL,
  `textbox59` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `return_code` varchar(50) DEFAULT NULL,
  `accl5_amt` varchar(50) DEFAULT NULL,
  `ship_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1872 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_puro`
--

DROP TABLE IF EXISTS `order_edi_detail_puro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_puro` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `vip_dtl_eco` varchar(7) DEFAULT NULL,
  `vip_dtl_from_city` varchar(20) DEFAULT NULL,
  `vip_dtl_beyond` varchar(7) DEFAULT NULL,
  `vip_dtl_gst` varchar(7) DEFAULT NULL,
  `vip_dtl_consol_pin` varchar(12) DEFAULT NULL,
  `vip_dtl_fuel_surcharge` varchar(7) DEFAULT NULL,
  `vip_dtl_product` varchar(4) DEFAULT NULL,
  `vip_dtl_wt` varchar(5) DEFAULT NULL,
  `vip_dtl_to_city` varchar(20) DEFAULT NULL,
  `vip_dtl_receiver_addr` varchar(30) DEFAULT NULL,
  `vip_dtl_type` varchar(4) DEFAULT NULL,
  `vip_dtl_insurance` varchar(7) DEFAULT NULL,
  `vip_dtl_weekender` varchar(7) DEFAULT NULL,
  `vip_dtl_sender_addr` varchar(30) DEFAULT NULL,
  `vip_dtl_parcel_length` varchar(4) DEFAULT NULL,
  `vip_dtl_consol_ship` varchar(5) DEFAULT NULL,
  `vip_dtl_consol_prov` varchar(4) DEFAULT NULL,
  `vip_dtl_rec_type` varchar(1) DEFAULT NULL,
  `vip_dtl_pieces` varchar(3) DEFAULT NULL,
  `vip_dtl_consol_city` varchar(12) DEFAULT NULL,
  `vip_dtl_parcel_height` varchar(4) DEFAULT NULL,
  `vip_dtl_dangr` varchar(7) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `vip_dtl_reference` varchar(15) DEFAULT NULL,
  `vip_dtl_bl_no` varchar(12) DEFAULT NULL,
  `vip_dtl_to_prov` varchar(4) DEFAULT NULL,
  `vip_dtl_parcel_width` varchar(4) DEFAULT NULL,
  `vip_dtl_from_prov` varchar(4) DEFAULT NULL,
  `vip_dtl_cos_surch` varchar(7) DEFAULT NULL,
  `vip_dtl_non_pack_srch` varchar(7) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `vip_dtl_serv_type` varchar(1) DEFAULT NULL,
  `vip_dtl_serv_date` varchar(6) DEFAULT NULL,
  `vip_dtl_weight_8` varchar(8) DEFAULT NULL,
  `vip_dtl_pst` varchar(7) DEFAULT NULL,
  `vip_dtl_account_no_chg` varchar(7) DEFAULT NULL,
  `vip_dtl_sender_name` varchar(30) DEFAULT NULL,
  `vip_dtl_amt` varchar(7) DEFAULT NULL,
  `vip_dtl_pc_flag` varchar(1) DEFAULT NULL,
  `vip_dtl_pod` varchar(7) DEFAULT NULL,
  `vip_dtl_purotherm` varchar(7) DEFAULT NULL,
  `vip_dtl_sender_unit` varchar(4) DEFAULT NULL,
  `vip_dtl_collect` varchar(7) DEFAULT NULL,
  `vip_dtl_incomplete_address` varchar(7) DEFAULT NULL,
  `vip_dtl_receiver_unit` varchar(4) DEFAULT NULL,
  `vip_dtl_trans_code` varchar(1) DEFAULT NULL,
  `vip_dtl_900am` varchar(7) DEFAULT NULL,
  `vip_dtl_quicks` varchar(7) DEFAULT NULL,
  `vip_dtl_from_post_code` varchar(6) DEFAULT NULL,
  `vip_dtl_receiver_name` varchar(30) DEFAULT NULL,
  `vip_dtl_aod` varchar(7) DEFAULT NULL,
  `vip_dtl_wt_units` varchar(1) DEFAULT NULL,
  `vip_dtl_to_post_code` varchar(6) DEFAULT NULL,
  `vip_dtl_non_pack_pieces` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_puro_c`
--

DROP TABLE IF EXISTS `order_edi_detail_puro_c`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_puro_c` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `k_un_ag` varchar(10) DEFAULT NULL,
  `hst` varchar(8) DEFAULT NULL,
  `gst` varchar(8) DEFAULT NULL,
  `c_ty` varchar(40) DEFAULT NULL,
  `pst` varchar(8) DEFAULT NULL,
  `c_cd` varchar(3) DEFAULT NULL,
  `qst` varchar(8) DEFAULT NULL,
  `r_ec_ty_p` varchar(1) DEFAULT NULL,
  `a_tt_nn_m` varchar(40) DEFAULT NULL,
  `con_num` varchar(10) DEFAULT NULL,
  `p_v` varchar(3) DEFAULT NULL,
  `t_piece` varchar(6) DEFAULT NULL,
  `p_sc_d` varchar(10) DEFAULT NULL,
  `tvl` varchar(10) DEFAULT NULL,
  `netvl_ac` varchar(10) DEFAULT NULL,
  `c_nm` varchar(40) DEFAULT NULL,
  `t_sh` varchar(6) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `n_et_vl` varchar(10) DEFAULT NULL,
  `netvl_sh` varchar(10) DEFAULT NULL,
  `t_fuel` varchar(8) DEFAULT NULL,
  `a_dd_r` varchar(90) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_puro_d`
--

DROP TABLE IF EXISTS `order_edi_detail_puro_d`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_puro_d` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `a_matnr` varchar(18) DEFAULT NULL,
  `total_value` varchar(10) DEFAULT NULL,
  `seq_number` int(8) DEFAULT NULL,
  `d_gewei` varchar(3) DEFAULT NULL,
  `a_weight` varchar(15) DEFAULT NULL,
  `g_st` varchar(8) DEFAULT NULL,
  `py_pty` varchar(2) DEFAULT NULL,
  `s_attnnm` varchar(40) DEFAULT NULL,
  `d_coun` varchar(3) DEFAULT NULL,
  `r_ef1` varchar(35) DEFAULT NULL,
  `s_city` varchar(40) DEFAULT NULL,
  `l_it_em` varchar(6) DEFAULT NULL,
  `b_st_kd` varchar(35) DEFAULT NULL,
  `base_value` varchar(10) DEFAULT NULL,
  `r_ef5` varchar(35) DEFAULT NULL,
  `d_name` varchar(40) DEFAULT NULL,
  `r_ec_ty_p` varchar(1) DEFAULT NULL,
  `r_ef4` varchar(35) DEFAULT NULL,
  `d_maktg` varchar(40) DEFAULT NULL,
  `r_ef3` varchar(35) DEFAULT NULL,
  `r_ef2` varchar(35) DEFAULT NULL,
  `s_addr` varchar(90) DEFAULT NULL,
  `s_prov` varchar(3) DEFAULT NULL,
  `q_st` varchar(8) DEFAULT NULL,
  `k_wm_en_g` varchar(6) DEFAULT NULL,
  `s_hi_pp_in_g` varchar(40) DEFAULT NULL,
  `k_dm_at` varchar(35) DEFAULT NULL,
  `d_addr` varchar(90) DEFAULT NULL,
  `b_mf_la_g` varchar(1) DEFAULT NULL,
  `d_attnnm` varchar(40) DEFAULT NULL,
  `p_st` varchar(8) DEFAULT NULL,
  `d_pcode` varchar(10) DEFAULT NULL,
  `h_st` varchar(8) DEFAULT NULL,
  `a_maktg` varchar(40) DEFAULT NULL,
  `a_gewei` varchar(3) DEFAULT NULL,
  `d_city` varchar(40) DEFAULT NULL,
  `b_sa_rk` varchar(4) DEFAULT NULL,
  `d_matnr` varchar(18) DEFAULT NULL,
  `s_pcode` varchar(10) DEFAULT NULL,
  `rt_flg` varchar(1) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `d_ntgew` varchar(15) DEFAULT NULL,
  `s_coun` varchar(3) DEFAULT NULL,
  `d_prov` varchar(3) DEFAULT NULL,
  `s_name` varchar(40) DEFAULT NULL,
  `a_ud_at` varchar(8) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_puro_e`
--

DROP TABLE IF EXISTS `order_edi_detail_puro_e`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_puro_e` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `a_wtflag` varchar(1) DEFAULT NULL,
  `d_width` varchar(6) DEFAULT NULL,
  `d_gewei` varchar(3) DEFAULT NULL,
  `d_duom` varchar(2) DEFAULT NULL,
  `a_weight` varchar(15) DEFAULT NULL,
  `d_weight` varchar(15) DEFAULT NULL,
  `a_gewei` varchar(3) DEFAULT NULL,
  `d_length` varchar(6) DEFAULT NULL,
  `a_length` varchar(6) DEFAULT NULL,
  `r_ec_ty_p` varchar(1) DEFAULT NULL,
  `bstkd_e` varchar(35) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `sp_flag` varchar(1) DEFAULT NULL,
  `d_height` varchar(6) DEFAULT NULL,
  `a_duom` varchar(2) DEFAULT NULL,
  `k_dm_at` varchar(35) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `a_height` varchar(6) DEFAULT NULL,
  `a_width` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_puro_f`
--

DROP TABLE IF EXISTS `order_edi_detail_puro_f`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_puro_f` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `a_sur` varchar(20) DEFAULT NULL,
  `seq_number` int(8) DEFAULT NULL,
  `s_ur_vl` varchar(8) DEFAULT NULL,
  `r_ec_ty_p` varchar(1) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `s_ur_cd` varchar(4) DEFAULT NULL,
  `d_sur` varchar(20) DEFAULT NULL,
  `s_ur_dp` varchar(40) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_sfw`
--

DROP TABLE IF EXISTS `order_edi_detail_sfw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_sfw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `accl2` varchar(50) DEFAULT NULL,
  `cartons20` varchar(50) DEFAULT NULL,
  `accl1` varchar(50) DEFAULT NULL,
  `audit_code` varchar(50) DEFAULT NULL,
  `accl8` varchar(50) DEFAULT NULL,
  `accl7` varchar(50) DEFAULT NULL,
  `adj9_amt` varchar(50) DEFAULT NULL,
  `accl9` varchar(50) DEFAULT NULL,
  `accl4` varchar(50) DEFAULT NULL,
  `accl3` varchar(50) DEFAULT NULL,
  `con_city` varchar(50) DEFAULT NULL,
  `accl6` varchar(50) DEFAULT NULL,
  `accl5` varchar(50) DEFAULT NULL,
  `division` varchar(50) DEFAULT NULL,
  `cartons4` varchar(50) DEFAULT NULL,
  `weight13` varchar(50) DEFAULT NULL,
  `cartons5` varchar(50) DEFAULT NULL,
  `weight14` varchar(50) DEFAULT NULL,
  `cartons6` varchar(50) DEFAULT NULL,
  `weight11` varchar(50) DEFAULT NULL,
  `cartons7` varchar(50) DEFAULT NULL,
  `weight12` varchar(50) DEFAULT NULL,
  `sh_zip` varchar(50) DEFAULT NULL,
  `cartons8` varchar(50) DEFAULT NULL,
  `weight17` varchar(50) DEFAULT NULL,
  `cartons9` varchar(50) DEFAULT NULL,
  `weight18` varchar(50) DEFAULT NULL,
  `weight15` varchar(50) DEFAULT NULL,
  `weight16` varchar(50) DEFAULT NULL,
  `del_date` varchar(50) DEFAULT NULL,
  `weight10` varchar(50) DEFAULT NULL,
  `invoice_number` varchar(50) DEFAULT NULL,
  `accl7_amt` varchar(50) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `master_pro` varchar(50) DEFAULT NULL,
  `weight19` varchar(50) DEFAULT NULL,
  `cartons1` varchar(50) DEFAULT NULL,
  `cartons2` varchar(50) DEFAULT NULL,
  `cartons3` varchar(50) DEFAULT NULL,
  `adj7_amt` varchar(50) DEFAULT NULL,
  `accl9_amt` varchar(50) DEFAULT NULL,
  `con_state` varchar(50) DEFAULT NULL,
  `weight20` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `adj4` varchar(50) DEFAULT NULL,
  `class19` varchar(50) DEFAULT NULL,
  `adj3` varchar(50) DEFAULT NULL,
  `adj6` varchar(50) DEFAULT NULL,
  `adj5` varchar(50) DEFAULT NULL,
  `adj2` varchar(50) DEFAULT NULL,
  `scac` varchar(50) DEFAULT NULL,
  `adj1` varchar(50) DEFAULT NULL,
  `con_zip` varchar(50) DEFAULT NULL,
  `accl10` varchar(50) DEFAULT NULL,
  `adj5_amt` varchar(50) DEFAULT NULL,
  `paid_amt` varchar(50) DEFAULT NULL,
  `invoice_date` varchar(50) DEFAULT NULL,
  `accl2_amt` varchar(50) DEFAULT NULL,
  `class20` varchar(50) DEFAULT NULL,
  `sh_city` varchar(50) DEFAULT NULL,
  `adj8` varchar(50) DEFAULT NULL,
  `adj7` varchar(50) DEFAULT NULL,
  `seq` varchar(50) DEFAULT NULL,
  `recd_date` varchar(50) DEFAULT NULL,
  `adj9` varchar(50) DEFAULT NULL,
  `accl6_amt` varchar(50) DEFAULT NULL,
  `adj3_amt` varchar(50) DEFAULT NULL,
  `sh_state` varchar(50) DEFAULT NULL,
  `adj1_amt` varchar(50) DEFAULT NULL,
  `accl4_amt` varchar(50) DEFAULT NULL,
  `class10` varchar(50) DEFAULT NULL,
  `class12` varchar(50) DEFAULT NULL,
  `class11` varchar(50) DEFAULT NULL,
  `class14` varchar(50) DEFAULT NULL,
  `class13` varchar(50) DEFAULT NULL,
  `class16` varchar(50) DEFAULT NULL,
  `class15` varchar(50) DEFAULT NULL,
  `class18` varchar(50) DEFAULT NULL,
  `class17` varchar(50) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `adj10` varchar(50) DEFAULT NULL,
  `accl8_amt` varchar(50) DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `weight9` varchar(50) DEFAULT NULL,
  `weight8` varchar(50) DEFAULT NULL,
  `accl10_amt` varchar(50) DEFAULT NULL,
  `adj8_amt` varchar(50) DEFAULT NULL,
  `batch` varchar(50) DEFAULT NULL,
  `adj10_amt` varchar(50) DEFAULT NULL,
  `class7` varchar(50) DEFAULT NULL,
  `adj6_amt` varchar(50) DEFAULT NULL,
  `class6` varchar(50) DEFAULT NULL,
  `class5` varchar(50) DEFAULT NULL,
  `class4` varchar(50) DEFAULT NULL,
  `class3` varchar(50) DEFAULT NULL,
  `class2` varchar(50) DEFAULT NULL,
  `class1` varchar(50) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `billed_amt` varchar(50) DEFAULT NULL,
  `accl1_amt` varchar(50) DEFAULT NULL,
  `payment_comment` varchar(250) DEFAULT NULL,
  `bol` varchar(50) DEFAULT NULL,
  `custno` varchar(50) DEFAULT NULL,
  `accl3_amt` varchar(50) DEFAULT NULL,
  `class9` varchar(50) DEFAULT NULL,
  `class8` varchar(50) DEFAULT NULL,
  `adj4_amt` varchar(50) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `cartons10` varchar(50) DEFAULT NULL,
  `cartons12` varchar(50) DEFAULT NULL,
  `adj2_amt` varchar(50) DEFAULT NULL,
  `cartons11` varchar(50) DEFAULT NULL,
  `cartons14` varchar(50) DEFAULT NULL,
  `cartons13` varchar(50) DEFAULT NULL,
  `weight7` varchar(50) DEFAULT NULL,
  `cartons16` varchar(50) DEFAULT NULL,
  `weight6` varchar(50) DEFAULT NULL,
  `cartons15` varchar(50) DEFAULT NULL,
  `weight5` varchar(50) DEFAULT NULL,
  `cartons18` varchar(50) DEFAULT NULL,
  `weight4` varchar(50) DEFAULT NULL,
  `cartons17` varchar(50) DEFAULT NULL,
  `weight3` varchar(50) DEFAULT NULL,
  `weight2` varchar(50) DEFAULT NULL,
  `cartons19` varchar(50) DEFAULT NULL,
  `weight1` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `return_code` varchar(50) DEFAULT NULL,
  `accl5_amt` varchar(50) DEFAULT NULL,
  `ship_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56588 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_tfc`
--

DROP TABLE IF EXISTS `order_edi_detail_tfc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_tfc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_number` int(8) DEFAULT NULL,
  `accl2` varchar(50) DEFAULT NULL,
  `cartons20` varchar(50) DEFAULT NULL,
  `accl1` varchar(50) DEFAULT NULL,
  `audit_code` varchar(50) DEFAULT NULL,
  `accl8` varchar(50) DEFAULT NULL,
  `accl7` varchar(50) DEFAULT NULL,
  `adj9_amt` varchar(50) DEFAULT NULL,
  `accl9` varchar(50) DEFAULT NULL,
  `accl4` varchar(50) DEFAULT NULL,
  `accl3` varchar(50) DEFAULT NULL,
  `con_city` varchar(50) DEFAULT NULL,
  `accl6` varchar(50) DEFAULT NULL,
  `accl5` varchar(50) DEFAULT NULL,
  `division` varchar(50) DEFAULT NULL,
  `cartons4` varchar(50) DEFAULT NULL,
  `weight13` varchar(50) DEFAULT NULL,
  `cartons5` varchar(50) DEFAULT NULL,
  `weight14` varchar(50) DEFAULT NULL,
  `cartons6` varchar(50) DEFAULT NULL,
  `weight11` varchar(50) DEFAULT NULL,
  `cartons7` varchar(50) DEFAULT NULL,
  `weight12` varchar(50) DEFAULT NULL,
  `sh_zip` varchar(50) DEFAULT NULL,
  `cartons8` varchar(50) DEFAULT NULL,
  `weight17` varchar(50) DEFAULT NULL,
  `cartons9` varchar(50) DEFAULT NULL,
  `weight18` varchar(50) DEFAULT NULL,
  `weight15` varchar(50) DEFAULT NULL,
  `weight16` varchar(50) DEFAULT NULL,
  `del_date` varchar(50) DEFAULT NULL,
  `weight10` varchar(50) DEFAULT NULL,
  `invoice_number` varchar(50) DEFAULT NULL,
  `accl7_amt` varchar(50) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `master_pro` varchar(50) DEFAULT NULL,
  `weight19` varchar(50) DEFAULT NULL,
  `cartons1` varchar(50) DEFAULT NULL,
  `cartons2` varchar(50) DEFAULT NULL,
  `cartons3` varchar(50) DEFAULT NULL,
  `adj7_amt` varchar(50) DEFAULT NULL,
  `accl9_amt` varchar(50) DEFAULT NULL,
  `con_state` varchar(50) DEFAULT NULL,
  `weight20` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `adj4` varchar(50) DEFAULT NULL,
  `class19` varchar(50) DEFAULT NULL,
  `adj3` varchar(50) DEFAULT NULL,
  `adj6` varchar(50) DEFAULT NULL,
  `adj5` varchar(50) DEFAULT NULL,
  `adj2` varchar(50) DEFAULT NULL,
  `scac` varchar(50) DEFAULT NULL,
  `adj1` varchar(50) DEFAULT NULL,
  `con_zip` varchar(50) DEFAULT NULL,
  `accl10` varchar(50) DEFAULT NULL,
  `adj5_amt` varchar(50) DEFAULT NULL,
  `paid_amt` varchar(50) DEFAULT NULL,
  `invoice_date` varchar(50) DEFAULT NULL,
  `accl2_amt` varchar(50) DEFAULT NULL,
  `class20` varchar(50) DEFAULT NULL,
  `sh_city` varchar(50) DEFAULT NULL,
  `adj8` varchar(50) DEFAULT NULL,
  `adj7` varchar(50) DEFAULT NULL,
  `seq` varchar(50) DEFAULT NULL,
  `recd_date` varchar(50) DEFAULT NULL,
  `adj9` varchar(50) DEFAULT NULL,
  `accl6_amt` varchar(50) DEFAULT NULL,
  `adj3_amt` varchar(50) DEFAULT NULL,
  `sh_state` varchar(50) DEFAULT NULL,
  `adj1_amt` varchar(50) DEFAULT NULL,
  `accl4_amt` varchar(50) DEFAULT NULL,
  `class10` varchar(50) DEFAULT NULL,
  `class12` varchar(50) DEFAULT NULL,
  `class11` varchar(50) DEFAULT NULL,
  `class14` varchar(50) DEFAULT NULL,
  `class13` varchar(50) DEFAULT NULL,
  `class16` varchar(50) DEFAULT NULL,
  `class15` varchar(50) DEFAULT NULL,
  `class18` varchar(50) DEFAULT NULL,
  `class17` varchar(50) DEFAULT NULL,
  `shipper` varchar(50) DEFAULT NULL,
  `adj10` varchar(50) DEFAULT NULL,
  `accl8_amt` varchar(50) DEFAULT NULL,
  `mode` varchar(50) DEFAULT NULL,
  `weight9` varchar(50) DEFAULT NULL,
  `weight8` varchar(50) DEFAULT NULL,
  `accl10_amt` varchar(50) DEFAULT NULL,
  `adj8_amt` varchar(50) DEFAULT NULL,
  `batch` varchar(50) DEFAULT NULL,
  `adj10_amt` varchar(50) DEFAULT NULL,
  `class7` varchar(50) DEFAULT NULL,
  `adj6_amt` varchar(50) DEFAULT NULL,
  `class6` varchar(50) DEFAULT NULL,
  `class5` varchar(50) DEFAULT NULL,
  `class4` varchar(50) DEFAULT NULL,
  `class3` varchar(50) DEFAULT NULL,
  `class2` varchar(50) DEFAULT NULL,
  `class1` varchar(50) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `billed_amt` varchar(50) DEFAULT NULL,
  `accl1_amt` varchar(50) DEFAULT NULL,
  `bol` varchar(50) DEFAULT NULL,
  `custno` varchar(50) DEFAULT NULL,
  `accl3_amt` varchar(50) DEFAULT NULL,
  `class9` varchar(50) DEFAULT NULL,
  `class8` varchar(50) DEFAULT NULL,
  `adj4_amt` varchar(50) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `cartons10` varchar(50) DEFAULT NULL,
  `cartons12` varchar(50) DEFAULT NULL,
  `adj2_amt` varchar(50) DEFAULT NULL,
  `cartons11` varchar(50) DEFAULT NULL,
  `cartons14` varchar(50) DEFAULT NULL,
  `cartons13` varchar(50) DEFAULT NULL,
  `weight7` varchar(50) DEFAULT NULL,
  `cartons16` varchar(50) DEFAULT NULL,
  `weight6` varchar(50) DEFAULT NULL,
  `cartons15` varchar(50) DEFAULT NULL,
  `weight5` varchar(50) DEFAULT NULL,
  `cartons18` varchar(50) DEFAULT NULL,
  `weight4` varchar(50) DEFAULT NULL,
  `cartons17` varchar(50) DEFAULT NULL,
  `weight3` varchar(50) DEFAULT NULL,
  `weight2` varchar(50) DEFAULT NULL,
  `cartons19` varchar(50) DEFAULT NULL,
  `weight1` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `return_code` varchar(50) DEFAULT NULL,
  `accl5_amt` varchar(50) DEFAULT NULL,
  `ship_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30950 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_detail_ups`
--

DROP TABLE IF EXISTS `order_edi_detail_ups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_detail_ups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `oversize_quantity` varchar(7) DEFAULT NULL,
  `third_party_addr2` varchar(50) DEFAULT NULL,
  `account_tax_id` varchar(25) DEFAULT NULL,
  `tax_var_amount` varchar(17) DEFAULT NULL,
  `misc_ln8` varchar(50) DEFAULT NULL,
  `doc_number` varchar(50) DEFAULT NULL,
  `misc_ln7` varchar(50) DEFAULT NULL,
  `bill_option_code` varchar(3) DEFAULT NULL,
  `misc_ln5` varchar(50) DEFAULT NULL,
  `sold_to_name` varchar(50) DEFAULT NULL,
  `ship_value_amount` varchar(17) DEFAULT NULL,
  `misc_ln9` varchar(50) DEFAULT NULL,
  `place_hold38` varchar(1) DEFAULT NULL,
  `place_hold39` varchar(1) DEFAULT NULL,
  `gst_amount` varchar(17) DEFAULT NULL,
  `po2` varchar(20) DEFAULT NULL,
  `po1` varchar(20) DEFAULT NULL,
  `po4` varchar(20) DEFAULT NULL,
  `place_hold34` varchar(1) DEFAULT NULL,
  `po3` varchar(20) DEFAULT NULL,
  `place_hold35` varchar(1) DEFAULT NULL,
  `po6` varchar(20) DEFAULT NULL,
  `place_hold36` varchar(1) DEFAULT NULL,
  `po5` varchar(20) DEFAULT NULL,
  `place_hold37` varchar(1) DEFAULT NULL,
  `ship_dlvry_date` varchar(50) DEFAULT NULL,
  `place_hold30` varchar(1) DEFAULT NULL,
  `po8` varchar(20) DEFAULT NULL,
  `place_hold31` varchar(1) DEFAULT NULL,
  `po7` varchar(20) DEFAULT NULL,
  `receiver_country` varchar(2) DEFAULT NULL,
  `sold_to_postal` varchar(12) DEFAULT NULL,
  `misc_addr1_postal` varchar(12) DEFAULT NULL,
  `job_number` varchar(50) DEFAULT NULL,
  `po9` varchar(20) DEFAULT NULL,
  `receiver_addr1` varchar(50) DEFAULT NULL,
  `receiver_addr2` varchar(50) DEFAULT NULL,
  `inv_level_charge` varchar(17) DEFAULT NULL,
  `third_party_addr1` varchar(50) DEFAULT NULL,
  `tariff_treat_number` varchar(11) DEFAULT NULL,
  `charge_cat_code` varchar(3) DEFAULT NULL,
  `misc_addr2_city` varchar(50) DEFAULT NULL,
  `misc_addr2_state` varchar(30) DEFAULT NULL,
  `version` varchar(3) DEFAULT NULL,
  `place_hold49` varchar(1) DEFAULT NULL,
  `sima_access` varchar(17) DEFAULT NULL,
  `excise_tax_amount` varchar(17) DEFAULT NULL,
  `misc_ln4` varchar(50) DEFAULT NULL,
  `place_hold45` varchar(1) DEFAULT NULL,
  `misc_addr2_country` varchar(2) DEFAULT NULL,
  `tax_type` varchar(2) DEFAULT NULL,
  `misc_ln3` varchar(50) DEFAULT NULL,
  `place_hold46` varchar(1) DEFAULT NULL,
  `misc_ln2` varchar(50) DEFAULT NULL,
  `place_hold47` varchar(1) DEFAULT NULL,
  `misc_ln1` varchar(50) DEFAULT NULL,
  `place_hold48` varchar(1) DEFAULT NULL,
  `package_ref_number5` varchar(35) DEFAULT NULL,
  `store_number` varchar(6) DEFAULT NULL,
  `place_hold41` varchar(1) DEFAULT NULL,
  `package_ref_number4` varchar(35) DEFAULT NULL,
  `place_hold42` varchar(1) DEFAULT NULL,
  `package_ref_number3` varchar(35) DEFAULT NULL,
  `place_hold43` varchar(1) DEFAULT NULL,
  `package_ref_number2` varchar(35) DEFAULT NULL,
  `place_hold44` varchar(1) DEFAULT NULL,
  `package_ref_number1` varchar(35) DEFAULT NULL,
  `entry_type` varchar(50) DEFAULT NULL,
  `shipment_import_date` varchar(10) DEFAULT NULL,
  `place_hold40` varchar(1) DEFAULT NULL,
  `trans_date` varchar(10) DEFAULT NULL,
  `receiver_comp_name` varchar(50) DEFAULT NULL,
  `shipment_export_date` varchar(10) DEFAULT NULL,
  `duty_rate` varchar(15) DEFAULT NULL,
  `invoice_date` varchar(10) DEFAULT NULL,
  `sender_state` varchar(30) DEFAULT NULL,
  `place_hold56` varchar(1) DEFAULT NULL,
  `tariff_code` varchar(50) DEFAULT NULL,
  `place_hold57` varchar(1) DEFAULT NULL,
  `sold_to_addr1` varchar(50) DEFAULT NULL,
  `oth_cus_num_ind` varchar(1) DEFAULT NULL,
  `place_hold58` varchar(1) DEFAULT NULL,
  `place_hold59` varchar(1) DEFAULT NULL,
  `place_hold52` varchar(1) DEFAULT NULL,
  `sold_to_addr2` varchar(50) DEFAULT NULL,
  `place_hold53` varchar(1) DEFAULT NULL,
  `place_hold54` varchar(1) DEFAULT NULL,
  `place_hold55` varchar(1) DEFAULT NULL,
  `sender_comp_name` varchar(50) DEFAULT NULL,
  `place_hold50` varchar(1) DEFAULT NULL,
  `vat_rate` varchar(15) DEFAULT NULL,
  `place_hold51` varchar(1) DEFAULT NULL,
  `order_in_council` varchar(16) DEFAULT NULL,
  `po10` varchar(20) DEFAULT NULL,
  `trans_mode` varchar(4) DEFAULT NULL,
  `cycle_number` varchar(50) DEFAULT NULL,
  `freight_seq_no` varchar(4) DEFAULT NULL,
  `sold_to_comp_name` varchar(50) DEFAULT NULL,
  `bol5` varchar(20) DEFAULT NULL,
  `charged_unit_quantity` varchar(7) DEFAULT NULL,
  `misc_addr1_state` varchar(30) DEFAULT NULL,
  `bol3` varchar(20) DEFAULT NULL,
  `bol4` varchar(20) DEFAULT NULL,
  `third_party_name` varchar(50) DEFAULT NULL,
  `bol1` varchar(20) DEFAULT NULL,
  `bol2` varchar(20) DEFAULT NULL,
  `sender_addr2` varchar(50) DEFAULT NULL,
  `sender_addr1` varchar(50) DEFAULT NULL,
  `tot_value_of_duty` varchar(17) DEFAULT NULL,
  `payer_role_code` varchar(2) DEFAULT NULL,
  `misc_addr1_addr1` varchar(50) DEFAULT NULL,
  `world_ease_number` varchar(26) DEFAULT NULL,
  `misc_addr1_addr2` varchar(50) DEFAULT NULL,
  `nmfc` varchar(20) DEFAULT NULL,
  `oth_cus_num` varchar(35) DEFAULT NULL,
  `sender_country` varchar(2) DEFAULT NULL,
  `eft_date` varchar(50) DEFAULT NULL,
  `alt_inv_amount` varchar(17) DEFAULT NULL,
  `third_party_city` varchar(50) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `billed_weight_unit_measure` varchar(1) DEFAULT NULL,
  `goods_desc` varchar(35) DEFAULT NULL,
  `inv_type_code` varchar(1) DEFAULT NULL,
  `val_date` varchar(50) DEFAULT NULL,
  `third_party_country` varchar(2) DEFAULT NULL,
  `tot_customs_amt` varchar(17) DEFAULT NULL,
  `entered_weight_unit_measure` varchar(1) DEFAULT NULL,
  `item_quantity` varchar(5) DEFAULT NULL,
  `office_number` varchar(50) DEFAULT NULL,
  `account_number` varchar(10) DEFAULT NULL,
  `sold_to_country` varchar(2) DEFAULT NULL,
  `receiver_city` varchar(50) DEFAULT NULL,
  `direct_ship_date` varchar(10) DEFAULT NULL,
  `cycle_date` varchar(50) DEFAULT NULL,
  `billed_weight_type` varchar(2) DEFAULT NULL,
  `third_party_state` varchar(30) DEFAULT NULL,
  `type_det_code_2` varchar(35) DEFAULT NULL,
  `type_det_code_1` varchar(35) DEFAULT NULL,
  `item_quan_unit_measure` varchar(14) DEFAULT NULL,
  `misc_ln10` varchar(58) DEFAULT NULL,
  `line_item_no` varchar(5) DEFAULT NULL,
  `carrier_name` varchar(50) DEFAULT NULL,
  `misc_ln11` varchar(91) DEFAULT NULL,
  `receiver_postal` varchar(12) DEFAULT NULL,
  `misc_net` varchar(17) DEFAULT NULL,
  `det_class` varchar(4) DEFAULT NULL,
  `xchge_rate` varchar(15) DEFAULT NULL,
  `excise_tax_rate` varchar(15) DEFAULT NULL,
  `recipient_number` varchar(10) DEFAULT NULL,
  `sender_city` varchar(50) DEFAULT NULL,
  `place_hold27` varchar(1) DEFAULT NULL,
  `place_hold28` varchar(1) DEFAULT NULL,
  `place_hold29` varchar(1) DEFAULT NULL,
  `place_hold24` varchar(1) DEFAULT NULL,
  `place_hold25` varchar(1) DEFAULT NULL,
  `entry_port` varchar(4) DEFAULT NULL,
  `place_hold26` varchar(4) DEFAULT NULL,
  `net_amount` varchar(17) DEFAULT NULL,
  `misc_addr1_city` varchar(50) DEFAULT NULL,
  `entered_value` varchar(17) DEFAULT NULL,
  `oth_amt` varchar(17) DEFAULT NULL,
  `seq_number` int(8) DEFAULT NULL,
  `org_ship_pkg_qnty` varchar(10) DEFAULT NULL,
  `customs_number` varchar(20) DEFAULT NULL,
  `shipment_date` varchar(10) DEFAULT NULL,
  `type_det_value_2` varchar(10) DEFAULT NULL,
  `type_det_value_1` varchar(10) DEFAULT NULL,
  `misc_cur_code` varchar(3) DEFAULT NULL,
  `shpip_relse_date` varchar(10) DEFAULT NULL,
  `sender_postal` varchar(12) DEFAULT NULL,
  `pkg_dim_unit_meas` varchar(1) DEFAULT NULL,
  `misc_addr2_postal` varchar(12) DEFAULT NULL,
  `invoice_number` varchar(20) DEFAULT NULL,
  `customer_ref_number` varchar(19) DEFAULT NULL,
  `third_party_company` varchar(50) DEFAULT NULL,
  `sold_to_city` varchar(50) DEFAULT NULL,
  `charge_desc_code` varchar(5) DEFAULT NULL,
  `epu` varchar(50) DEFAULT NULL,
  `misc_incentive_amount` varchar(17) DEFAULT NULL,
  `sold_to_state` varchar(30) DEFAULT NULL,
  `entered_weight` varchar(10) DEFAULT NULL,
  `container_type` varchar(3) DEFAULT NULL,
  `oth_basis_amt` varchar(17) DEFAULT NULL,
  `inv_type_detail_code` varchar(2) DEFAULT NULL,
  `package_dim` varchar(17) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `misc_addr2_name` varchar(50) DEFAULT NULL,
  `class_number` varchar(15) DEFAULT NULL,
  `billed_weight` varchar(10) DEFAULT NULL,
  `forgn_trd_ref_number` varchar(50) DEFAULT NULL,
  `trans_cur_code` varchar(3) DEFAULT NULL,
  `invoice_amount` varchar(17) DEFAULT NULL,
  `receiver_state` varchar(30) DEFAULT NULL,
  `third_party_postal` varchar(12) DEFAULT NULL,
  `duty_value` varchar(17) DEFAULT NULL,
  `receiver_name` varchar(50) DEFAULT NULL,
  `lead_shipment_number` varchar(26) DEFAULT NULL,
  `duty_amount` varchar(17) DEFAULT NULL,
  `contact_name` varchar(50) DEFAULT NULL,
  `alt_inv_cur_code` varchar(3) DEFAULT NULL,
  `alt_inv_number` varchar(15) DEFAULT NULL,
  `misc_addr2_addr1` varchar(50) DEFAULT NULL,
  `misc_addr2_addr2` varchar(50) DEFAULT NULL,
  `charge_cat_det_code` varchar(4) DEFAULT NULL,
  `vat_basis_amt` varchar(17) DEFAULT NULL,
  `shipment_ref_number2` varchar(35) DEFAULT NULL,
  `vat_amt` varchar(17) DEFAULT NULL,
  `shipment_ref_number1` varchar(35) DEFAULT NULL,
  `sender_name` varchar(50) DEFAULT NULL,
  `cccd_number` varchar(11) DEFAULT NULL,
  `cur_var` varchar(17) DEFAULT NULL,
  `master_air_waybill_no` varchar(50) DEFAULT NULL,
  `charge_class_code` varchar(3) DEFAULT NULL,
  `entry_date` varchar(10) DEFAULT NULL,
  `charge_source` varchar(5) DEFAULT NULL,
  `zone` varchar(3) DEFAULT NULL,
  `export_place` varchar(2) DEFAULT NULL,
  `tariff_rate` varchar(15) DEFAULT NULL,
  `ship_desc` varchar(50) DEFAULT NULL,
  `basis_value` varchar(17) DEFAULT NULL,
  `incentive_amount` varchar(17) DEFAULT NULL,
  `oth_rate` varchar(15) DEFAULT NULL,
  `misc_addr1_comp_name` varchar(50) DEFAULT NULL,
  `basis_cur_code` varchar(3) DEFAULT NULL,
  `inv_xchg_rate` varchar(17) DEFAULT NULL,
  `dec_number` varchar(50) DEFAULT NULL,
  `import_tax_id` varchar(14) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `inv_cur_code` varchar(3) DEFAULT NULL,
  `misc_addr2_comp_name` varchar(50) DEFAULT NULL,
  `pickup_rec_number` varchar(10) DEFAULT NULL,
  `tax_indicator` varchar(1) DEFAULT NULL,
  `doc_type` varchar(50) DEFAULT NULL,
  `type_code_2` varchar(2) DEFAULT NULL,
  `type_code_1` varchar(2) DEFAULT NULL,
  `gst_rate` varchar(15) DEFAULT NULL,
  `cpc_code` varchar(50) DEFAULT NULL,
  `tax_value` varchar(17) DEFAULT NULL,
  `dec_freight_cls` varchar(4) DEFAULT NULL,
  `tracking_number` varchar(26) DEFAULT NULL,
  `unit_of_measure` varchar(10) DEFAULT NULL,
  `misc_addr1_name` varchar(50) DEFAULT NULL,
  `account_country` varchar(2) DEFAULT NULL,
  `charge_desc` varchar(100) DEFAULT NULL,
  `entry_number` varchar(50) DEFAULT NULL,
  `misc_addr1_country` varchar(2) DEFAULT NULL,
  `origin_country` varchar(2) DEFAULT NULL,
  `cus_off_name` varchar(15) DEFAULT NULL,
  `misc_addr_qual_1` varchar(1) DEFAULT NULL,
  `package_quantity` varchar(8) DEFAULT NULL,
  `misc_addr_qual_2` varchar(1) DEFAULT NULL,
  `inv_due_date` varchar(10) DEFAULT NULL,
  `entered_cur_code` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_header`
--

DROP TABLE IF EXISTS `order_edi_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) DEFAULT NULL,
  `detail_seq_number` int(8) DEFAULT NULL,
  `processed_date` datetime DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `merge_count` int(10) DEFAULT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `carrier_type` tinyint(1) DEFAULT NULL,
  `edi_file_name` varchar(150) DEFAULT NULL,
  `elapsed_time` bigint(20) DEFAULT NULL,
  `invoice_number` varchar(20) DEFAULT NULL,
  `tot_invoice_amount` decimal(30,2) DEFAULT NULL,
  `released` tinyint(1) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6693 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_edi_service`
--

DROP TABLE IF EXISTS `order_edi_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_edi_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20) DEFAULT NULL,
  `markup` decimal(30,2) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `service_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_flight_segment`
--

DROP TABLE IF EXISTS `order_flight_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_flight_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spot_rate_kilo` varchar(255) DEFAULT NULL,
  `spot_rate_nbr` varchar(255) DEFAULT NULL,
  `airport_code` varchar(255) DEFAULT NULL,
  `flight_nbr` varchar(255) DEFAULT NULL,
  `consign_airway_bill` varchar(255) DEFAULT NULL,
  `cws_acct_nbr` varchar(255) DEFAULT NULL,
  `eta` datetime DEFAULT NULL,
  `etd` datetime DEFAULT NULL,
  `carrier_value` int(11) DEFAULT NULL,
  `air_name` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `customer_acct_nbr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_invoice_comments`
--

DROP TABLE IF EXISTS `order_invoice_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_invoice_comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3849 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_invoice_creditnotes`
--

DROP TABLE IF EXISTS `order_invoice_creditnotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_invoice_creditnotes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2220 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_invoice_workorder`
--

DROP TABLE IF EXISTS `order_invoice_workorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_invoice_workorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369846 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_job_type`
--

DROP TABLE IF EXISTS `order_job_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_job_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_rate`
--

DROP TABLE IF EXISTS `order_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` varchar(128) DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_rate_quote`
--

DROP TABLE IF EXISTS `order_rate_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_rate_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `carrier_id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL,
  `transit_days` int(11) DEFAULT NULL,
  `total_charges` decimal(30,2) DEFAULT NULL,
  `base_fee` decimal(30,2) DEFAULT NULL,
  `fuel_fee` decimal(30,2) DEFAULT NULL,
  `insurance` decimal(30,2) DEFAULT NULL,
  `cross_border_fee` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_service`
--

DROP TABLE IF EXISTS `order_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_status`
--

DROP TABLE IF EXISTS `order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_status_change`
--

DROP TABLE IF EXISTS `order_status_change`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_status_change` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `old_status` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `new_status` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_status_message`
--

DROP TABLE IF EXISTS `order_status_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_status_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_text` varchar(255) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_subservice`
--

DROP TABLE IF EXISTS `order_subservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_subservice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_type` int(11) DEFAULT NULL,
  `apply_fuel_surcharge` bit(1) DEFAULT NULL,
  `charge_value` decimal(30,2) DEFAULT NULL,
  `charge_unit` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `cost_value` decimal(30,2) DEFAULT NULL,
  `eshipper_surcharge_id` bigint(20) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `commissionable` bit(1) DEFAULT NULL,
  `charge_quantity` int(11) DEFAULT NULL,
  `apply_tax` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `wos_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1215 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_subservice_eshipper_charge`
--

DROP TABLE IF EXISTS `order_subservice_eshipper_charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_subservice_eshipper_charge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `woss_id` bigint(20) DEFAULT NULL,
  `eshipper_charge_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_tax`
--

DROP TABLE IF EXISTS `order_tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_tax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `final_total_value` decimal(30,2) DEFAULT NULL,
  `total_value` decimal(30,2) DEFAULT NULL,
  `edi_charge_id` bigint(20) DEFAULT NULL,
  `total_value_charge` decimal(30,2) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `final_total_value_charge` decimal(30,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=295761 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_taxes`
--

DROP TABLE IF EXISTS `order_taxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_taxes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_transport_segment`
--

DROP TABLE IF EXISTS `order_transport_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_transport_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `airbill_number` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_trucking_pre_alert`
--

DROP TABLE IF EXISTS `order_trucking_pre_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_trucking_pre_alert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commodity` varchar(255) DEFAULT NULL,
  `special_instructions` varchar(1000) DEFAULT NULL,
  `trucking_contact` varchar(255) DEFAULT NULL,
  `delivery_date_time` datetime DEFAULT NULL,
  `trucking_fax_number` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type_of_truck` varchar(255) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `delivery_address_id` bigint(20) DEFAULT NULL,
  `agreed_price` decimal(30,2) DEFAULT NULL,
  `agreed_price_currency` varchar(255) DEFAULT NULL,
  `trucking_company` varchar(255) DEFAULT NULL,
  `number_of_pieces` int(11) DEFAULT NULL,
  `pickup_address_id` bigint(20) DEFAULT NULL,
  `job_description` varchar(255) DEFAULT NULL,
  `trucking_phone_number` varchar(255) DEFAULT NULL,
  `trucking_service` varchar(255) DEFAULT NULL,
  `pickup_date_time` datetime DEFAULT NULL,
  `trucking_date_time` datetime DEFAULT NULL,
  `dimension` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2536 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_trucking_segment`
--

DROP TABLE IF EXISTS `order_trucking_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_trucking_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dedicated` varchar(255) DEFAULT NULL,
  `airway_bill_info` varchar(255) DEFAULT NULL,
  `paps_nbr` varchar(255) DEFAULT NULL,
  `ltl` varchar(255) DEFAULT NULL,
  `line_haul` varchar(255) DEFAULT NULL,
  `pars_nbr` varchar(255) DEFAULT NULL,
  `manifest_nbr` varchar(255) DEFAULT NULL,
  `ftl` varchar(255) DEFAULT NULL,
  `team_driver` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23725 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_workorder_service`
--

DROP TABLE IF EXISTS `order_workorder_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_workorder_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_workorder_subservice`
--

DROP TABLE IF EXISTS `order_workorder_subservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_workorder_subservice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_type` int(11) DEFAULT NULL,
  `charge_value` decimal(30,2) DEFAULT NULL,
  `charge_unit` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `cost_value` decimal(30,2) DEFAULT NULL,
  `ss_id` bigint(20) DEFAULT NULL,
  `commissionable` bit(1) DEFAULT NULL,
  `charge_quantity` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `wos_id` int(11) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3660 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package`
--

DROP TABLE IF EXISTS `package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `nmfc_code` varchar(50) DEFAULT NULL,
  `cod_amount` varchar(128) DEFAULT NULL,
  `pieces` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tracking_number` varchar(255) DEFAULT NULL,
  `dim_length` varchar(128) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `cubed_weight` int(11) DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `weight` varchar(128) DEFAULT NULL,
  `old_order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `insurance_amount` decimal(30,2) DEFAULT NULL,
  `dim_height` varchar(128) DEFAULT NULL,
  `dim_width` varchar(128) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `cod_value` decimal(30,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `freight_class` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=677316 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_service`
--

DROP TABLE IF EXISTS `package_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `carrier_service_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=881 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_type`
--

DROP TABLE IF EXISTS `package_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `markup_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_type_services`
--

DROP TABLE IF EXISTS `package_type_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package_type_services` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_type_vehicle_types`
--

DROP TABLE IF EXISTS `package_type_vehicle_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package_type_vehicle_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `elt` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pallet`
--

DROP TABLE IF EXISTS `pallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dim_height` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  `freightclass` varchar(128) DEFAULT NULL,
  `dim_width` varchar(128) DEFAULT NULL,
  `weight` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `dim_length` varchar(128) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `insurance` decimal(30,2) DEFAULT NULL,
  `nmfc_code` varchar(24) DEFAULT NULL,
  `pieces` int(11) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pallet_type`
--

DROP TABLE IF EXISTS `pallet_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pallet_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_transaction`
--

DROP TABLE IF EXISTS `payment_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chq_image` varchar(128) DEFAULT NULL,
  `creditcard_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type` varchar(128) DEFAULT NULL,
  `tx_id` bigint(20) DEFAULT NULL,
  `created_by_user_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tx_result` varchar(128) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `chq_date` varchar(128) DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `chq_number` varchar(128) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pickup`
--

DROP TABLE IF EXISTS `pickup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pickup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `num_packages` int(11) DEFAULT NULL,
  `confirmation_number` varchar(255) DEFAULT NULL,
  `charge_card_expiry_date` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `next_pickup_date` datetime DEFAULT NULL,
  `ready_time` varchar(25) DEFAULT NULL,
  `afterhours_closing_time` varchar(255) DEFAULT NULL,
  `contact_extension` varchar(255) DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `pickup_date` datetime DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `cc_transaction_id` bigint(20) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `afterhours_closing_location` varchar(255) DEFAULT NULL,
  `special_instructions` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `charge` float DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `call_time` varchar(25) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `close_time` varchar(255) DEFAULT NULL,
  `ready_time_2` varchar(25) DEFAULT NULL,
  `location_type` varchar(255) DEFAULT NULL,
  `charge_card_number` varchar(255) DEFAULT NULL,
  `package_location` varchar(255) DEFAULT NULL,
  `charge_card_type` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `pickup_time` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `call_time_2` varchar(25) DEFAULT NULL,
  `status` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107920 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pins`
--

DROP TABLE IF EXISTS `pins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `next_pin` bigint(20) DEFAULT NULL,
  `from_pin` bigint(20) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `to_pin` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=483 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polaris_ca_rating_zone`
--

DROP TABLE IF EXISTS `polaris_ca_rating_zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polaris_ca_rating_zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating_zone` varchar(25) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=861655 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `privileged_rate`
--

DROP TABLE IF EXISTS `privileged_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privileged_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `privilieged_rate`
--

DROP TABLE IF EXISTS `privilieged_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilieged_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` float DEFAULT NULL,
  `rate_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_list`
--

DROP TABLE IF EXISTS `product_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `product_id` varchar(255) DEFAULT NULL,
  `country_of_origin` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `harmonized_code` varchar(255) DEFAULT NULL,
  `unit_price` varchar(255) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34567 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `properties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `property_type` varchar(45) DEFAULT NULL,
  `property_value` varchar(255) DEFAULT NULL,
  `property_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `province`
--

DROP TABLE IF EXISTS `province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `province` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `puro_invoicing_temp_final`
--

DROP TABLE IF EXISTS `puro_invoicing_temp_final`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puro_invoicing_temp_final` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actual_length` double DEFAULT NULL,
  `shipment_tot_amt` double DEFAULT NULL,
  `actual_height` double DEFAULT NULL,
  `beyond_surcharge` double DEFAULT NULL,
  `actual_width` double DEFAULT NULL,
  `resi_area_surcharge` double DEFAULT NULL,
  `fuel_surcharge` float DEFAULT NULL,
  `shipment_base_amt` float DEFAULT NULL,
  `res_sign_surcharge` double DEFAULT NULL,
  `actual_weight` double DEFAULT NULL,
  `address_correction` double DEFAULT NULL,
  `multi_piece_surcharge` double DEFAULT NULL,
  `bol_manifest_num` varchar(90) DEFAULT NULL,
  `declared_weight` double DEFAULT NULL,
  `special_handling` double DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=581 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `puro_invoicing_temp_final_archive`
--

DROP TABLE IF EXISTS `puro_invoicing_temp_final_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puro_invoicing_temp_final_archive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actual_wgt` double DEFAULT NULL,
  `actual_length` double DEFAULT NULL,
  `shipment_tot_amt` double DEFAULT NULL,
  `actual_height` double DEFAULT NULL,
  `beyond_surcharge` double DEFAULT NULL,
  `actual_width` double DEFAULT NULL,
  `resi_area_surcharge` double DEFAULT NULL,
  `fuel_surcharge` float DEFAULT NULL,
  `shipment_base_amt` float DEFAULT NULL,
  `res_sign_surcharge` double DEFAULT NULL,
  `address_correction` double DEFAULT NULL,
  `multi_piece_surcharge` double DEFAULT NULL,
  `bol_manifest_num` varchar(90) DEFAULT NULL,
  `declared_weight` double DEFAULT NULL,
  `special_handling` double DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4546 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `puro_orderid`
--

DROP TABLE IF EXISTS `puro_orderid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puro_orderid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bol_manifest_num` varchar(765) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `puro_temp`
--

DROP TABLE IF EXISTS `puro_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puro_temp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipment_tot_amount` double DEFAULT NULL,
  `bol_manifest_num` varchar(270) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=656 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purocourier_manifest_details`
--

DROP TABLE IF EXISTS `purocourier_manifest_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purocourier_manifest_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `manifest_pdf_label` blob,
  `manifest_number` varchar(15) DEFAULT NULL,
  `manifest_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purolator_first_tariff_negotiated_beyond_and_rural`
--

DROP TABLE IF EXISTS `purolator_first_tariff_negotiated_beyond_and_rural`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purolator_first_tariff_negotiated_beyond_and_rural` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rural_amount` decimal(30,2) DEFAULT NULL,
  `beyond_disc_perc` double DEFAULT NULL,
  `price_zone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purolator_first_tariff_surcharge_data`
--

DROP TABLE IF EXISTS `purolator_first_tariff_surcharge_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purolator_first_tariff_surcharge_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `surcharge_method` int(11) DEFAULT NULL,
  `surcharge_amount` decimal(30,2) DEFAULT NULL,
  `record_type` int(11) DEFAULT NULL,
  `surcharge_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purolator_negotiated_beyond_and_rural`
--

DROP TABLE IF EXISTS `purolator_negotiated_beyond_and_rural`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purolator_negotiated_beyond_and_rural` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rural_amount` decimal(30,2) DEFAULT NULL,
  `beyond_disc_perc` double DEFAULT NULL,
  `price_zone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quote`
--

DROP TABLE IF EXISTS `quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `expires_at` datetime DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_adjustment`
--

DROP TABLE IF EXISTS `rate_adjustment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_adjustment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_at` datetime DEFAULT NULL,
  `fixed_amount` varchar(128) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `min_amount` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `fraction` varchar(128) DEFAULT NULL,
  `calc_from` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `max_amount` varchar(128) DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_charge_type`
--

DROP TABLE IF EXISTS `rt_charge_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_charge_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comm_aplcble` bit(1) DEFAULT NULL,
  `tax_aplcble` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `fuel_aplcble` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_city`
--

DROP TABLE IF EXISTS `rt_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `province_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_rate`
--

DROP TABLE IF EXISTS `rt_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` float DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_service_transit_code_rate`
--

DROP TABLE IF EXISTS `rt_service_transit_code_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_service_transit_code_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rate_id` bigint(20) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `transit_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_transit_code_vehicle_rate`
--

DROP TABLE IF EXISTS `rt_transit_code_vehicle_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_transit_code_vehicle_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rate_id` bigint(20) DEFAULT NULL,
  `vehicle_type` int(11) DEFAULT NULL,
  `transit_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_vehicle_type`
--

DROP TABLE IF EXISTS `rt_vehicle_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_vehicle_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `free_wgt` float DEFAULT NULL,
  `ext_wgt_ch_name` varchar(255) DEFAULT NULL,
  `service_id_full` bigint(20) DEFAULT NULL,
  `rate_id_ext_wgt` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `ext_pcs_ch_name` varchar(255) DEFAULT NULL,
  `rate_id_wait_time` bigint(20) DEFAULT NULL,
  `max_pcs` float DEFAULT NULL,
  `rate_id_add_freight` bigint(20) DEFAULT NULL,
  `wait_time_ch_name` varchar(255) DEFAULT NULL,
  `free_wait_time` varchar(255) DEFAULT NULL,
  `rate_id_ext_pcs` bigint(20) DEFAULT NULL,
  `max_wgt` float DEFAULT NULL,
  `fuel_aplcble` bit(1) DEFAULT NULL,
  `free_pcs` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_zone`
--

DROP TABLE IF EXISTS `rt_zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_zone_city_map`
--

DROP TABLE IF EXISTS `rt_zone_city_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_zone_city_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `zone_id` bigint(20) DEFAULT NULL,
  `city_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_zone_postal_code`
--

DROP TABLE IF EXISTS `rt_zone_postal_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_zone_postal_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_pc` varchar(255) DEFAULT NULL,
  `zone_id` bigint(20) DEFAULT NULL,
  `start_pc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_zone_transit_code`
--

DROP TABLE IF EXISTS `rt_zone_transit_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_zone_transit_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_zone_id` bigint(20) DEFAULT NULL,
  `to_zone_id` bigint(20) DEFAULT NULL,
  `transit_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `selected_quote`
--

DROP TABLE IF EXISTS `selected_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `selected_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_id` bigint(20) NOT NULL,
  `transit_days` int(11) DEFAULT NULL,
  `total_charges` decimal(30,2) DEFAULT NULL,
  `base_fee` decimal(30,2) DEFAULT NULL,
  `fuel_fee` decimal(30,2) DEFAULT NULL,
  `insurance` decimal(30,2) DEFAULT NULL,
  `cross_border_fee` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=504665 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_at` datetime DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `color` varchar(250) DEFAULT NULL,
  `transit_code_intl` varchar(45) DEFAULT NULL,
  `contract_id` varchar(100) DEFAULT NULL,
  `carrier_service_name` varchar(250) DEFAULT NULL,
  `display_order` tinyint(1) unsigned DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `mode` int(11) DEFAULT NULL,
  `code_us` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `logo` varchar(128) DEFAULT NULL,
  `distribution_id` bigint(20) DEFAULT NULL,
  `terminal_info_link` varchar(500) DEFAULT NULL,
  `billing_address_id` bigint(20) unsigned DEFAULT NULL,
  `code_intl` varchar(255) DEFAULT NULL,
  `markup_id` bigint(20) DEFAULT NULL,
  `service_time_mins` int(11) DEFAULT NULL,
  `guaranteed` bit(1) DEFAULT NULL,
  `discontinued` tinyint(1) unsigned DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `rate_adjustments_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `service_name_us` varchar(255) DEFAULT NULL,
  `tran_fee` decimal(30,2) DEFAULT NULL,
  `transit_code_us` varchar(45) DEFAULT NULL,
  `transit_code_ca` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ship_item`
--

DROP TABLE IF EXISTS `ship_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ship_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dim_height` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `freightclass` varchar(128) DEFAULT NULL,
  `dim_width` varchar(128) DEFAULT NULL,
  `weight` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `type` varchar(128) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `dim_length` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipments_by_cust_view`
--

DROP TABLE IF EXISTS `shipments_by_cust_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipments_by_cust_view` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_count` bigint(21) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `package_type` varchar(7) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipping_address`
--

DROP TABLE IF EXISTS `shipping_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `instructions` text,
  `postal_zip` varchar(128) DEFAULT NULL,
  `care_of` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL,
  `city` varchar(128) DEFAULT NULL,
  `tailgate` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `street_name` varchar(128) DEFAULT NULL,
  `confirm_delivery` tinyint(1) DEFAULT NULL,
  `unit_number` varchar(128) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `country_name` varchar(50) DEFAULT NULL,
  `is_residential` varchar(128) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `res` tinyint(1) DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `province_state` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `notify_recipient` tinyint(1) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `company_name` varchar(128) DEFAULT NULL,
  `street_number` varchar(128) DEFAULT NULL,
  `attention` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `address_book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=912888 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipping_labels`
--

DROP TABLE IF EXISTS `shipping_labels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_labels` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_destination_string` varchar(255) DEFAULT NULL,
  `system_num_string` varchar(255) DEFAULT NULL,
  `twod_label_string` varchar(1000) DEFAULT NULL,
  `dimmed_string` varchar(255) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `cod_label` mediumblob,
  `origin_id` varchar(255) DEFAULT NULL,
  `label_type` int(11) DEFAULT NULL,
  `label` mediumblob,
  `astra_label_string` varchar(255) DEFAULT NULL,
  `actual_weight_string` varchar(255) DEFAULT NULL,
  `ship_date_string` varchar(255) DEFAULT NULL,
  `deliv_day_string` varchar(255) DEFAULT NULL,
  `tracking_number` varchar(255) DEFAULT NULL,
  `deliv_by_string` varchar(255) DEFAULT NULL,
  `ursa_code_string` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `service_commitment_string` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201739 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipping_labels_archive`
--

DROP TABLE IF EXISTS `shipping_labels_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_labels_archive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_destination_string` varchar(765) DEFAULT NULL,
  `system_num_string` varchar(765) DEFAULT NULL,
  `twod_label_string` varchar(3000) DEFAULT NULL,
  `dimmed_string` varchar(765) DEFAULT NULL,
  `form_id` varchar(765) DEFAULT NULL,
  `cod_label` blob,
  `origin_id` varchar(765) DEFAULT NULL,
  `label_type` int(11) DEFAULT NULL,
  `label` blob,
  `astra_label_string` varchar(765) DEFAULT NULL,
  `actual_weight_string` varchar(765) DEFAULT NULL,
  `ship_date_string` varchar(765) DEFAULT NULL,
  `deliv_day_string` varchar(765) DEFAULT NULL,
  `tracking_number` varchar(765) DEFAULT NULL,
  `deliv_by_string` varchar(765) DEFAULT NULL,
  `archive_order_id` bigint(20) DEFAULT NULL,
  `ursa_code_string` varchar(765) DEFAULT NULL,
  `service_commitment_string` varchar(765) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipping_order_archive`
--

DROP TABLE IF EXISTS `shipping_order_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_order_archive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_cost_franchise` double DEFAULT NULL,
  `quoted_base_charge` float DEFAULT NULL,
  `fuel_cost` float DEFAULT NULL,
  `cod_cheque_date` date DEFAULT NULL,
  `origin_terminal` bigint(20) DEFAULT NULL,
  `origin_close_time_h` varchar(135) DEFAULT NULL,
  `total_cost` double DEFAULT NULL,
  `load_id` bigint(20) DEFAULT NULL,
  `eshipper_user` int(11) DEFAULT NULL,
  `scheduled_ship_date` datetime DEFAULT NULL,
  `quoted_fuel_cost` float DEFAULT NULL,
  `freeze_protect` tinyint(1) DEFAULT NULL,
  `origin_close_time_m` varchar(135) DEFAULT NULL,
  `ship_to_id` bigint(20) DEFAULT NULL,
  `cod_payable_to` text,
  `bol_id` varchar(750) DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `distribution_quote` float DEFAULT NULL,
  `pier_charge` tinyint(1) DEFAULT NULL,
  `guarantee_charge` int(11) DEFAULT NULL,
  `inside_delivery` tinyint(1) DEFAULT NULL,
  `inside_pickup` tinyint(1) DEFAULT NULL,
  `sat_delivery` tinyint(1) DEFAULT NULL,
  `payment_type_id` int(11) DEFAULT NULL,
  `military_base_delivery` tinyint(1) DEFAULT NULL,
  `quoted_total_charge` float DEFAULT NULL,
  `manifest_number` text,
  `mfw_connection_key` text,
  `update_color_code` bit(1) DEFAULT NULL,
  `shipment_error` varchar(3000) DEFAULT NULL,
  `homeland_security` tinyint(1) DEFAULT NULL,
  `ship_from_id` bigint(20) DEFAULT NULL,
  `insured_amount` float DEFAULT NULL,
  `frozen` tinyint(1) DEFAULT NULL,
  `total_charge` double DEFAULT NULL,
  `master_tracking_num_1` text,
  `protective_drum_cover` tinyint(1) DEFAULT NULL,
  `excess_length` tinyint(1) DEFAULT NULL,
  `quoted_total_cost` float DEFAULT NULL,
  `reference3_name` text,
  `limited_access` tinyint(1) DEFAULT NULL,
  `pickup_time` text,
  `cod_value` float DEFAULT NULL,
  `shipment_density` int(11) DEFAULT NULL,
  `req_delivery_date` text,
  `tender_error_message` text,
  `vehicle_full` bit(1) DEFAULT NULL,
  `heated_service` tinyint(1) DEFAULT NULL,
  `reference_name` text,
  `dim_type` int(11) DEFAULT NULL,
  `partner_markup` float DEFAULT NULL,
  `batch_id` varchar(150) DEFAULT NULL,
  `claim_id` bigint(20) DEFAULT NULL,
  `freight_tariff` float DEFAULT NULL,
  `fuel_tariff` float DEFAULT NULL,
  `tender_status` text,
  `pod_file` blob,
  `expected_transit` int(11) DEFAULT NULL,
  `currency_rate` float DEFAULT NULL,
  `dangerous_goods` text,
  `master_tracking_num` text,
  `internal_notes` text,
  `tot_act_wgt` float DEFAULT NULL,
  `billing_status` tinyint(3) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `tracking_url` varchar(1800) DEFAULT NULL,
  `sort_segregate` tinyint(1) DEFAULT NULL,
  `actual_base_charge` double DEFAULT NULL,
  `unload_freight_at_del` tinyint(1) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_of_delivery` datetime DEFAULT NULL,
  `vehicle_type` int(11) DEFAULT NULL,
  `mode_transport` text,
  `destination_close_time_h` varchar(135) DEFAULT NULL,
  `mark_and_tag_freight` tinyint(1) DEFAULT NULL,
  `mfw_booking_key` varchar(750) DEFAULT NULL,
  `carrier_pick_up_conf` varchar(75) DEFAULT NULL,
  `destination_close_time_m` varchar(135) DEFAULT NULL,
  `delivery_appt` tinyint(1) DEFAULT NULL,
  `comm_driver_id` bigint(20) DEFAULT NULL,
  `is_e_manifested` bit(1) DEFAULT NULL,
  `proof_of_delivery` text,
  `customer_id` bigint(20) DEFAULT NULL,
  `customes_freight` tinyint(1) DEFAULT NULL,
  `assign_driver_id` bigint(20) DEFAULT NULL,
  `gross_profit` double DEFAULT NULL,
  `rate_error` varchar(3000) DEFAULT NULL,
  `destination_terminal` bigint(20) DEFAULT NULL,
  `quoted_fuel_surcharge` float DEFAULT NULL,
  `pod_file_name` varchar(135) DEFAULT NULL,
  `tot_act_quantity` int(11) DEFAULT NULL,
  `hold` tinyint(1) DEFAULT NULL,
  `cod_payment` text,
  `insurance_value_3rd` float DEFAULT NULL,
  `paper_manifest_status` int(11) DEFAULT NULL,
  `package_type_id` bigint(20) DEFAULT NULL,
  `scheduled_pick_up_id` bigint(20) DEFAULT NULL,
  `exibition_site` tinyint(1) DEFAULT NULL,
  `color_code` varchar(90) DEFAULT NULL,
  `is_insurance_manifested` bit(1) DEFAULT NULL,
  `wait_time` int(11) DEFAULT NULL,
  `fuel_surcharge` float DEFAULT NULL,
  `edi_verified` bit(1) DEFAULT NULL,
  `insurance_currency` int(3) DEFAULT NULL,
  `insurance_type` int(11) DEFAULT NULL,
  `payment_type` text,
  `total_tariff` float DEFAULT NULL,
  `original_markdown_type` int(10) DEFAULT NULL,
  `after_hours` bit(1) DEFAULT NULL,
  `eshipper_order_id` bigint(20) DEFAULT NULL,
  `per_cubic_feet` varchar(63) DEFAULT NULL,
  `invoice_status` tinyint(3) DEFAULT NULL,
  `transit_days` int(11) DEFAULT NULL,
  `pickup_num` int(11) DEFAULT NULL,
  `reference3` text,
  `distribution_service_id` bigint(20) DEFAULT NULL,
  `cod_currency` text,
  `param_service_id` bigint(20) DEFAULT NULL,
  `expected_delivery_date` varchar(300) DEFAULT NULL,
  `base_cost` float DEFAULT NULL,
  `cross_border_fee` tinyint(1) DEFAULT NULL,
  `reference2` text,
  `signature_required` int(11) DEFAULT NULL,
  `quote_status` int(11) DEFAULT NULL,
  `cod_pin_number` varchar(45) DEFAULT NULL,
  `return_service` tinyint(1) DEFAULT NULL,
  `has_been_edited` bit(1) DEFAULT NULL,
  `carrier_id` bigint(20) DEFAULT NULL,
  `canadian_customs` tinyint(1) DEFAULT NULL,
  `insurance_value` float DEFAULT NULL,
  `single_shipment` tinyint(1) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `reference_code` text,
  `base_charge` float DEFAULT NULL,
  `customer_markdown` float DEFAULT NULL,
  `inbond_fee` tinyint(1) DEFAULT NULL,
  `load_freight_at_pickup` tinyint(1) DEFAULT NULL,
  `reference2_name` text,
  `quantity` bigint(20) DEFAULT NULL,
  `pickup_time_h` varchar(135) DEFAULT NULL,
  `pickup_time_m` varchar(135) DEFAULT NULL,
  `editable` tinyint(1) DEFAULT NULL,
  `customer_markup` float DEFAULT NULL,
  `customer_markup_type` int(10) DEFAULT NULL,
  `protective_pallet_cover` tinyint(1) DEFAULT NULL,
  `special_equipment` text,
  `quoted_base_cost` float DEFAULT NULL,
  `update_status` bit(1) DEFAULT NULL,
  `return_order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=142264 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subservice`
--

DROP TABLE IF EXISTS `subservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subservice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surcharge`
--

DROP TABLE IF EXISTS `surcharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surcharge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(30,2) DEFAULT NULL,
  `code` varchar(128) DEFAULT NULL,
  `charged_amount2` decimal(30,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `market_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `cost_to_mf` decimal(30,2) DEFAULT NULL,
  `charged_amount` decimal(30,2) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `charge_id` int(11) DEFAULT NULL,
  `quoted_cost_to_f` decimal(30,2) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `quoted_amount` decimal(30,2) DEFAULT NULL,
  `charge_tax_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `charge_type_id` bigint(20) DEFAULT NULL,
  `cost_to_f` decimal(30,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `fuel_aplcble` bit(1) DEFAULT NULL,
  `quoted_cost_to_mf` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=552089 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surcharge_default_cost`
--

DROP TABLE IF EXISTS `surcharge_default_cost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surcharge_default_cost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` varchar(128) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `market_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system_property`
--

DROP TABLE IF EXISTS `system_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `data` text,
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab`
--

DROP TABLE IF EXISTS `tab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_rate`
--

DROP TABLE IF EXISTS `tax_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `tax_number` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `fraction` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temp_generic`
--

DROP TABLE IF EXISTS `temp_generic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp_generic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `c3` varchar(90) DEFAULT NULL,
  `c4` varchar(90) DEFAULT NULL,
  `n1` decimal(11,0) DEFAULT NULL,
  `n2` decimal(11,0) DEFAULT NULL,
  `n3` decimal(11,0) DEFAULT NULL,
  `d1` datetime DEFAULT NULL,
  `c1` varchar(90) DEFAULT NULL,
  `c2` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1400 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transport_segment`
--

DROP TABLE IF EXISTS `transport_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transport_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `login` varchar(128) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `first_time_login` bit(1) DEFAULT NULL,
  `cell` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `lastname` varchar(128) DEFAULT NULL,
  `password` text,
  `deleted` bit(1) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `sub_type` varchar(255) DEFAULT NULL,
  `first_login_complete` bit(1) DEFAULT NULL,
  `password_expiry` datetime DEFAULT NULL,
  `new_password_required` tinyint(1) DEFAULT '0',
  `fax` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `email` (`email`),
  KEY `customer_id` (`customer_id`),
  KEY `login` (`login`),
  KEY `email_2` (`email`),
  KEY `customer_id_2` (`customer_id`),
  KEY `login_2` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=13204 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_name` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12681 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weight_break`
--

DROP TABLE IF EXISTS `weight_break`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weight_break` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `max_weight` double DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `markup_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `min_weight` double DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workorder_status`
--

DROP TABLE IF EXISTS `workorder_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workorder_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xpress_ship_work_order_shipping_order`
--

DROP TABLE IF EXISTS `xpress_ship_work_order_shipping_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xpress_ship_work_order_shipping_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_cost_franchise` decimal(30,2) DEFAULT NULL,
  `total_cost` decimal(30,2) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `adjustment` decimal(30,2) DEFAULT NULL,
  `total_charge` decimal(30,2) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `markup_perc` float DEFAULT NULL,
  `invoice_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-06 14:43:16
