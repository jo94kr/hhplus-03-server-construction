delete
from concert;
delete
from concert_schedule;
delete
from concert_seat;

alter table concert
    alter column id restart with 1;
alter table concert_schedule
    alter column id restart with 1;
alter table concert_seat
    alter column id restart with 1;

insert into concert(id, name, create_datetime)
values (1, '백엔드 콘서트', now()),
       (2, '프론트엔드 콘서트', now());

insert into concert_schedule(id, concert_id, concert_datetime, status, create_datetime)
values (1, 1, '2024-07-20T13:00:00', 'AVAILABLE', now()),
       (2, 1, '2024-07-21T13:00:00', 'AVAILABLE', now()),
       (3, 2, '2024-08-01T13:00:00', 'AVAILABLE', now());

insert into concert_seat(id, concert_schdule_id, seat_num, grade, price, status, create_datetime)
values (1, 1, 'A01', 'GOLD', 3000, 'POSSIBLE', now()),
       (2, 1, 'B01', 'SILVER', 2000, 'POSSIBLE', now()),
       (3, 1, 'C01', 'BRONZE', 1000, 'POSSIBLE', now()),
       (4, 2, 'A01', 'GOLD', 3000, 'POSSIBLE', now()),
       (5, 2, 'A02', 'GOLD', 3000, 'POSSIBLE', now()),
       (6, 2, 'A03', 'GOLD', 3000, 'PENDING', now());
