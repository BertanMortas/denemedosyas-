package com.hrms.mapper;

import com.hrms.dto.request.CreateCompanyRequestDto;
import com.hrms.dto.response.GetCompanyResponseDto;
import com.hrms.dto.response.ProfitLossResponseDto;
import com.hrms.repository.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);
    Company toCompany(final CreateCompanyRequestDto dto);
    ProfitLossResponseDto toProfitLossDto(Double totalIncome, Double totalOutcome, Double companyTotalProfit);

    GetCompanyResponseDto toGetCompanyResponseDto(final Company company);
}
