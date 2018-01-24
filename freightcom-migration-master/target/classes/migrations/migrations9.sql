-- April 3

alter table `charge` modify `subtotal` decimal(30,2);


alter table `charge`
add commission decimal(30,2);

alter table charge drop foreign key charge_ibfk_2;

alter table `charge`
drop tax1,
drop tax1_name,
drop tax2,
drop tax2_name,
drop tax3,
drop tax3_name,
drop charge_tax_id;

alter table charge_tax modify `amount` decimal(20,2);

alter table charge
drop qty,
add quantity int default 1;

alter table charge_tax
drop fraction,
add rate double default 0;

alter table charge_tax
add charge_id bigint(20),
add index(charge_id);


-- April 4

alter table customer
add auto_charge varchar(32) default null,
add term varchar(32) default 'never';

alter table carrier add term varchar(32) default 'monthly';

alter table charge
add payable_id bigint(20);


DROP TABLE IF EXISTS `payable`;

CREATE TABLE `payable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `due_at` datetime DEFAULT NULL,
  `paid_at` datetime DEFAULT NULL,
  `carrier_id` bigint(20),
  `status` varchar(20),
  `currency` varchar(20),
  PRIMARY KEY (`id`),
  INDEX (`carrier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- April 5

alter table commission_payable
add paid_amount decimal(30,2);

alter table commission_payable
add paid_at datetime DEFAULT NULL;

alter table commission_payable
add due_at datetime DEFAULT NULL;

alter table payable
add paid_amount decimal(30,2);

alter table charge
add commission_payable_id bigint(20);

alter table charge
add index(commission_payable_id),
add index(payable_id);


alter table charge
add index(carrier_id);

--


DROP TABLE IF EXISTS `commission_payable`;

