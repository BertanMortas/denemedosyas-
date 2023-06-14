package com.hrms.dto.request;

import com.hrms.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FromUserProfileToAuthForActivateManager {
    private Long authId;
    private EStatus status;
}
