-- Cousines
insert into cousine (name) values('Sushi');
insert into cousine (name) values('Chinese');
insert into cousine (name) values('Pizza');
insert into cousine (name) values('Vietnamese');
insert into cousine (name) values('Brazilian');
insert into cousine (name) values('Mexican');

-- Stores
insert into store (name, cousine_id) values('Best Sushi', 1);
insert into store (name, cousine_id) values('Sushi in Box', 1);

insert into store (name, cousine_id) values('Chine in box', 2);
insert into store (name, cousine_id) values('Chines food bar', 2);

insert into store (name, cousine_id) values('Casa de la pizza', 3);
insert into store (name, cousine_id) values('Za Pizza Bistro', 3);

-- Products
insert into product (store_id, name, description, price) values(1, 'Shrimp Tempura', 'Fresh shrimp battered and deep fried until golden brown', 10.95);
insert into product (store_id, name, description, price) values(1, 'Special Deep-Fried Fish', 'Tilapia fish deep fried until flaky and tender', 15.55);

-- Customer
insert into CUSTOMER (email,name,address,creation,password) values(1, 'luca.ds.lima@gmail.com', 'Lucas Lima', '2018-03-18 16:47:52.69', '123');

-- Order
insert into TB_ORDER (customer_id,delivert_address,contact,store_id,total,order_date,order_status,order_type) values (1, 'Hower', '55659962045', 1, 87.55, '2018-03-18 16:47:52.69', 'WAITING', 'DELIVERY');

insert into ORDER_ITEM (order_id,product_id,price,quantity,total) values (1, 1, 87.99, 2, 150.00);
insert into ORDER_ITEM (order_id,product_id,price,quantity,total) values (1, 2, 12.19, 1, 24.48);