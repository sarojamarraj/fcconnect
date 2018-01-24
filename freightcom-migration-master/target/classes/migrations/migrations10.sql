-- May 3 pm


alter table customer
drop affiliate,
drop aff_login_page,
drop flat_charge,
drop comm_pallet_deduct_affiliate_fee,
drop show_news,
drop fedex_us_tariff,
drop api_username,
drop comm_wo_op_expense,
drop external_affiliate_num,
drop api_password,
drop insurance_percentage,
drop flat_cost3,
drop comm_courier_deduct_affiliate_fee,
drop ref_code_mandatory,
drop preferred_carrier_id,
drop cc_receipt,
drop flat_cost2,
drop flat_cost1,
drop commercial_invoice,
drop ref_sort_billing,
drop comm_pallet_op_expense,
drop opt_pcs_wgt,
drop comm_courier_deduct_tran_fee,
drop insurance_min,
drop insurance_preference,
drop comm_pallet_deduct_tran_fee,
drop aff_reg_page,
drop credit_code,
drop affiliate_kick_back,
drop allow_predispatch,
drop comm_courier_op_expense,
drop flat_charge_final,
drop flat_rate,
drop affiliate_id;




DROP TABLE IF EXISTS `stored_credit_card`;

CREATE TABLE `stored_credit_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_key` varchar(128) DEFAULT NULL,
  `response_code` varchar(128) DEFAULT NULL,
  `message` varchar(128) DEFAULT NULL,
  `transaction_date` varchar(128) DEFAULT NULL,
  `transaction_time` varchar(128) DEFAULT NULL,
  `complete` tinyint(1) DEFAULT NULL,
  `timed_out` tinyint(1) DEFAULT NULL,
  `success`  tinyint(1) DEFAULT NULL,
  `payment_type` varchar(128) DEFAULT NULL,
  `customer_id` varchar(128) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `masked_card` varchar(128) DEFAULT NULL,
  `note` varchar(128) DEFAULT NULL,
  `expiry_date` varchar(128) DEFAULT NULL,
  `crypt_type` varchar(128) DEFAULT NULL,
  `street_number` varchar(128) DEFAULT NULL,
  `street_name` varchar(128) DEFAULT NULL,
  `postal_code` varchar(128) DEFAULT NULL,
  `card_type` varchar(128),
  `created_at` datetime,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table credit_card
add stored_credit_card_id bigint(20);


alter table credit_card
drop number,
drop cvc;


-- May 10

alter table service add `term` varchar(32) DEFAULT 'MONTHLY';

update service set term=(select term from carrier where carrier.id=service.carrier_id) where carrier_id is not null;


alter table carrier_invoice
change carrier_id carrier_service_id bigint;


update carrier_invoice set carrier_service_id = (select id from service where service.id=carrier_service_id limit 1);


alter table payable
change carrier_id service_id bigint;

update payable set service_id = (select service_id from charge where charge.payable_id=payable.id limit 1);


-- May 17



DROP TABLE IF EXISTS `excluded_service`;
CREATE TABLE `excluded_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20),
  `service_id` bigint(20),
  `created_at` datetime,
  `updated_at` datetime,
  `deleted_at` datetime,
  PRIMARY KEY (`id`),
  KEY (`customer_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


alter table customer change column package_preference package_preference_id bigint(20);

