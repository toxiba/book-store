package com.example.bookstore.mappers;

import com.example.bookstore.dtos.CartCheckoutDto;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {CartItemCheckoutMapper.class})
public interface CartCheckoutMapper {

    @Mappings({
        @Mapping(target = "username", source = "user.username"),
        @Mapping(target = "total", ignore = true)
    })
    CartCheckoutDto toDto(CartEntity entity);


    @AfterMapping
    default void calculateTotal(@MappingTarget CartCheckoutDto cartCheckoutDto, CartEntity entity) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItemEntity cartItem: entity.getItems()) {
            Integer quantity = cartItem.getQuantity();
            BigDecimal price = cartItem.getBook().getPrice();

            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

            total = total.add(subtotal);
        }

        cartCheckoutDto.setTotal(total);
    }
}
