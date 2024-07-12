package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.infra.entity.ConcertSeatEntity;
import io.hhplus.server_construction.infra.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity userEntity) {
        return User.create(userEntity.getId(),
                userEntity.getName(),
                userEntity.getAmount(),
                userEntity.getCreateDatetime(),
                userEntity.getModifyDatetime()
        );
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getId(),
                user.getName(),
                user.getAmount());
    }
}
