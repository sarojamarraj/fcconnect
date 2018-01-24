

insert into user (id,login) values (-1, 'SYSTEM');
update user set id=0 where id=-1;
update user set enabled=0 where id=0;
insert into authorities (authority, username) values ('SYSTEM', 'SYSTEM');

alter table logged_event add index (entity_id);

alter table package add index (order_id);

alter table address_book add index (customer_id);

alter table customer_order add key (customer_id);
alter table customer_order add index (package_type_name);
alter table customer_order add index (`scheduled_ship_date`), add index (`reference_code`);
alter table customer_order add index (`expected_delivery_date`);
alter table customer_order add index (`date_of_delivery`);
alter table customer_order add index (`ship_date`);
alter table customer_order add index (`eshipper_oid`);
alter table customer_order add index (`status_id`);


alter table shipping_address add index(company);
alter table shipping_address add index(city);
alter table shipping_address add index(postal_code);



insert into agent (id) values (-1);
update agent set id=0 where id=-11;

alter table order_status add `sequence` int;
insert into order_status (id, name) values (16, 'DRAFT');

insert into order_status (id, name, `sequence`) values (15, 'INVOICED', 19);
insert into order_status (id, name, `sequence`) values (999, 'DELETED', 999);



update order_status set sequence=0 where id=16;

insert into pallet_type (id, name) values (1, 'Pallet'), (2, 'Drum');

alter table pallet add insurance decimal(30,2), add nmfc_code varchar(24), add pieces int;

alter table pallet add description text;

alter table user add index(`email`);
alter table user add index(`customer_id`);
alter table user add index(`login`);



alter table customer_order add distribution_list_id int;


CREATE TABLE `distribution_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distribution_group_id` bigint(20),
  `shipto_id` bigint(20),
  `created_at` datetime ,
  `deleted_at` datetime ,
  `updated_at` datetime ,
  PRIMARY KEY (`id`),
  KEY (`distribution_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `distribution_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20),
  `created_at` datetime ,
  `deleted_at` datetime ,
  `updated_at` datetime ,
  PRIMARY KEY (`id`),
  KEY (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


alter table distribution_group add file_name varchar(256);
alter table distribution_group add name varchar(256);
alter table distribution_address add has_error tinyint(1);
