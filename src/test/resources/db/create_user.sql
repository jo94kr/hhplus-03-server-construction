delete
from users;

alter table users
    alter column id restart with 1;

insert into users(id, name, amount, create_datetime)
values (1, '조진우', 5000, now()),
       (2, '김영원', 5000, now()),
       (3, '김지연', 6000, now()),
       (4, '서건희', 7000, now());
