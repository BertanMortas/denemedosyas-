package com.hrms.repository;

import com.hrms.repository.entity.UserProfile;
import com.hrms.repository.entity.enums.EStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository extends MongoRepository<UserProfile,String> {
    List<UserProfile> findAllByStatus(EStatus status);
    Optional<UserProfile> findByAuthId(Long authId);
    List<UserProfile> findAllByCompanyId(Long id);
    Optional<UserProfile> findByEmail(String email);
}
