-- Feb 4
-- Check that agents are correct


select UR.id as 'role _id', A.agent_id as 'agent_id', U.id as 'user_id', C.id, C.name, agent_name, firstname, lastname
from agent A
left join user_role UR on UR.id=A.id
left join user U on U.id=UR.user_id
left join customer C on C.sub_agent_id = A.agent_id
order by A.id;



select * from customer where id=8646

select * from agent where agent_id=279

select * from agent where agent_name like '%dwight%neal%'

select * from agent where agent_id=281




select A.agent_id, A.user_id, C.customer_id, C.name, A.agent_name
from wo_sales_agent A
left join customer C on C.sub_agent_id=A.agent_id;


select distinct parent_sales_agent_id from agent



select agent_id, U.login, U.id,parent_sales_agent_id from agent left join user_role UR on UR.id=agent.id left join user U on U.id=UR.user_id where parent_sales_agent_id is not null and (select count(*) from customer where sub_agent_id=agent_id) > 3

select agent_id, U.login, U.id,parent_sales_agent_id from agent left join user_role UR on UR.id=agent.id left join user U on U.id=UR.user_id
where exists (select * from agent AX where AX.parent_sales_agent_id = agent.agent_id) and (select count(*) from customer where sub_agent_id=agent_id) > 3


select agent_id, U.login, U.id,parent_sales_agent_id, agent_name from agent left join user_role UR on UR.id=agent.id left join user U on U.id=UR.user_id
where exists (select * from agent AX where AX.parent_sales_agent_id = agent.agent_id)


select user0_.id as id1_167_, user0_.cell as cell2_167_, user0_.created_at as created_3_167_, user0_.deleted as deleted4_167_, user0_.deleted_at as deleted_5_167_, user0_.email as email6_167_, user0_.enabled as enabled7_167_, user0_.fax as fax8_167_, user0_.first_login_complete as first_lo9_167_, user0_.first_time_login as first_t10_167_, user0_.firstname as firstna11_167_, user0_.lastname as lastnam12_167_, user0_.login as login13_167_, user0_.password as passwor14_167_, user0_.password_expiry as passwor15_167_, user0_.phone as phone16_167_, user0_.sub_type as sub_typ17_167_, user0_.type as type18_167_, user0_.updated_at as updated19_167_ from user user0_ where ( user0_.deleted_at is null) and (exists (select userrole1_.id from user_role userrole1_ left outer join admin userrole1_1_ on userrole1_.id=userrole1_1_.id left outer join agent userrole1_2_ on userrole1_.id=userrole1_2_.id left outer join customer_admin userrole1_3_ on userrole1_.id=userrole1_3_.id left outer join customer_staff userrole1_4_ on userrole1_.id=userrole1_4_.id left outer join freightcom_staff userrole1_5_ on userrole1_.id=userrole1_5_.id cross join agent agent2_ inner join user_role agent2_1_ on agent2_.id=agent2_1_.id where userrole1_.role_name='agent' and userrole1_.user_id=user0_.id and agent2_.parent_sales_agent_id=290))

select user0_.id, login,
(select userrole1_.id from user_role userrole1_ left outer join admin userrole1_1_ on userrole1_.id=userrole1_1_.id left outer join agent userrole1_2_ on userrole1_.id=userrole1_2_.id left outer join customer_admin userrole1_3_ on userrole1_.id=userrole1_3_.id left outer join customer_staff userrole1_4_ on userrole1_.id=userrole1_4_.id left outer join freightcom_staff userrole1_5_ on userrole1_.id=userrole1_5_.id cross join agent agent2_ inner join user_role agent2_1_ on agent2_.id=agent2_1_.id where userrole1_.role_name='agent' and userrole1_.user_id=user0_.id and agent2_.parent_sales_agent_id=290)
from user user0_ where ( user0_.deleted_at is null) and (exists (select userrole1_.id from user_role userrole1_ left outer join admin userrole1_1_ on userrole1_.id=userrole1_1_.id left outer join agent userrole1_2_ on userrole1_.id=userrole1_2_.id left outer join customer_admin userrole1_3_ on userrole1_.id=userrole1_3_.id left outer join customer_staff userrole1_4_ on userrole1_.id=userrole1_4_.id left outer join freightcom_staff userrole1_5_ on userrole1_.id=userrole1_5_.id cross join agent agent2_ inner join user_role agent2_1_ on agent2_.id=agent2_1_.id where userrole1_.role_name='agent' and userrole1_.user_id=user0_.id and agent2_.parent_sales_agent_id=290))



select userrole1_.id ,  agent2_.parent_sales_agent_id from user_role userrole1_ left outer join admin userrole1_1_ on userrole1_.id=userrole1_1_.id left outer join agent userrole1_2_ on userrole1_.id=userrole1_2_.id left outer join customer_admin userrole1_3_ on userrole1_.id=userrole1_3_.id left outer join customer_staff userrole1_4_ on userrole1_.id=userrole1_4_.id left outer join freightcom_staff userrole1_5_ on userrole1_.id=userrole1_5_.id cross join agent agent2_ inner join user_role agent2_1_ on agent2_.id=agent2_1_.id where userrole1_.role_name='agent' and userrole1_.user_id=16 and agent2_.parent_sales_agent_id=290







drop function if exists check_subagent;
create function check_subagent(inParentId bigint unsigned, ancestorId bigint unsigned)
RETURNS tinyint(1) unsigned DETERMINISTIC READS SQL DATA
BEGIN
  DECLARE parentId bigint unsigned;
  DECLARE  resultValue tinyint(1) unsigned;

  SET parentId = inParentId;

  IF parentId = ancestorId THEN
    SET resultValue = true;
  ELSE
    SET resultValue = false;

    WHILE not resultValue and parentId is not null DO
       SET parentId = (select `parent_sales_agent_id"` from `agent` where `id`=parentId);
       SET resultValue = (parentId = ancestorId);
    END WHILE;
  END IF;

  RETURN resultValue;
END;


select count(user0_.id) as col_0_0_ from user user0_
where ( user0_.deleted_at is null)
and (exists (select userrole1_.id from user_role userrole1_
left outer join admin userrole1_1_ on userrole1_.id=userrole1_1_.id
left join agent userrole1_2_ on userrole1_.id=userrole1_2_.id
left outer join customer_admin userrole1_3_ on userrole1_.id=userrole1_3_.id
left outer join customer_staff userrole1_4_ on userrole1_.id=userrole1_4_.id
left outer join freightcom_staff userrole1_5_ on userrole1_.id=userrole1_5_.id
     where userrole1_.role_name='AGENT' and userrole1_.user_id=user0_.id
           and check_subagent(agent.parent_sales_agent_id, 290)=1))






select O.id, O.customer_id, I.customer_id, L.order_id, L.elt, L.position,
(select group_concat() from order_invoice_comments C where C.id=I.id)
from customer_order O
left join order_invoice_workorder L on L.elt=O.id
left join invoice I on I.id=L.order_id
