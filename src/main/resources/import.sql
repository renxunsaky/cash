insert into T_USER(login, password, created_by, created_date, activated) values('test', 'bfb7f2438f88efa452273f8b95e6b15cb024cd44a9c2585f5e02d34bda805037b34dcebe54661703', 'system', sysdate, true);
insert into T_USER(login, password, created_by, created_date, activated) values('admin', '956281511c2ed279b3323445432af3b991b098ff40b6c1c2d4f2bbb6bf143d217d7b6927ae5697b9', 'system', sysdate, true);

insert into T_CATEGORY(name, code, discount) values('vetement', '10', 0);
insert into T_CATEGORY(name, code, discount) values('pantalon', '20', 0);
insert into T_CATEGORY(name, code, discount) values('chaussure', '30', 0);

insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(1, '2015 Nouvelle collection', '100001', 'vetement', 29.9, 'system', sysdate);
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(2, '2015 fashion black', '100002', 'vetement', 19.9, 'system', sysdate);
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(3, 'Running', '300001', 'chaussure', 59.9, 'system', sysdate);
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(4, 'tennis', '300002', 'chaussure', 99.9, 'system', sysdate);
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(5, 'jeans', '200001', 'pantalon', 39.9, 'system', sysdate);
insert into T_PRODUCT(id, name, code, category, price, created_by, created_date) values(6, 'Lee', '200002', 'pantalon', 49.9, 'system', sysdate);