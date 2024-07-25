delete
from users;

alter table users AUTO_INCREMENT = 1;

insert into users(id, name, amount, create_datetime, version)
values (1, '조진우', 5000, now(), 0),
       (2, '김영원', 5000, now(), 0),
       (3, '김지연', 6000, now(), 0),
       (4, '서건희', 7000, now(), 0);
