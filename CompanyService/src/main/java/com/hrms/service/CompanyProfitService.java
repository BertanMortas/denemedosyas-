package com.hrms.service;

import com.hrms.dto.request.AddIncomeRequestDto;
import com.hrms.dto.request.AddOutcomeRequestDto;
import com.hrms.exception.CompanyManagerException;
import com.hrms.exception.ErrorType;
import com.hrms.mapper.ICompanyProfitMapper;
import com.hrms.repository.ICompanyProfitRepository;
import com.hrms.repository.entity.CompanyProfit;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyProfitService extends ServiceManager<CompanyProfit,Long> {
    private final ICompanyProfitRepository companyProfitRepository;
    private final JwtTokenProvider jwtTokenProvider;
    public CompanyProfitService(ICompanyProfitRepository companyProfitRepository, JwtTokenProvider jwtTokenProvider) {
        super(companyProfitRepository);
        this.companyProfitRepository = companyProfitRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /**
     * frontend de şirket yöneticisinin şirkete gelir eklemesi için kullanılacak olan method
     * @param dto
     * @return
     */
    public CompanyProfit addIncome(AddIncomeRequestDto dto){// todo frontend de gelen datalar string olabilir ona göre dtoları refactor edebiliriz
        tokenRoleControls(dto.getToken());
        CompanyProfit companyProfit = ICompanyProfitMapper.INSTANCE.toCompanyProfit(dto);
        return save(companyProfit);
    }

    public CompanyProfit addOutcome(AddOutcomeRequestDto dto){
        tokenRoleControls(dto.getToken());
        CompanyProfit companyProfit = ICompanyProfitMapper.INSTANCE.toCompanyProfit(dto);
        return save(companyProfit);
    }
    public Double findIncomeByCompanyId(Long companyId){
        return companyProfitRepository.findSumOfIncomes(companyId);
    }
    public Double findOutcomeByCompanyId(Long companyId){
        return companyProfitRepository.findSumOfOutcomes(companyId);
    }
    /**
     * her metod da aynı kontrolleri tekrar yazmamak için ekledim
     * @param token
     * @return role dönüşü yapıyor
     */
    public List<String> tokenRoleControls(String token){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);// hata düzenlenebilir
        }
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (role.stream().anyMatch(x-> x.equals("MANAGER"))) {
            return role;
        }
        throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
    }
    /**
     * her metod da aynı kontrolleri tekrar yazmamak için ekledim
     * @param token
     * @return authId dönüşü yapıyor
     */
    public Long tokenRoleControlsAndAuthId(String token){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);// hata düzenlenebilir
        }
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (role.stream().anyMatch(x-> x.equals("MANAGER"))) {
            return authId.get();
        }
        throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
    }
}
