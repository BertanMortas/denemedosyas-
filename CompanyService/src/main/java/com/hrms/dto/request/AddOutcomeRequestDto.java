package com.hrms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOutcomeRequestDto {
    private String token;
    private Long companyId;
    private Double outcome;
    private String comment;
}
