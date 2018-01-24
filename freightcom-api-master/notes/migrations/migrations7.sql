

alter table order_invoice_workorder
add key (`elt`),
add key (`order_id`);


alter table markup
modify column min_amount Decimal(30,2),
modify column max_amount Decimal(30,2),
modify column fixed_amount Decimal(30,2),
modify column fraction double;


alter table credit
modify column amount Decimal(30,2),
add amount_remaining Decimal(30,2);



drop table if exists currency_exchange;

CREATE TABLE `currency_exchange` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from` char(3) not null,
  `to` char(3) not null,
  `rate` double,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UNIQUE KEY (`from`, `to`, `deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `currency_exchange` (id, `from`, `to`, rate, updated_at) values (1, 'CAN', 'USD', .76, now()), (2, 'USD', 'CAN', 1.34, now());


update customer_order set scheduled_pick_up_id=null where scheduled_pick_up_id=0;


alter table pickup
 add  `created_at` datetime DEFAULT NULL,
 add  `deleted_at` datetime DEFAULT NULL,
 add  `updated_at` datetime DEFAULT NULL;



alter table `currency_exchange`
add updated_by varchar(64);


alter table `invoice`
add viewed_at datetime DEFAULT NULL;


alter table `customer_order`
drop `req_delivery_date`;

alter table `invoice`
add key (order_id);





CREATE TABLE `applied_credit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint(20) NOT NULL,
  `credit_id` bigint(20) NOT NULL,
  `sequence` int NOT NULL,
  `amount` DECIMAL(30,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`invoice_id`),
  CONSTRAINT UNIQUE KEY (`invoice_id`, `credit_id`, `deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




alter table charge add key (order_id);


-- Feb 23
alter table logged_event add key (`entity_type`);
alter table logged_event add key (`created_at`);

-- order_accounts_receivable -> payment_transaction

alter table payment_transaction drop order_id;
alter table payment_transaction add amount DECIMAL(30,2) NOT NULL default 0;

CREATE TABLE `applied_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint(20) NOT NULL,
  `payment_transaction_id` bigint(20) NOT NULL,
  `sequence` int NOT NULL,
  `amount` DECIMAL(30,2) NOT NULL default 0,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`invoice_id`),
  CONSTRAINT UNIQUE KEY (`invoice_id`, `payment_transaction_id`, `deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table credit
modify column `amount` decimal(30,2) not null default 0,
modify column `amount_remaining` decimal(30,2) not null default 0;

alter table customer_order add key (`agent_id`);

update customer_order set agent_id=null where agent_id is not null && not exists (select * from agent A where A.id=customer_order.agent_id) limit 1;


select id from customer_order where agent_id is not null && not exists (select * from agent A where A.id=customer_order.agent_id);

-- Feb 25

alter table charge modify amount decimal(30,2);

-- Feb 27

alter table customer_order add object_version bigint;

update invoice set created_at = date_generated where date_generated is not null;

alter table invoice
change due_date due_at datetime,
drop date_generated,
add effective_at datetime;

update customer_order set ship_to_id=null where not exists (select * from shipping_address where shipping_address.id=ship_to_id and shipping_address.deleted_at is null);





alter table customer
add auto_invoice tinyint(1) default 0;

-- Feb 28

alter table charge
modify total decimal(30,2);

alter table invoice add sent_at datetime;

-- Mar 1

alter table charge add key (`invoice_id`);

alter table charge drop total;
alter table charge change total_value total decimal(30,2);

alter table charge drop description,
  change name description varchar(255);


-- Mar 1 b

create temporary table charge_invoice as
select C.id as 'charge_id', OI.order_id as 'invoice_id' from charge C left join customer_order O on O.id=C.order_id left join order_invoice_workorder OI on OI.elt=C.order_id;

alter table charge_invoice add key (charge_id);

update charge set invoice_id=(select invoice_id from charge_invoice where charge_invoice.charge_id=charge.id limit 1) where charge.order_id >  500000;



alter table customer_order add key (scheduled_ship_date);


-- Mar 2

alter table pickup add contact_email varchar(512);

-- Mar 3 after work

alter table customer_order modify customer_id bigint(20);


DROP TABLE IF EXISTS `super_admin`;
CREATE TABLE `super_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table customer_order add key(deleted_at);
alter table customer add key(deleted_at);
alter table user add last_login datetime;

update user set last_login=now() where first_login_complete;
alter table user drop first_time_login, drop first_login_complete;

-- Mar 4
DROP TABLE IF EXISTS `system_log`;

CREATE TABLE `system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20),
  `object_id` bigint(20),
  `action` varchar(255),
  `comments` text,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table customer_order modify agent_id bigint(20);

alter table selected_quote add `deleted_at` datetime DEFAULT NULL;


-- Mar 6

alter table customer_order drop status;

alter table pickup add delivery_instructions text, add delivery_close_time varchar(16);

alter table pickup add carrier_confirmation varchar(255);

alter table charge
add accessorial_id bigint(20),
add tax1 decimal(30,2),
add tax2 decimal(30,2),
add tax3 decimal(30,2),
add tax1_name varchar(32),
add tax2_name varchar(32),
add tax3_name varchar(32);

alter table charge
add cost decimal(30,2);

alter table invoice add index (customer_id);

-- Mar 8

alter table user
add key (login),
add key (email);

alter table customer_staff
add key(customer_id);

alter table customer_admin
add key(customer_id);


alter table payment_transaction
add payment decimal(30,2),
add payment_type varchar(128),
add credit decimal(30,2);

-- March 20

update credit_card set number=cc_number, expiry_year=cc_expiry_year, expiry_month=cc_expiry_month;


alter table credit_card drop cc_number, drop cc_expiry_year, drop cc_expiry_month;

-- March 22
alter table credit_card add is_default tinyint;


-- March 23

create table carrier_invoice (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `carrier_id` bigint(20),
  `document_id` text,
  `processed` tinyint,
  `file_name` varchar(128),
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`carrier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- March 24

alter table `charge`
add reconciled tinyint(1),
add carrier_id bigint(20);

alter table customer_order
drop ship_to_address_id,
drop trailer_reference1,
drop trailer_reference2,
drop shipment_error,
drop bill_to_id,
drop trailer_weight2,
drop trailer_weight1,
drop charge_comments,
drop tax_type,
drop guaranteed_dropoff_at,
drop customs_currency,
drop customs_contact_name,
drop order_batch_id,
drop ship_from_address_id,
drop to_airport_code,
drop from_datetime,
drop rate_error,
drop job_pickup_date,
drop to_location_type,
drop customs_broker_name,
drop bill_to,
drop description,
drop fuel_perc_charge,
drop bill_to_contact,
drop customs_fax_number,
drop trucking_prealert_id,
drop stackable,
drop trailer_seal_number2,
drop trailer_seal_number1,
drop tax_rate_id,
drop shipment_type,
drop cod_cheque_date,
drop from_location_type,
drop trailer_carton_count2,
drop trailer_carton_count1,
drop cod_payable_to,
drop pod_given_to,
drop trailer_number,
drop quoted_by,
drop pickup_time,
drop batch_id,
drop job_radio,
drop from_airport_code,
drop tax_type2,
drop customs_phone_number,
drop trailer_dimension_weight,
drop bol_number,
drop job_quantity,
drop to_datetime,
drop fuel_perc_cost,
drop quoted_amount,
drop requested_pickup_at,
drop job_deliv_deadline,
drop delivery_prealert_id,
drop job_weight,
drop payment_type,
drop customs_value,
drop job_content_descr,
drop cod_currency,
drop job_quantity_type;


alter table customer_order
drop req_delivery_date;
