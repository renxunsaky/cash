insert into T_USER(login, password, created_by, created_date, activated) values('test', 'bfb7f2438f88efa452273f8b95e6b15cb024cd44a9c2585f5e02d34bda805037b34dcebe54661703', 'system', sysdate(), true);
insert into T_USER(login, password, created_by, created_date, activated) values('admin', '956281511c2ed279b3323445432af3b991b098ff40b6c1c2d4f2bbb6bf143d217d7b6927ae5697b9', 'system', sysdate(), true);
insert into T_CONFIG(id, name, value, description) values(1, 'TERMINAL_ID:00-23-6C-8F-B4-75', 'Terminal Tomat', null);
insert into T_CONFIG(id, name, value, description) values(11, 'STRICK_REDUCTION_ACTIVE', 'true', null);
insert into T_CONFIG(id, name, value, description) values(12, 'STRICK_REDUCTION_VALUE', '0;0.2;0.3', null);
insert into T_CONFIG(id, name, value, description) values(13, 'LOCALE', 'FR', null);
insert into T_CONFIG(id, name, value, description) values(14, 'ADMIN_PASSWORD', 'admin', null);
insert into T_CONFIG(id, name, value, description) values(15, 'DISCOUNT_COUPON_FOR_ALL', 'false', null);



insert into T_MemebershipLevel(level, name, discount) values(1, 'Golden', 0.25);
insert into T_MemebershipLevel(level, name, discount) values(2, 'Black', 0.2);
insert into T_MemebershipLevel(level, name, discount) values(3, 'White', 0.1);
insert into T_CLIENT(id, code, firstname, lastname, address, postcode, city, phone, membershipLevelId, membershipStartDate, created_by, created_date, activated) values(1, '9910001', 'Thomas', 'Holland', '198 Quai Alphonse le Gallo', '92100', 'Boulogne Billancourt', '06 73 06 69 11', 1, sysdate(), 'system', sysdate(), true);
