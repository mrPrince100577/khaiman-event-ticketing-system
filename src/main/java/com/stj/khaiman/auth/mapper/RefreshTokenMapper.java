package com.stj.khaiman.auth.mapper;

import com.stj.khaiman.auth.entity.RefreshToken;
import com.stj.khaiman.auth.record.IssuedToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(source = "expiresAt", target = "expTime")
    @Mapping(source = "jti", target = "jti")
    @Mapping(source = "sid", target = "sid")
    @Mapping(target = "user", ignore = true)
    RefreshToken toRefreshTokenEntity(IssuedToken token);
}
