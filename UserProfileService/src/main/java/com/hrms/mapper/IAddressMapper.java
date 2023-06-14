package com.hrms.mapper;

import com.hrms.dto.request.AddAddressRequestDto;
import com.hrms.dto.response.AddAddressResponseDto;
import com.hrms.repository.entity.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAddressMapper {

    IAddressMapper INSTANCE = Mappers.getMapper(IAddressMapper.class);
    @Mapping(target = "authId", expression = "java(authId)")
    Address toAddress(final AddAddressRequestDto dto,Long authId);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address toUpdateAddress(@MappingTarget Address address,final AddAddressRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AddAddressResponseDto toAddAddressResponseDto(final Address address);
}
