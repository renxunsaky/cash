insert into T_USER(login, password, created_by, created_date, activated) values('test', 'bfb7f2438f88efa452273f8b95e6b15cb024cd44a9c2585f5e02d34bda805037b34dcebe54661703', 'system', sysdate(), true);
insert into T_USER(login, password, created_by, created_date, activated) values('admin', '956281511c2ed279b3323445432af3b991b098ff40b6c1c2d4f2bbb6bf143d217d7b6927ae5697b9', 'system', sysdate(), true);

insert into T_CATEGORY(name, code, discount) values('Vetement', '10', 0);
insert into T_CATEGORY(name, code, discount) values('Pantalon', '20', 0);
insert into T_CATEGORY(name, code, discount) values('Chaussure', '30', 0);

insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(1, 'Ketty', '100001', 'Vetement', 29.9, 'system', sysdate());
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(2, 'Fashion', '100002', 'Vetement', 19.9, 'system', sysdate());
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(3, 'Running', '300001', 'Chaussure', 59.9, 'system', sysdate());
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(4, 'tennis', '300002', 'Chaussure', 99.9, 'system', sysdate());
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(5, 'Jeans', '200001', 'Pantalon', 39.9, 'system', sysdate());
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(6, 'Lee', '200002', 'Pantalon', 49.9, 'system', sysdate());

insert into T_CONFIG(id, name, value, description) values(1, 'TERMINAL_ID:00-23-6C-8F-B4-75', 'Terminal Tomat', null);
insert into T_CONFIG(id, name, value, description) values(2, 'SHORTCUT_PRICES:100001', '5.99;7.99;9.99;14.99;17.99;19.99;24.99;29.99', null);
insert into T_CONFIG(id, name, value, description) values(3, 'SHORTCUT_PRICES:100002', '5.99;7.99;9.99;14.99;17.99;19.99;24.99;29.99', null);
insert into T_CONFIG(id, name, value, description) values(4, 'SHORTCUT_PRICES:200001', '6.99;7.99;9.99;14.99;17.99;19.99;49.99;99.99', null);
insert into T_CONFIG(id, name, value, description) values(5, 'SHORTCUT_PRICES:200002', '6.99;7.99;9.99;14.99;17.99;19.99;49.99;99.99', null);
insert into T_CONFIG(id, name, value, description) values(6, 'SHORTCUT_PRICES:300001', '6.99;7.99;9.99;14.99;17.99;19.99;49.99;99.99', null);
insert into T_CONFIG(id, name, value, description) values(7, 'SHORTCUT_PRICES:300002', '6.99;7.99;9.99;14.99;17.99;19.99;49.99;99.99', null);
insert into T_CONFIG(id, name, value, description) values(8, 'SHORTCUT_PRODUCTS:10', '100001;100002', null);
insert into T_CONFIG(id, name, value, description) values(9, 'SHORTCUT_PRODUCTS:20', '200001;200002', null);
insert into T_CONFIG(id, name, value, description) values(10, 'SHORTCUT_PRODUCTS:30', '300001;300002', null);

insert into T_CONFIG(id, name, value, description) values(11, 'STRICK_REDUCTION_ACTIVE', 'true', null);
insert into T_CONFIG(id, name, value, description) values(12, 'STRICK_REDUCTION_VALUE', '0;0.2;0.3', null);
insert into T_CONFIG(id, name, value, description) values(13, 'LOCALE', 'FR', null);
insert into T_CONFIG(id, name, value, description) values(14, 'ADMIN_PASSWORD', 'admin', null);
insert into T_CONFIG(id, name, value, description) values(15, 'DISCOUNT_COUPON_FOR_ALL', 'false', null);



insert into T_MemebershipLevel(level, name, discount) values(1, 'Golden', 0.25);
insert into T_MemebershipLevel(level, name, discount) values(2, 'Black', 0.2);
insert into T_MemebershipLevel(level, name, discount) values(3, 'White', 0.1);
insert into T_CLIENT(id, code, firstname, lastname, adress, postcode, city, phone, membershipLevelId, membershipStartDate, created_by, created_date, activated) values(1, '9910001', 'Thomas', 'Holland', '198 Quai Alphonse le Gallo', '92100', 'Boulogne Billancourt', '06 73 06 69 11', 1, sysdate(), 'system', sysdate(), true);
