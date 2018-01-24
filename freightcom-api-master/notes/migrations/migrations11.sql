



alter table charge
add disputed_at datetime,
add dispute_resolved_at datetime;


drop table if exists charge_dispute;


CREATE TABLE `charge_dispute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_id` bigint(20),
  `logged_event_id` bigint(20),
  `position` int default 0,
  `created_at` timestamp default current_timestamp,
  PRIMARY KEY (`id`),
  KEY (`charge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


drop table if exists customer_billing;


CREATE TABLE `customer_billing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20),
  last_invoiced datetime,
  last_charged datetime,
  last_invoice_run datetime,
  last_charge_run datetime,

  PRIMARY KEY (`id`),
  KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table customer
add key(`auto_invoice`),
add key(`auto_charge`);




drop table if exists customs_invoice;
drop table if exists customs_invoice_product;
drop table if exists customs_invoice_contact_info;


CREATE TABLE `customs_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20),
  `bill_to_id` bigint(20),
  `contact_info_id` bigint(20),
  `dutiable` tinyint(1),
  `bill`     varchar(64),
  `consigneeAccount`  varchar(64),
  `sedNumber`  varchar(64),
  `created_at` timestamp default current_timestamp,
  `modified_at` timestamp,
  `deleted_at` timestamp,
  PRIMARY KEY (`id`),
  KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `customs_invoice_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customs_invoice_id` bigint(20),
  `description` text,
  `hscode`  varchar(64),
  `origin`  varchar(64),
  `quantity`  int,
  `unit_price`  decimal(30,4),
  `created_at` timestamp default current_timestamp,
  `modified_at` timestamp,
  `deleted_at` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `customs_invoice_contact_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company`  varchar(64),
  `name` varchar(64),
  `broker_name` varchar(64),
  `tax_id` varchar(64),
  `phone` varchar(64),
  `receipts_tax_id` varchar(64),
  `created_at` timestamp default current_timestamp,
  `modified_at` timestamp,
  `deleted_at` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20),
  `document_id` varchar(256),
  `name` varchar(256),
  `type` varchar(256),
  `created_at` timestamp default current_timestamp,
  `modified_at` datetime default null,
  `deleted_at` datetime default null,
  PRIMARY KEY (`id`),
  KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- July 30


alter table customer add index(sub_agent_id);

alter table customer drop index deleted_at_2;


alter table freightcom_staff
add can_manage_claims tinyint(1) default false,
add can_manage_disputes tinyint(1) default false;





drop table if exists claim;


CREATE TABLE `claim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `logged_event_id` bigint(20),
  `status` varchar(32) not null,
  `amount` decimal(30,2) not null,
  `submitted_by_id` bigint(20),
  `created_at` timestamp default current_timestamp,
  `modified_at` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- July 31

alter table charge
add taxable tinyint(1) default true;

-- Aug 1

alter table customer_order
drop color_code,
drop update_color_code;

alter table shipping_address
add save_to_address_book tinyint(1) default false;


-- Aug 4

insert into order_status (id, name,sequence) values (17, 'BOOKED', 13);
insert into order_status (id, name,sequence) values (18, 'SCHEDULED', 14);
insert into order_status (id, name,sequence) values (19, 'WAITING BORDER', 15);

update order_status set sequence=999 where id=999;


insert into service (id) values (-1);


# Sep 1

alter table customer_order
add pak_weight float default 1;


# Sep 6



DROP TABLE IF EXISTS `ups_rate_codes`;
CREATE TABLE `ups_rate_codes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(256) not null,
  `from_country` char(2),
  `to_country` char(2),
  `category` varchar(256),
  `shipping_code` varchar(8),
  `rating_code` varchar(8),
  PRIMARY KEY (`id`),
  KEY(`service_name`,`from_country`,`to_country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into ups_rate_codes (service_name, from_country, to_country, category, shipping_code, rating_code) VALUES
('UPS Standard', 'US', null, null, '11', '11'),
('UPS Worldwide Express', 'US', null, null, '07', '07'),
('UPS Worldwide Expedited', 'US', null, null, '08', '08'),
('UPS Worldwide Express Plus', 'US', null, null, '54', '54'),
('UPS Worldwide Saver', 'US', null, null, '65', '65'),

('UPS 2nd Day Air', 'US', 'US', null, '02', '02'),
('UPS 2nd Day Air A.M.', 'US', 'US', null, '59', '59'),
('UPS 3 Day Select', 'US', 'US', null, '12', '12'),
('UPS Ground', 'US', 'US', null, '03', '03'),
('UPS Next Day Air', 'US', 'US', null, '01', '01'),
('UPS Next Day Air Early', 'US', 'US', null, '14', '14'),
('UPS Next Day Air Saver', 'US', 'US', null, '13', '13'),


