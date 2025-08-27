package com.alexsys.smartmarket.order.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.order.model.OrderDetails;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {

    // Update existing OrderDetails (ignore null values)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget OrderDetails target, OrderDetails source);
}
