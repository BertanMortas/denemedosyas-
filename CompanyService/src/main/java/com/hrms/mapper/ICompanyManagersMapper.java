package com.hrms.mapper;

import com.hrms.dto.request.CreateCompanyManagerRequestDto;
import com.hrms.repository.entity.CompanyManagers;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ICompanyManagersMapper {
    ICompanyManagersMapper INSTANCE = Mappers.getMapper(ICompanyManagersMapper.class);
    CompanyManagers toCompanyManagers(final CreateCompanyManagerRequestDto dto);
}
