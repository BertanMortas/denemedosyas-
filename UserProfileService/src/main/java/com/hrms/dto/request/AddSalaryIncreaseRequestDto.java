package com.hrms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddSalaryIncreaseRequestDto {
    private String token;
    private Long authId;
    private String salaryIncreaseRate; // 0.07 gibi yüzde 7
}
