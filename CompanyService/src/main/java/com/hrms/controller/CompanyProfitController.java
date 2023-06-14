package com.hrms.controller;

import com.hrms.dto.request.AddIncomeRequestDto;
import com.hrms.dto.request.AddOutcomeRequestDto;
import com.hrms.repository.entity.CompanyProfit;
import com.hrms.service.CompanyProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hrms.constant.ApiUrls.*;

@RestController
@RequestMapping(COMPANY_PROFIT)
@RequiredArgsConstructor
public class CompanyProfitController {
    private final CompanyProfitService companyProfitService;
    @PostMapping(ADD_INCOME)
    public ResponseEntity<CompanyProfit> addIncome(AddIncomeRequestDto dto){
        return ResponseEntity.ok(companyProfitService.addIncome(dto));
    }
    @PostMapping(ADD_OUTCOME)
    public ResponseEntity<CompanyProfit> addOutcome(AddOutcomeRequestDto dto){
        return ResponseEntity.ok(companyProfitService.addOutcome(dto));
    }
}
