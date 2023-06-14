package com.hrms.repository;

import com.hrms.repository.entity.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IAddressRepository extends MongoRepository<Address,String> {
   Optional<Address> findByAuthId(Long authId);
}
