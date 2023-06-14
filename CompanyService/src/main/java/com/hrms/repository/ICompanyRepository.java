package com.hrms.repository;

import com.hrms.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Long> {
    @Query("select c.companyName, c.logo from Company as c")
    List<String[]> findCompanyNames();

    List<Company> findByCompanyNameIgnoreCaseContaining(String name);

    Optional<Company> findByCompanyNameIgnoreCase(String name);
}
