package com.hrms.manager;

import com.hrms.dto.request.CreateCompanyManagerRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.hrms.constant.ApiUrls.CREATE;

@FeignClient(url = "http://localhost:8070/api/v1/company-manager",name = "userprofile-companyManagers")
public interface ICompanyManagersManager {
    @PostMapping(CREATE)
    public ResponseEntity<Void> createCompanyManager(@RequestBody CreateCompanyManagerRequestDto dto);
}
