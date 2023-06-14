package com.hrms.repository;

import com.hrms.repository.entity.CompanyManagers;
import com.hrms.repository.view.VWgetCompanyManagers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyManagersRepository extends JpaRepository<CompanyManagers, Long> {

    @Query("select new com.hrms.repository.view.VWgetCompanyManagers(c.name,c.surname) from CompanyManagers as c where c.companyId=?1")
    List<VWgetCompanyManagers> getCompanyManagersNameAndSurNameWithCompanyId(Long companyId);
}
