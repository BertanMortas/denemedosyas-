package com.hrms.manager;

import com.hrms.dto.response.GetNameByIdResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.hrms.constant.ApiUrls.FIND_BY_ID;

@FeignClient(url = "http://localhost:8090/api/v1/user-profile", name = "company-userprofile")
public interface IUserprofileManager {
    @GetMapping(FIND_BY_ID+"/{userId}")
    public ResponseEntity<GetNameByIdResponseDto> findById(@PathVariable String userId);
    @GetMapping("/find-by-company-name/{companyName}")
    public ResponseEntity<List<String>> findByCompanyName(@PathVariable Long companyId);
}
