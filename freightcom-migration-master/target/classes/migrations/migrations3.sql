

alter table user_role add role_name varchar(32);
alter table user_role add key (user_id);
alter table user_role add key (role_name);


update user_role UR set role_name = IF((select group_concat(A.authority) from user U left join authorities A on A.username = U.login where U.id=UR.user_id) like '%admin%' AND (select name from role R where R.id=UR.role_id) not like '%admin%',
CONCAT(UCASE((select name from role R where R.id=UR.role_id)), '_', 'ADMIN'), UCASE((select name from role R where R.id=UR.role_id)));



alter table user_role drop role_id;
drop table role;

alter table user_role
add   `created_at` datetime DEFAULT NULL,
add  `deleted_at` datetime DEFAULT NULL,
add  `updated_at` datetime DEFAULT NULL;


alter table customer_staff
add `perm` varchar(128);


update user_role set role_name='ADMIN' where role_name in ('SUPREMEADMIN', 'SUPERADMIN', 'SUBADMIN');

update user_role set role_name='SALES' where role_name in ('SALES_ADMIN');


update user_role set role_name='FREIGHTCOM_STAFF' where role_name like '%OPERATOR%';

update user_role set role_name='AGENT' where role_name = 'SALES';


alter table customer_staff
drop created_at,
drop updated_at,
drop deleted_at;


alter table customer_staff drop user_id;
alter table customer_staff drop customer_id;
alter table customer_staff add customer_id bigint(20);

insert into customer_staff select UR.id as 'id', null as 'perm', IF(U.customer_id=0, null, U.customer_id) as 'customer_id' from user_role UR left join user U on U.id = UR.user_id where UR.role_name='CUSTOMER';


alter table customer_admin
drop created_at,
drop updated_at,
drop deleted_at,
drop user_id;

alter table customer_admin
drop key customer_id,
drop customer_id,
add customer_id bigint(20);

insert into customer_admin select UR.id as 'id', IF(U.customer_id=0, null, U.customer_id) as 'customer_id' from user_role UR left join user U on U.id = UR.user_id where UR.role_name='CUSTOMER_ADMIN';

alter table admin
drop key user_id,
drop user_id,
drop created_at,
drop updated_at,
drop deleted_at;

insert into admin select UR.id as 'id' from user_role UR left join user U on U.id = UR.user_id where UR.role_name='ADMIN';



set @rank=(select max(id) from user_role) + 1;
create temporary table new_agent_ids (select id, @rank:=@rank + 1 as 'rank' from agent);

update agent A, new_agent_ids I set A.id = I.rank where I.id=A.id;

update agent A set id=(select id from user_role UR where UR.user_id=A.user_id and UR.role_name='AGENT') where exists (select * from user_role UR2 where UR2.user_id=A.user_id and UR2.role_name='AGENT');

insert into user_role select id, user_id, 'AGENT' as role_name, created_at, deleted_at, updated_at from agent A where not exists (select * from user_role UR where UR.id=A.id);



alter table freightcom_staff
drop key user_id,
drop user_id,
drop created_at,
drop updated_at,
drop deleted_at;






DROP TABLE IF EXISTS `configuration`;
CREATE TABLE `configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(256) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `welcome_banner` TEXT DEFAULT NULL,
  `welcome_title` TEXT DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table customer_order
add distribution_list_id bigint(20);


alter table pallet
add description text;

alter table pallet
add `insurance` decimal(30,2) DEFAULT NULL;

alter table pallet
add   `nmfc_code` varchar(24) DEFAULT NULL;

alter table pallet
add  `pieces` int(11) DEFAULT NULL;

alter table customer_order add key(status_id);

update customer set sub_agent_id=null where sub_agent_id=0;

update customer_order set agent_id=null where agent_id=0;
