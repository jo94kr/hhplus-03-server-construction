delete
from reservation;
delete
from reservation_item;

alter table reservation
    alter column id restart with 1;
alter table reservation_item
    alter column id restart with 1;

insert into reservation(id, users_id, status, total_price, create_datetime)
values (1, 1, 'PAYMENT_WAITING', 5000, now()),
       (2, 2, 'PAYMENT_COMPLETE', 3000, now()),
       (3, 3, 'CANCEL', 2000, now());

insert into reservation_item(id, reservation_id, concert_seat_id, price, create_datetime)
values (1, 1, 1, 3000, now()),
       (2, 1, 2, 2000, now()),
       (3, 2, 3, 3000, now()),
       (4, 3, 4, 2000, now());
