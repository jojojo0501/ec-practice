-- 確認用
SELECT 
o.id o_id,o.user_id o_user_id,o.status o_status,o.total_price o_total_price,o.order_date o_order_date,
o.destination_name o_destination_name,o.destination_email o_destination_email,o.destination_zipcode o_destination_zipcode,o.destination_address o_destination_address,o.destination_tel o_destination_tel,
o.delivery_time o_delivery_time,o.payment_method o_payment_method,
oi.id oi_id,oi.item_id oi_item_id,oi.order_id oi_order_id,oi.quantity oi_quantity,oi.size oi_size,
ot.id ot_id,ot.topping_id ot_topping_id,ot.order_item_id ot_order_item_id,
i.id i_id,i.name i_name,i.price_m i_price_m,i.price_l i_price_l,
t.id t_id,t.name t_name,t.price_m t_price_m,t.price_l t_price_l
FROM orders o LEFT OUTER JOIN order_items oi ON o.id = oi.order_id
LEFT OUTER JOIN order_toppings ot ON oi.id = ot.order_item_id
LEFT OUTER JOIN items i ON oi.item_id = i.id
LEFT OUTER JOIN toppings t ON ot.topping_id = t.id;

-- 確認用
SELECT 
o.id o_id,o.user_id o_user_id,o.status o_status,o.total_price o_total_price,o.order_date o_order_date,
o.destination_name o_destination_name,o.destination_email o_destination_email,o.destination_zipcode o_destination_zipcode,o.destination_address o_destination_address,o.destination_tel o_destination_tel,
o.delivery_time o_delivery_time,o.payment_method o_payment_method,
oi.id oi_id,oi.item_id oi_item_id,oi.order_id oi_order_id,oi.quantity oi_quantity,oi.size oi_size,
ot.id ot_id,ot.topping_id ot_topping_id,ot.order_item_id ot_order_item_id,
i.id i_id,i.name i_name,i.price_m i_price_m,i.price_l i_price_l,
t.id t_id,t.name t_name,t.price_m t_price_m,t.price_l t_price_l
FROM orders o LEFT OUTER JOIN order_items oi ON o.id = oi.order_id
LEFT OUTER JOIN order_toppings ot ON oi.id = ot.order_item_id
LEFT OUTER JOIN items i ON oi.item_id = i.id
LEFT OUTER JOIN toppings t ON ot.topping_id = t.id
 WHERE o.user_id = 1 AND o.status <> 0 ORDER BY oi.id DESC;
