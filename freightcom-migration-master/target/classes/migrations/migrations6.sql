
alter table order_status add sequence int;



update order_status set sequence=1 where name = 'PREDISPATCHED';
update order_status set sequence=2 where name = 'READY FOR DISPATCH';
update order_status set sequence=3 where name = 'READY FOR SHIPPING';
update order_status set sequence=4 where name = 'IN TRANSIT';
update order_status set sequence=5 where name = 'DELIVERED';
update order_status set sequence=6 where name = 'CANCELLED';
update order_status set sequence=7 where name = 'EXCEPTION';
update order_status set sequence=8 where name = 'MISSED PICKUP';
update order_status set sequence=9 where name = 'READY TO INVOICE';
update order_status set sequence=10 where name = 'QUOTED';
update order_status set sequence=11 where name = 'INVOICED';
update order_status set sequence=12 where name = 'DRAFT';
update order_status set sequence=13 where name = 'DELETED';


update customer_order set invoice_status=1,status_id=3 where status_id=7 limit 6;
delete from order_status where id=7 or id=15;




-- Feb 4


create temporary table AGENT
select UR.id, UR.user_id, agent_id
from agent A left join user_role UR on UR.id=A.id
where UR.user_id != -1;

--

update customer set sub_agent_id = (select id from AGENT where AGENT.agent_id=sub_agent_id) where sub_agent_id is not null;
update customer_order set agent_id = (select id from AGENT where AGENT.agent_id=customer_order.agent_id) where agent_id is not null and agent_id != 0 and agent_id != -1;

update agent set parent_sales_agent_id = (select id from AGENT where AGENT.agent_id=parent_sales_agent_id) where parent_sales_agent_id is not null;






drop function if exists check_subagent;
DELIMITER $$;
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
       SET parentId = (select `parent_sales_agent_id` from `agent` where `id`=parentId);
       SET resultValue = (parentId = ancestorId);
    END WHILE;
  END IF;

  RETURN resultValue;
END$$;


DELIMITER ;


alter table alert
add user_role_id bigint(20),
add from_user_id bigint(20);



drop table wo_orders_charges;


create table A select * from agent;

update agent set parent_sales_agent_id=null where parent_sales_agent_id is not null and not exists (select * from A where A.id=parent_sales_agent_id);