('UPS Expedited', 'CA', 'CA', 'Canadian domestic shipments', '02', '02'),
('UPS Express Saver', 'CA', 'CA', 'Canadian domestic shipments', '13', '13'),
('UPS 3 Day Select', 'CA', 'CA', 'Shipments originating in Canada to CA and US 48 ', '12', '12'),
('UPS 3 Day Select', 'CA', 'US', 'Shipments originating in Canada to CA and US 48 ', '12', '12'),
('UPS Access Point Economy', 'CA', 'CA', 'Canadian domestic shipments', '70', '70'),
('UPS Express', 'CA', 'CA', 'Canadian domestic shipments', '01', '01'),
('UPS Express Early', 'CA', 'CA', 'Canadian domestic shipments', '14', '14'),
('UPS Express Saver', 'CA', null, 'Canadian domestic shipments', '65', '65'),
('UPS Standard', 'CA', 'CA', 'Shipments originating in Canada (Domestic and Int’l)', '11', '11'),
('UPS Worldwide Expedited', 'CA', NULL, 'International shipments originating in Canada', '08', '08'),
('UPS Worldwide Express', 'CA', NULL, 'International shipments originating in Canada', '07', '07'),
('UPS Worldwide Express Plus', 'CA', NULL, 'International shipments originating in Canada', '54', '54'),
('UPS Worldwide Early', 'CA', 'CA', 'Shipments originating in Canada to CA and US 48', '54', '54'),
('UPS Worldwide Early', 'CA', 'US', 'Shipments originating in Canada to CA and US 48', '54', '54');




DROP TABLE IF EXISTS `ups_transit_codes`;
CREATE TABLE `ups_transit_codes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(256) not null,
  `from_country` char(2),
  `to_country` char(2),
  `category` varchar(256),
  `service_code` varchar(8),
  PRIMARY KEY (`id`),
  KEY(`service_name`,`from_country`,`to_country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into ups_transit_codes (service_name, from_country, to_country, category, service_code) VALUES
('UPS 2nd Day Air', 'US', 'US', null, '2DA'),
('UPS 2nd Day Air (Saturday Delivery)', 'US', 'US', null, '2DAS'),
('UPS 2nd Day Air A.M.', 'US', 'US', null, '2DM'),
('UPS 3 Day Select', 'US', 'US', null, '3DS'),
('UPS Ground', 'US', 'US', null, 'GND'),
('UPS Next Day Air', 'US', 'US', null, '1DA'),
('UPS Next Day Air (Saturday Delivery)', 'US', 'US', null, '1DAS'),
('UPS Next Day Air Early', 'US', 'US', null, '1DM'),
('UPS Next Day Air Early (Saturday Delivery)', 'US', 'US', null, '1DMS'),
('UPS Next Day Air Saver', 'US', 'US', null, '1DP'),

('UPS Standard', 'US', null, null, '03'),
('UPS Worldwide Expedited ', 'US', null, null, '05'),
('UPS Worldwide Express', 'US', null, null, '01'),
('UPS Worldwide Express Plus', 'US', null, null, '21'),
('UPS Worldwide Saver', 'US', null, null, '28'),


('UPS Expedited', 'CA', 'CA', null, '19'),
('UPS Express', 'CA', 'CA', null, '24'),
('UPS Express Early', 'CA', 'CA', null, '23'),
('UPS Express Saver', 'CA', 'CA', null, '20'),
('UPS Standard', 'CA', 'CA', null, '25'),


('UPS 3 Day Select', 'CA', 'US', null, '33'),
('UPS Express Early', 'CA', 'US', null, '21'),
('UPS Express Saver', 'CA', 'US', null, '28'),
('UPS Standard', 'CA', 'US', null, '03'),
('UPS Worldwide Expedited', 'CA', 'US', null, '05'),
('UPS Worldwide Express', 'CA', 'US', null, '01'),

('UPS Express Saver', 'CA', NULL, NULL, '28'),
('UPS Worldwide Expedited', 'CA', NULL, NULL, '05'),
('UPS Worldwide Express', 'CA', NULL, NULL, '01'),
('UPS Worldwide Express Plus', 'CA', NULL, NULL, '21');



update service set name=description where name is null and description is not null and carrier_id=6;


alter table order_rate_quote
add guaranteed_delivery tinyint(1) default 0;

# September 11

