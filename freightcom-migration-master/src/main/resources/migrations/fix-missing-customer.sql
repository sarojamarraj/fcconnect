delete from customer where id in (13955, 13925, 13924, 13103, 10970, 10969, 10950, 10949, 10939, 10938, 10937);

delete from customer where name is null and id >= 10730;

delete from customer where name like 'customer_%' or name like 'customer_2_name%';

delete from agent where not exists (select * from user_role where user_role.id=agent.id);

delete from customer where name like 'customer_name%' or name like 'c0_%' or name like 'c2_%';

delete from user where login like 'agent\_%' or login like 'parent-agent%' or login like 'customer\_user%' or login like 'hex01\_%' or login like 'staff\_%' or login like 'customerTest\_%' or login like 'a\_%' or login like 'b\_%' or login like 'george.%' or login is null or login='' or login=' ';


update customer_order set ship_from_id=null where not exists (select * from shipping_address where shipping_address.id=ship_from_id and shipping_address.deleted_at is null);

update customer_order set agent_id=null where agent_id is not null and not exists (select * from agent where agent.id=agent_id);


update customer_order set customer_id=null where not exists (select * from customer where customer.id=customer_id and customer.deleted_at is null);



update invoice set customer_id=null where not exists (select * from customer where customer.id=customer_id and customer.deleted_at is null);


delete from customer_staff where  not exists (select * from customer where customer.id=customer_id and customer.deleted_at is null);


update customer set sub_agent_id = null where not exists (select * from customer_staff where customer_staff.id=sub_agent_id) and not exists (select * from customer_admin where customer_admin.id=sub_agent_id) ;


delete from user_role where role_name = 'customer_staff' and not exists (select * from customer_staff where customer_staff.id=user_role.id);

delete from user_role where role_name = 'customer_admin' and not exists (select * from customer_admin where customer_admin.id=user_role.id);


delete from user_role where not exists (select * from user where user.id=user_role.user_id and deleted_at is null);


update invoice set deleted_at = now() where customer_id is null;


update agent set parent_sales_agent_id = null where parent_sales_agent_id is not null and not exists (select * from user_role where id=parent_sales_agent_id);


update charge set agent_id=null where not exists (select * from agent where agent.id=charge.agent_id);


delete from payable where not exists (select * from charge where charge.payable_id = payable.id);

delete from charge_commission where not exists (select * from charge where charge.id = charge_commission.charge_id);

update agent set address_id=null where address_id is not null and not exists (select * from shipping_address where shipping_address.id=address_id);

update service set billing_address_id = null where billing_address_id is not null and (billing_address_id = 0 or not exists (select * from address where address.id=billing_address_id));

update charge set reported_at=now() where reported_at is null and created_at < '2017-04-01';


delete from payable where not exists (select * from charge where charge.payable_id=payable.id);


delete from commission_payable where not exists (select * from charge_commission CC where CC.commission_payable_id=commission_payable.id);

delete from commission_payable where not exists (select * from agent A where A.id=commission_payable.agent_id);

update invoice set deleted_at=now() where not exists (select * from charge where invoice_id=invoice.id);

update charge set service_id=null where not exists (select * from service where service.id=service_id);

delete from charge where charge.accessorial_id is not null and charge.accessorial_id != 0 and charge.accessorial_id is not null and not exists (select * from order_accessorials where order_accessorials.id=charge.accessorial_id);

update charge set accessorial_id=null where accessorial_id=0;



update customer_order set service_id=(select id from service where carrier_id=customer_order.carrier_id and deleted_at is null limit 1) where carrier_id is not null and service_id is null;
