package com.hrms.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://localhost:8070/api/v1/company", name = "auth-company")
public interface ICompanyManager {
    @PostMapping("/create/{companyName}")
    ResponseEntity<Long> getCompanyIdWithCompanyName(@PathVariable String companyName);
}
