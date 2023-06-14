package com.hrms.manager;

import com.hrms.dto.request.FromAuthToUserProfileCreateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8090/api/v1/user-profile", name = "auth-userprofile")
public interface IUserManager {

    @PostMapping("/create")
    ResponseEntity<Boolean> createUser(@RequestBody FromAuthToUserProfileCreateUserRequestDto dto);
}
