

select S.carrier_id from customer_order O
left join selected_quote Q on Q.id = O.selected_quote_id
left join service S on S.id=Q.service_id
where O.deleted_at is null and selected_quote_id is not null and S.carrier_id is not null;




update charge set carrier_id=(select S.carrier_id from customer_order O
left join selected_quote Q on Q.id = O.selected_quote_id
left join service S on S.id=Q.service_id
where O.id=charge.order_id and O.deleted_at is null and selected_quote_id is not null and S.carrier_id is not null);
