package com.hrms.service;

import com.hrms.dto.request.CreateCompanyRequestDto;
import com.hrms.dto.request.DeleteCompanyRequestDto;
import com.hrms.dto.request.ProfitLossRequestDto;
import com.hrms.dto.response.GetCompanyResponseDto;
import com.hrms.dto.response.ProfitLossResponseDto;
import com.hrms.exception.CompanyManagerException;
import com.hrms.exception.ErrorType;
import com.hrms.manager.IUserprofileManager;
import com.hrms.mapper.ICompanyMapper;
import com.hrms.repository.ICompanyRepository;
import com.hrms.repository.entity.Company;
import com.hrms.repository.entity.CompanyManagers;
import com.hrms.repository.view.VWgetCompanyManagers;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final ICompanyRepository companyRepository;
    private final CompanyProfitService companyProfitService;
    private final IUserprofileManager userprofileManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CompanyManagersService companyManagersService;

    public CompanyService(ICompanyRepository companyRepository, CompanyProfitService companyProfitService,
                          IUserprofileManager userprofileManager, JwtTokenProvider jwtTokenProvider,
                          CompanyManagersService companyManagersService) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.companyProfitService = companyProfitService;
        this.userprofileManager = userprofileManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyManagersService = companyManagersService;
    }


    public Company createCompany(CreateCompanyRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (!roles.stream().anyMatch(x -> x.equals("ADMIN"))) {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        return save(ICompanyMapper.INSTANCE.toCompany(dto));
    }

    public Boolean deleteCompanyById(DeleteCompanyRequestDto dto) {
        companyProfitService.tokenRoleControls(dto.getToken());
        deleteById(dto.getCompanyId());
        return true;
    }

    public ProfitLossResponseDto profitLoss(ProfitLossRequestDto dto) {
        companyProfitService.tokenRoleControls(dto.getToken());
        Double income = companyProfitService.findIncomeByCompanyId(dto.getCompanyId());
        Double outcome = companyProfitService.findOutcomeByCompanyId(dto.getCompanyId());
        Double total = income - outcome;
        // çalışan maaşlarının toplamı çekilecek aylık olarak bunu 12x çarpım
        // eksi olarak incomdan çıkarılacak
        return ICompanyMapper.INSTANCE.toProfitLossDto(income, outcome, total);
    }

    public List<GetCompanyResponseDto> getCompany() {
        List<GetCompanyResponseDto> responseDtoList = new ArrayList<>();
        List<String[]> companyNamesAndUrls = companyRepository.findCompanyNames();

        for (String[] item : companyNamesAndUrls) {
            responseDtoList.add(GetCompanyResponseDto.builder()
                    .companyName(item[0])
                    .companyLogoUrl(item[1])
                    // todo burada firma managerlarının isim ve soy isimlerinin verilmesi lazım
                    //.companyDirectories(userprofileManager.findByCompanyName(item[0]).getBody()) // managerdan şirket direktörünün geldiği kısım

                    .build());
        }
        return responseDtoList;
    }

    public List<GetCompanyResponseDto> getCompanyWithName(String companyName) {
        List<GetCompanyResponseDto> companyResponseDtoList = new ArrayList<>();
        List<Company> companyList = companyRepository.findByCompanyNameIgnoreCaseContaining(companyName);
        System.out.println(companyList);
        companyList.parallelStream().forEach(company -> {
            List<String> fullName = new ArrayList<>();
            GetCompanyResponseDto companyResponseDto = ICompanyMapper.INSTANCE.toGetCompanyResponseDto(company);
            List<VWgetCompanyManagers> companyManagers = companyManagersService.getCompanyManagersWithCompanyId(company.getCompanyId());
            companyManagers.stream().forEach(x -> {
                fullName.add(x.getName() +" "+ x.getSurname());
            });
            companyResponseDto.setCompanyManagersFullName(fullName);
            companyResponseDtoList.add(companyResponseDto);
        });
        return companyResponseDtoList;
    }

    public Long findByCompanyNameIgnoreCase(String companyName){
        return companyRepository.findByCompanyNameIgnoreCase(companyName).get().getCompanyId();
    }

}

