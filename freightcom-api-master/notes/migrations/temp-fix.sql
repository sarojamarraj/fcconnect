

update customer_order set service_id=(select id from service where carrier_id=customer_order.carrier_id and deleted_at is null limit 1) where carrier_id is not null and service_id is null;
