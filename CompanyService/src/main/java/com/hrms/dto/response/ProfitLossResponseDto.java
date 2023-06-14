package com.hrms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfitLossResponseDto {
    private Double totalIncome;
    private Double totalOutcome;
    private Double companyTotalProfit;
}
