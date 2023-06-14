package com.hrms.controller;

import com.hrms.dto.request.*;
import com.hrms.dto.response.AddAddressResponseDto;
import com.hrms.dto.response.GetNameByIdResponseDto;
import com.hrms.dto.response.GetOffDaysResponseDto;
import com.hrms.repository.entity.UserProfile;
import com.hrms.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Hidden
    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody CreateUserRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @PostMapping("/doActivateManager")
    public ResponseEntity<Boolean> doActiveManager(String token, Long authId) {
        return ResponseEntity.ok(userProfileService.doActiveManager(token, authId));
    }

    @PostMapping("/add-employee/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> addEmployee(@PathVariable String token, @RequestBody @Valid AddEmployeeRequestDto dto) {
        return ResponseEntity.ok(userProfileService.addEmployee(token, dto));
    }
//

    //
    
    /*
        @PostMapping("/activate-director/{token}/{directorId}")
        public ResponseEntity<Boolean> activateDirector(@PathVariable String token, @PathVariable Long directorId){
            return ResponseEntity.ok(userProfileService.activateDirector(token, directorId));
        }

        @DeleteMapping("/delete-employee/{employeeId}/{token}")
        public ResponseEntity<Boolean> removeEmployee(@PathVariable String employeeId,@PathVariable String token){
            return ResponseEntity.ok(userProfileService.deleteEmployee(employeeId,token));
        }

        @GetMapping("/find-by-company-name/{companyName}")
        public ResponseEntity<List<String>> findByCompanyName(@PathVariable String companyName){
            return ResponseEntity.ok(userProfileService.findByCompanyName(companyName));
        }
     */
    @PutMapping("update-userprofile/{token}")
    public ResponseEntity<Boolean> updateUserProfile(@PathVariable String token, @RequestBody UserUpdateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(token, dto));
    }

    @PutMapping("add-address/{token}")
    public ResponseEntity<AddAddressResponseDto> userProfileAddAddressId(@PathVariable String token, @RequestBody AddAddressRequestDto dto) {
        return ResponseEntity.ok(userProfileService.userProfileAddAddressId(token, dto));
    }
    @GetMapping(FIND_BY_ID+"/{userId}")
    public ResponseEntity<GetNameByIdResponseDto> findById(@PathVariable String userId){
        return ResponseEntity.ok(userProfileService.findNameById(userId));
    }
    @PostMapping("/take-off-day")
    public ResponseEntity<Long> takeOffDay(@RequestBody TakeOffDayRequestDto dto){
        return ResponseEntity.ok(userProfileService.takeOffDay(dto));
    }
    @GetMapping("/get-off-day/{token}")
    public ResponseEntity<GetOffDaysResponseDto> getOffDays(@PathVariable String token){
        return ResponseEntity.ok(userProfileService.getOffDays(token));
    }
    @PostMapping("/add-off-days")
    public ResponseEntity<UserProfile> addOffDays(@RequestBody AddOffDaysRequestDto dto){
        return ResponseEntity.ok(userProfileService.addOffDays(dto));
    }
    @GetMapping("/pay-day-comes")
    public ResponseEntity<Boolean> payDayComes(){
        return ResponseEntity.ok(userProfileService.payDayComes());
    }
    @PostMapping("/add-salary-increase")
    public ResponseEntity<UserProfile> addSalaryIncrease(@RequestBody AddSalaryIncreaseRequestDto dto){
        return ResponseEntity.ok(userProfileService.addSalaryIncrease(dto));
    }
    @PostMapping("/update-salary")
    public ResponseEntity<UserProfile> updateSalary(@RequestBody UpdateSalaryRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateSalary(dto));
    }
    @DeleteMapping("delete-employee/{token}/{email}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable String token,@PathVariable String email){
        return ResponseEntity.ok(userProfileService.deleteEmployee(token, email));
    }
}
