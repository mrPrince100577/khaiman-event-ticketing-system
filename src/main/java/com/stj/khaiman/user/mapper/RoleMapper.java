package com.stj.khaiman.user.mapper;

import com.stj.khaiman.user.dto.response.RoleSummaryResponse;
import com.stj.khaiman.user.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleSummaryResponse toRoleSummaryResponse(RoleEntity entity);
}
