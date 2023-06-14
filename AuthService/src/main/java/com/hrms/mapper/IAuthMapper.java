package com.hrms.mapper;

import com.hrms.dto.request.*;
import com.hrms.dto.response.RegisterResponseDto;
import com.hrms.rabbitmq.model.RegisterMailModel;
import com.hrms.repository.entity.Auth;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    FromAuthToUserProfileCreateUserRequestDto fromAuthToCreateUserRequestDto(final Auth auth);

    Auth fromRegisterRequestDtoToAuth(final RegisterRequestDto dto);

    RegisterResponseDto fromAuthToRegisterResponseDto(final Auth auth);
    Auth fromFromUserProfileToAuthAddEmployeeRequestDtoToAuth(final FromUserProfileToAuthAddEmployeeRequestDto dto);

    //userRegister için
    Auth fromRegisteUserRequestDtoToAuth(final RegisterUserRequestDto dto);

    //companyRegister için
    Auth fromRegisterCompanyRequestDtoToAuth(final RegisterCompanyRequestDto dto);
    RegisterMailModel fromAuthToRegisterMailModel(final Auth auth);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auth fromUserUpdateRequestDtotoAuth(@MappingTarget Auth auth, final UserUpdateRequestDto dto);
}
