package io.hhplus.server_construction.infra.user.mapper;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.infra.user.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity userEntity) {
        return User.create(userEntity.getId(),
                userEntity.getName(),
                userEntity.getAmount(),
                userEntity.getCreateDatetime(),
                userEntity.getModifyDatetime(),
                userEntity.getVersion()
        );
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getId(),
                user.getName(),
                user.getAmount(),
                user.getVersion());
    }
}
