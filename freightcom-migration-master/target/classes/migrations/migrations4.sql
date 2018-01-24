DROP TABLE IF EXISTS `order_rate_quote`;
CREATE TABLE `order_rate_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `carrier_id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL,
  `transit_days` int,
  `total_charges` DECIMAL(30,2),
  `base_fee` DECIMAL(30,2),
  `fuel_fee` DECIMAL(30,2),
  `insurance` DECIMAL(30,2),
  `cross_border_fee` DECIMAL(30,2),
  PRIMARY KEY (`id`),
  KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into order_status (id, name) values (999, 'DELETED');


delete from user_role where role_name is null;

update user_role set role_name='CUSTOMER_STAFF' where role_name = 'CUSTOMER';

DROP TABLE IF EXISTS `system_property`;
CREATE TABLE `system_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `data` text,
  PRIMARY KEY (`id`),
  KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into system_property (name, data) values ('welcome_title', 'This be the title'), ('welcome_message', 'this be the message');


alter table alert add user_id bigint(20), add key (user_id);



DROP TABLE IF EXISTS `selected_quote`;
CREATE TABLE `selected_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_id` bigint(20) NOT NULL,
  `transit_days` int,
  `total_charges` DECIMAL(30,2),
  `base_fee` DECIMAL(30,2),
  `fuel_fee` DECIMAL(30,2),
  `insurance` DECIMAL(30,2),
  `cross_border_fee` DECIMAL(30,2),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table customer_order add selected_quote_id bigint(20);


update customer_order set selected_quote_id = id;

insert into selected_quote (id, service_id, transit_days, total_charges, base_fee, fuel_fee, insurance, cross_border_fee)
select id, service_id, transit_days, quoted_total_charge, quoted_base_charge, quoted_fuel_cost, 0, cross_border_fee from customer_order;


update customer_order O set package_type_id = (select id from package_type PT where PT.description = O.package_type_name) where package_type_id is null and exists (select id from package_type PT where PT.description = O.package_type_name);




update customer_order O set package_type_id = (select id from package_type PT where PT.name = O.package_type_name) where package_type_id is null and exists (select id from package_type PT where PT.name = O.package_type_name);

update customer_order O set package_type_id=1 where package_type_name='envelope' and package_type_id is null;

update customer_order O set package_type_id=2 where package_type_name='courier pak' and package_type_id is null;

alter table customer_order drop package_type_name;


update customer_order set carrier_id= null where not exists (select * from carrier where carrier.id=customer_order.carrier_id);

