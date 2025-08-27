package com.alexsys.smartmarket.order.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.order.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    // Update existing OrderItem (ignore null values)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget OrderItem target, OrderItem source);
}
