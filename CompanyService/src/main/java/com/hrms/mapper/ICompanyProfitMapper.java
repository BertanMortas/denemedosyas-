package com.hrms.mapper;

import com.hrms.dto.request.AddIncomeRequestDto;
import com.hrms.dto.request.AddOutcomeRequestDto;
import com.hrms.repository.entity.CompanyProfit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ICompanyProfitMapper {
    ICompanyProfitMapper INSTANCE = Mappers.getMapper(ICompanyProfitMapper.class);
    CompanyProfit toCompanyProfit(final AddIncomeRequestDto dto);
    CompanyProfit toCompanyProfit(final AddOutcomeRequestDto dto);
}
