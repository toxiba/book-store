package com.example.bookstore.mappers;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserDto toDto(UserEntity entity);

    @Mappings({
            @Mapping(target = "role", constant = "ROLE_USER")
    })
    UserEntity toEntity(UserDto dto);
}
