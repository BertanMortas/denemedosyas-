package com.hrms.controller;
import com.hrms.dto.request.CreateCompanyManagerRequestDto;
import com.hrms.repository.entity.CompanyManagers;
import com.hrms.service.CompanyManagersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(COMPANY_MANAGER)
@RequiredArgsConstructor
public class CompanyManagerController {
    private final CompanyManagersService companyManagersService;
    @PostMapping(CREATE)
    public ResponseEntity<CompanyManagers> createCompanyManager(@RequestBody CreateCompanyManagerRequestDto dto){
        return ResponseEntity.ok(companyManagersService.createCompanyManager(dto));
    }
}