insert into accessorial_services (name, type) values
('Signature Required', 'env'),
('Signature Required', 'package'),
('Signature Required', 'pak');


alter table order_rate_quote
add currency char(3),
add scheduled_delivery_time varchar(32);

alter table selected_quote
add currency char(3),
add scheduled_delivery_time varchar(32),
add guaranteed_delivery tinyint(1) default 0;



update accessorial_services set rate=0;


DROP TABLE IF EXISTS `quote_tax`;
CREATE TABLE `quote_tax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(20,2) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tax_number` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `rate` double DEFAULT '0',
  `order_rate_quote_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_rate_quote_id` (`order_rate_quote_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `selected_quote_tax`;
CREATE TABLE `selected_quote_tax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(20,2) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `tax_number` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `rate` double DEFAULT '0',
  `selected_quote_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `charge_id` (`selected_quote_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--   Don't use signature required accessorial
delete from accessorial_services where name like 'signature%';


--  Sept 23

CREATE TABLE `sent_email` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_email` varchar(256) not null,
  `subject` varchar(128) DEFAULT NULL,
  `to_email` varchar(256) not null,
  `created_at` datetime DEFAULT NULL,
  `message` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



alter table customer_order add key CLAIM_ID (claim_id);

alter table charge add key DISPUTED_AT (disputed_at);


drop table if exists dispute_information;

CREATE TABLE `dispute_information` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email_addresses` varchar(1024),
  `user_id` bigint(20),
  `status` varchar(32) default 'new',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table customer_order
add dispute_information_id bigint(20) default null,
add key DISPUTE_ID (dispute_information_id);

-- insert into dispute_information (status) values ('new');

-- Sept 25

alter table `dispute_information`
modify email_addresses varchar(1024) default null;



alter table order_rate_quote
add residential_delivery_fee decimal(30,2) not null DEFAULT 0;


-- Sept 27




alter table freightcom_staff
add can_generate_invoices tinyint(1) default false,
add can_enter_payments tinyint(1) default false;

-- Oct 1

alter table customer add cc_receipt tinyint(1) default 1;


alter table customer_order add version int default 0;
alter table customer add version int default 0;

-- Oct 4

alter table logged_event add action varchar(128);

alter table order_document add description text;

-- Oct 5
alter table claim add `created_by_role_id` bigint(20);

-- Oct 7

alter table order_document add `uploaded_by_role_id` bigint(20);


-- Oct 10


create temporary table FOO select O.id from customer_order O left join selected_quote Q on Q.id=O.selected_quote_id where O.selected_quote_id is not null and  not exists (select * from service S where S.id=Q.service_id);

delete from customer_order where id in (select * from FOO);

delete from order_rate_quote;

-- Oct 16

alter table claim
add currency char(3),
add reason varchar(32);

CREATE TABLE `shipping_labels` (   `id` bigint(20) NOT NULL AUTO_INCREMENT,   `service_destination_string` varchar(255) DEFAULT NULL,   `system_num_string` varchar(255) DEFAULT NULL,   `twod_label_string` varchar(1000) DEFAULT NULL,   `dimmed_string` varchar(255) DEFAULT NULL,   `form_id` varchar(255) DEFAULT NULL,   `cod_label` mediumblob,   `origin_id` varchar(255) DEFAULT NULL,   `label_type` int(11) DEFAULT NULL,   `label` mediumblob,   `astra_label_string` varchar(255) DEFAULT NULL,   `actual_weight_string` varchar(255) DEFAULT NULL,   `ship_date_string` varchar(255) DEFAULT NULL,   `deliv_day_string` varchar(255) DEFAULT NULL,   `tracking_number` varchar(255) DEFAULT NULL,   `deliv_by_string` varchar(255) DEFAULT NULL,   `ursa_code_string` varchar(255) DEFAULT NULL,   `order_id` bigint(20) DEFAULT NULL,   `service_commitment_string` varchar(255) DEFAULT NULL,   PRIMARY KEY (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=201739 DEFAULT CHARSET=utf8;




CREATE TABLE `pallet_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20),
  `name` varchar(128),
  `length` int,
  `height` int,
  `width` int,
  `pieces` int,
  `weight` double,
  `description` text,
  `type_id` long,
  `insurance` decimal(30,2),
  `nmfc_code` varchar(128),
  `freightclass` varchar(128),
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,

  PRIMARY KEY (`id`),
  KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table pallet_template add pallet_type_id bigint(20);


-- Oct 26

alter table payable add cheque_number varchar(64);
alter table commission_payable add cheque_number varchar(64);
