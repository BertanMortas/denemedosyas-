package com.hrms.service;

import com.hrms.dto.request.AddAddressRequestDto;
import com.hrms.dto.response.AddAddressResponseDto;
import com.hrms.mapper.IAddressMapper;
import com.hrms.repository.IAddressRepository;
import com.hrms.repository.entity.Address;
import com.hrms.utility.JwtTokenProvider;
import com.hrms.utility.ServiceManager;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService extends ServiceManager<Address, String> {
    private final IAddressRepository addressRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AddressService(IAddressRepository addressRepository, JwtTokenProvider jwtTokenProvider) {
        super(addressRepository);
        this.addressRepository = addressRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AddAddressResponseDto addAddress(String token, AddAddressRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<Address> address=addressRepository.findByAuthId(authId.get());
        if(address.isEmpty()){
            Address saveAddress=save(IAddressMapper.INSTANCE.toAddress(dto, authId.get()));
            return IAddressMapper.INSTANCE.toAddAddressResponseDto(saveAddress);
        }
       update(IAddressMapper.INSTANCE.toUpdateAddress(address.get(),dto));
        return IAddressMapper.INSTANCE.toAddAddressResponseDto(address.get());
    }

    public Address getAddressId(Long authId) {
        return addressRepository.findByAuthId(authId).get();
    }
    // ahmet deneme
    // arda deneme
    // bertan 2 deneme2
}
