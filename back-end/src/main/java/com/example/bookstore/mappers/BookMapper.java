package com.example.bookstore.mappers;

import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.entities.BookEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(BookEntity entity);

    BookEntity toEntity(BookDto dto);

    List<BookDto> toDtoList(List<BookEntity> entities);
}
