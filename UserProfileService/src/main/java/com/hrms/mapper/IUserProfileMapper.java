package com.hrms.mapper;

import com.hrms.dto.request.*;
import com.hrms.dto.response.GetNameByIdResponseDto;
import com.hrms.rabbitmq.model.AddEmployeeMailModel;
import com.hrms.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {
    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile createUser(final CreateUserRequestDto dto);
    UserProfile fromAddEmployeeRequestDtoToUserProfile(final AddEmployeeRequestDto dto);
    FromUserProfileToAuthAddEmployeeRequestDto fromUserProfileToFromUserProfileToAuthAddEmployeeRequestDto(final UserProfile userProfile);
    AddEmployeeMailModel fromAddEmployeeRequestDtoToAddEmployeeMailModel(final AddEmployeeRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile fromUpdateUserProfileToUserProfile(@MappingTarget UserProfile userProfile, final UserUpdateRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile fromUserProfileToAddAddressRequestDto(@MappingTarget UserProfile userProfile,final AddAddressRequestDto dto);
    GetNameByIdResponseDto toGetNameDto (final UserProfile userProfile);

}
