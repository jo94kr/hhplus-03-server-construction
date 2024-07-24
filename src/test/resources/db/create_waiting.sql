delete
from waiting;

alter table waiting AUTO_INCREMENT = 1;

insert into waiting(token, status, access_datetime, expired_datetime, create_datetime)
values ('DUMMY_TOKEN_1', 'EXPIRED', now(), TIMESTAMPADD(MINUTE, 5, now()), now()),
       ('DUMMY_TOKEN_2', 'PROCEEDING', now(), TIMESTAMPADD(MINUTE, 5, now()), now()),
       ('DUMMY_TOKEN_3', 'WAITING', TIMESTAMPADD(MINUTE, 5, now()), TIMESTAMPADD(MINUTE, 5, now()), now()),
       ('DUMMY_TOKEN_4', 'WAITING', TIMESTAMPADD(MINUTE, 5, now()), TIMESTAMPADD(MINUTE, 5, now()), now());
