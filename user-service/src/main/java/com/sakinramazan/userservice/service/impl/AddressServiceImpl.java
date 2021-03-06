package com.sakinramazan.userservice.service.impl;

import com.sakinramazan.userservice.entity.Address;
import com.sakinramazan.userservice.exception.AddressNotFoundException;
import com.sakinramazan.userservice.repository.AddressRepository;
import com.sakinramazan.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Cacheable(value = "addresses")
    @Override
    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address getOne(Integer id) {
        Optional<Address> byId = addressRepository.findById(id);
        return byId.orElseThrow(() -> new AddressNotFoundException(id));
    }

    @CacheEvict(value = "addresses", allEntries = true)
    @Override
    public Address addOne(Address address) {
        return addressRepository.save(address);
    }

    @CacheEvict(value = "addresses", allEntries = true)
    @Override
    public Address updateOne(Address address) {
        if (address.getId() == null)
            throw new RuntimeException("Id must not be null for update entity");
        getOne(address.getId());
        return addressRepository.save(address);
    }

    @CacheEvict(value = "addresses", allEntries = true)
    @Override
    public boolean deleteOne(Integer id) {
        Address one = getOne(id);
        addressRepository.save(one);
        return true;
    }
}
