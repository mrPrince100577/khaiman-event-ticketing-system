package com.stj.khaiman.user.mapper;

import com.stj.khaiman.auth.dto.request.RegisterRequest;
import com.stj.khaiman.auth.dto.response.RegisterResponse;
import com.stj.khaiman.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserEntity toUserEntity(RegisterRequest registerRequest);

    RegisterResponse toRegisterResponse(UserEntity user);
}
