package com.hrms.controller;

import com.hrms.dto.request.CreateCompanyRequestDto;
import com.hrms.dto.request.DeleteCompanyRequestDto;
import com.hrms.dto.request.ProfitLossRequestDto;
import com.hrms.dto.response.GetCompanyResponseDto;
import com.hrms.dto.response.ProfitLossResponseDto;
import com.hrms.repository.entity.Company;
import com.hrms.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(COMPANY)
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    ResponseEntity<Company> createCompany(@RequestBody CreateCompanyRequestDto dto){
        return ResponseEntity.ok(companyService.createCompany(dto));
    }
    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Boolean> deleteCompany(@RequestBody DeleteCompanyRequestDto dto){
        return ResponseEntity.ok(companyService.deleteCompanyById(dto));
    }
    @PostMapping(PROFIT_LOSS)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<ProfitLossResponseDto> profitLoss(@RequestBody ProfitLossRequestDto dto){
        return ResponseEntity.ok(companyService.profitLoss(dto));
    }
    @GetMapping(GET_COMPANIES)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<GetCompanyResponseDto>> getCompanies(){
        return ResponseEntity.ok(companyService.getCompany());
    }
    @GetMapping(GET_COMPANIES+"-with-name")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<GetCompanyResponseDto>> getCompanyWithName(String companyName){
        return ResponseEntity.ok(companyService.getCompanyWithName(companyName));
    }
    @PostMapping("/create/{companyName}")
    public ResponseEntity<Long> getCompanyIdWithCompanyName(@PathVariable String companyName){
        return  ResponseEntity.ok(companyService.findByCompanyNameIgnoreCase(companyName));
    }
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Company>> findAll(){
        return  ResponseEntity.ok(companyService.findAll());
    }
}