CREATE TABLE `commission_payable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` varchar(128) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `paid_amount` decimal(30,2) DEFAULT NULL,
  `paid_at` datetime DEFAULT NULL,
  `due_at` datetime DEFAULT NULL,
  INDEX(`agent_id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `charge_commission`;

CREATE TABLE `charge_commission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `charge_id` bigint(20) DEFAULT NULL,
  `commission_payable_id` bigint(20) DEFAULT NULL,
  `amount` decimal(30,2) default 0,
  `currency` varchar(128) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX(`charge_id`),
  INDEX(`commission_payable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table customer_order add index(`agent_id`);


alter table charge
drop commission_payable_id;


alter table charge
change total_value_charge charge decimal(30,2);

alter table charge
drop amount;

-- April 6

alter table charge
add commission_calculated_at datetime;

alter table charge
add reported_at datetime;

-- April 7

alter table payment_transaction
add credit_id bigint(20);


-- April 10

alter table charge
add agent_id bigint(20),
add index(agent_id);

alter table agent
add term varchar(32);

-- April 11

alter table charge
drop reconciled,
add reconciled_at datetime;


drop function if exists last_comment;
DELIMITER $$;
create function last_comment(order_id bigint unsigned)
RETURNS varchar(750) DETERMINISTIC READS SQL DATA
BEGIN
  RETURN (select `comment` from logged_event where `entity_id`=order_id and `entity_type`='order' order by id desc limit 1);
END$$;


DELIMITER ;


alter table customer_order add custom_work_order tinyint(1);

-- April 12

alter table customer
add past_due_action varchar(32);


alter table customer
change net_terms invoice_term int(10);

alter table customer
change net_terms_warning invoice_term_warning int(10);

alter table customer
change pod_reqd shipping_pod_required tinyint(1);

alter table customer
add shipping_nmfc_required tinyint(1);


-- April 12 b

alter table customer
add allow_new_orders tinyint(1) default true;

alter table customer
modify column auto_invoice varchar(32);

update customer set auto_invoice='ON_DELIVERY' where auto_invoice = 1;
update customer set auto_invoice=null where auto_invoice = 0;
update customer set auto_charge='NEVER' where auto_charge = 0;

update customer set past_due_action='DO_NOTHING' where past_due_action = 0;




drop function if exists commission_total_amount;
DELIMITER $$;
create function commission_total_amount(payable_id bigint unsigned)
RETURNS decimal(30,2) DETERMINISTIC READS SQL DATA
BEGIN
  RETURN (select IFNULL(sum(CC.amount),0) from charge_commission CC where CC.commission_payable_id=payable_id);
END$$;


DELIMITER ;


-- April 18

alter table logged_event modify entity_id bigint(20);



-- April 18 b

insert into accessorial_services (id,name) values
(-1, 'Other'),
(-2, 'Base Fee'),
(-3, 'Fule Fee'),
(-4, 'Insurance');


-- April 19


drop function if exists agent_name;
DELIMITER $$;
create function agent_name(agentId bigint unsigned)
RETURNS VARCHAR(256) DETERMINISTIC READS SQL DATA
BEGIN
   DECLARE  resultValue varchar(256);

   IF agentId = null THEN
     SET resultValue = null;
   ELSE
     SET resultValue = (select IF(A.agent_name is null or A.agent_name=' ', concat(U.firstname, ' ', U.lastname), A.agent_name) from agent A left join user_role UR on UR.id=A.id left join user U on U.id=UR.user_id where A.id=agentId);
   END IF;

   RETURN resultValue;
END$$;


DELIMITER ;


-- April 19


drop function if exists user_role_phone;
DELIMITER $$;
create function user_role_phone(roleId bigint unsigned)
RETURNS VARCHAR(256) DETERMINISTIC READS SQL DATA
BEGIN
   DECLARE  resultValue varchar(256);

   IF roleId = null THEN
     SET resultValue = null;
   ELSE
     SET resultValue = (select U.phone from user_role UR left join user U on U.id=UR.user_id where UR.id=roleId);
   END IF;

   RETURN resultValue;
END$$;


DELIMITER ;

-- April 20

update carrier set term='MONTHLY' where term='monthly';

update agent set term='MONTHLY' where term='monthly';

alter table payment_transaction add reference varchar(128);



--

alter table agent
add allow_new_order tinyint(1) default 1,
add view_invoices tinyint(1) default 1;

--

CREATE TABLE `currency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(3),
  `created_at` datetime,
  PRIMARY KEY (`id`),
  KEY (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


insert into `currency` (`name`) values ('CAD'), ('USD');


DROP TABLE IF EXISTS `tax_definition`;
CREATE TABLE `tax_definition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `short_name` varchar(128),
  `name` varchar(128),
  `rate` double,
  `tax_id` varchar(128),
  `created_at` datetime,
  PRIMARY KEY (`id`),
  KEY (`short_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

insert into `tax_definition` (`short_name`, `name`, `rate`, `tax_id`, `created_at`) values ('HST', 'HST - 3411211', 0.13, '3411211', '2017-04-01');


DROP TABLE IF EXISTS `applicable_taxes`;
CREATE TABLE `applicable_taxes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20),
  `tax_definition_id` bigint(20),
  `position` int default 0,
  `created_at` datetime,
  PRIMARY KEY (`id`),
  KEY (`customer_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


insert into `applicable_taxes` (customer_id, tax_definition_id, position, created_at)
select id as customer_id, 1 as tax_definition_id, 0 as position, '2017-04-20' as created_at from customer where deleted_at is null;

-- Apr 24



drop function if exists root_agent_id;
DELIMITER $$;
create function root_agent_id(agentId BIGINT(20))
RETURNS BIGINT(20) DETERMINISTIC READS SQL DATA
BEGIN
   DECLARE resultValue BIGINT(20);
   DECLARE parentId BIGINT(20);

   SET resultValue = null;

   WHILE resultValue is null and agentId is not null DO
      SET parentId = (select `parent_sales_agent_id` from `agent` where `id`=agentId);

      IF parentId is null THEN
        SET resultValue = agentId;
      ELSE
        SET agentId = parentId;
      END IF;
   END WHILE;

   RETURN resultValue;
END$$;

DELIMITER ;


alter table charge_commission add agent_id bigint(20);


-- Apr 25

alter table charge change column carrier_id service_id bigint(20);


-- Apr 25

alter table shipping_address add email_notification tinyint(1) default 0;

-- May 3 pm
alter table alert add key (object_id);
