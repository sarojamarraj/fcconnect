DROP TABLE IF EXISTS `accessorial_services`;
CREATE TABLE `accessorial_services` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `description` text,
  `type` varchar(64),
  `rate` decimal(30,2),
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`name`),
  KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `accessorial_services`
(`id`, `name`, `description`, `type`, `rate`)
values
('1','Pickup Tailgate','','pallet','500'),
('2','Delivery Tailgate','','pallet','500'),
('3','Inside Pickup','','pallet','500'),
('4','Saturday Pickup','','pallet','500'),
('5','Saturday Delivery','','pallet','500'),
('6','Appointment Pickup','','pallet','500'),
('7','Residential Pickup','','pallet','500'),
('8','Residential Delivery','','pallet','500'),
('9','Excess Length','','pallet','500'),
('10','Sort and Segregate','','pallet','500'),
('11','Single Shipment','','pallet','500'),
('12','Construction Site','','pallet','500'),
('13','Homeland Security','','pallet','500'),
('14','Military Base Delivery','','pallet','500'),
('15','Limited Access','','pallet','500'),
('16','Inside Delivery','','pallet','500'),
('17','Appointment Delivery','','pallet','500'),
('18','Customs','','pallet','500'),
('19','Cross Border Fee','','pallet','500'),
('20','Pier Charge','','pallet','500'),
('21','In-bond Freight','','pallet','500'),
('22','Notification Fee','','pallet','500'),
('23','Heated Service','','pallet','500'),
('24','Saturday Pickup','','env','500'),
('25','Saturday Delivery','','env','500'),
('26','Hold For Pickup','','env','500'),
('27','Residential Pickup','','env','500'),
('28','Residential Delivery','','env','500'),
('29','Saturday Pickup','','pak','500'),
('30','Saturday Delivery','','pak','500'),
('31','Hold For Pickup','','pak','500'),
('32','Residential Pickup','','pak','500'),
('33','Residential Delivery','','pak','500'),
('34','Saturday Pickup','','package','500'),
('35','Saturday Delivery','','package','500'),
('36','Hold For Pickup','','package','500'),
('37','Residential Pickup','','package','500'),
('38','Residential Delivery','','package','500');

CREATE TABLE IF NOT EXISTS `order_accessorials` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20),
  `accessorial_id` bigint(20),
  PRIMARY KEY (`id`),
  KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



alter table customer_order add key (`status_id`);
alter table shipping_address add address_book_id bigint(20);
