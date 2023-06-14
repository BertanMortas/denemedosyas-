package com.hrms.service;

import com.hrms.dto.request.CreateCompanyManagerRequestDto;
import com.hrms.mapper.ICompanyManagersMapper;
import com.hrms.mapper.ICompanyMapper;
import com.hrms.repository.ICompanyManagersRepository;
import com.hrms.repository.entity.Company;
import com.hrms.repository.entity.CompanyManagers;
import com.hrms.repository.view.VWgetCompanyManagers;
import com.hrms.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyManagersService extends ServiceManager<CompanyManagers,Long> {
    private final ICompanyManagersRepository companyManagersRepository;
    public CompanyManagersService(ICompanyManagersRepository companyManagersRepository) {
        super(companyManagersRepository);
        this.companyManagersRepository = companyManagersRepository;
    }
    public CompanyManagers createCompanyManager(CreateCompanyManagerRequestDto dto){
        return save(ICompanyManagersMapper.INSTANCE.toCompanyManagers(dto));
    }
    public List<VWgetCompanyManagers> getCompanyManagersWithCompanyId(Long companyId){
       List<VWgetCompanyManagers> VWgetCompanyManagers=
               companyManagersRepository.getCompanyManagersNameAndSurNameWithCompanyId(companyId);
       return  VWgetCompanyManagers;
    }
}
