package com.alexsys.smartmarket.order.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.order.model.PaymentDetails;

@Mapper(componentModel = "spring")
public interface PaymentDetailsMapper {

    // Update existing PaymentDetails (ignore null values)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget PaymentDetails target, PaymentDetails source);
}
