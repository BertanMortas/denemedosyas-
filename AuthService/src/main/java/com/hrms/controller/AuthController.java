package com.hrms.controller;

import com.hrms.dto.request.*;
import com.hrms.dto.response.LoginResponseDto;
import com.hrms.dto.response.RegisterResponseDto;
import com.hrms.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


 /*   @PostMapping(REGISTER_USER)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> registerUser(@RequestBody @Valid RegisterUserRequestDto dto){
        return ResponseEntity.ok(authService.registerUser(dto));
    }
    @PostMapping(REGISTER_COMPANY)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<RegisterResponseDto> registerCompany(@RequestBody @Valid RegisterCompanyRequestDto dto){
        return ResponseEntity.ok(authService.registerCompany(dto));
    }*/

    @PostMapping(REGISTER)
    public ResponseEntity<Boolean> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/activate-status/{token}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable String token) {
        return ResponseEntity.ok(authService.activateStatus(token));
    }

    @Hidden
    @PostMapping("/do-activate-manager")
    public ResponseEntity<Boolean> doActivateManager(@RequestBody FromUserProfileToAuthForActivateManager dto) {
        return ResponseEntity.ok(authService.doActivateManager(dto));
    }

    @GetMapping("/forgot-password-mail/{email}")
    public ResponseEntity<Boolean> forgotPasswordMail(@PathVariable String email){
        return ResponseEntity.ok(authService.forgotPasswordMail(email));
    }

    @GetMapping("/change-forgot-password")
    public ResponseEntity<Boolean> changeForgotPassword(){
        return ResponseEntity.ok(authService.changeForgotPassword());
    }

    @PutMapping("/forgot-password")
    ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordRequestDto dto){
        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    @Hidden
    @PostMapping("/create-employee")
    ResponseEntity<Long> createEmployee(@RequestBody FromUserProfileToAuthAddEmployeeRequestDto dto){
        return ResponseEntity.ok(authService.createEmployee(dto));
    }

    @Hidden
    @PutMapping("update-userprofile/{token}")
    public ResponseEntity<Boolean> updateUserProfile(@PathVariable String token, @RequestBody UserUpdateRequestDto dto){
        return ResponseEntity.ok(authService.updateAuth(token, dto));
    }

    @Hidden
    @DeleteMapping("delete-auth/{authId}")
    public ResponseEntity<Boolean> deleteAuth(@PathVariable Long authId){
        return ResponseEntity.ok(authService.deleteAuth(authId));
    }
}
