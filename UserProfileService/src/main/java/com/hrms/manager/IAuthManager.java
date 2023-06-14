package com.hrms.manager;

import com.hrms.dto.request.FromUserProfileToAuthAddEmployeeRequestDto;
import com.hrms.dto.request.FromUserProfileToAuthForActivateManager;
import com.hrms.dto.request.UserUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:8060/api/v1/auth",name = "userprofile-auth")
public interface IAuthManager {
    @PostMapping("/do-activate-manager")
    ResponseEntity<Boolean> doActivateManager(@RequestBody FromUserProfileToAuthForActivateManager dto);

    @PostMapping("/create-employee")
    ResponseEntity<Long> createEmployee(@RequestBody FromUserProfileToAuthAddEmployeeRequestDto dto);
    @PutMapping("update-userprofile/{token}")
    public ResponseEntity<Boolean> updateUserProfile(@PathVariable String token, @RequestBody UserUpdateRequestDto dto);

    @DeleteMapping("delete-auth/{authId}")
    public ResponseEntity<Boolean> deleteAuth(@PathVariable Long authId);
}
