package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByCity(String city);
    Optional<Address> findAddressByCountry(String country);
    Optional<Address> findAddressById(Long id);
    Optional<Address> findAddressByCityAndStreetAndNumber(String city, String street, String number);
    @Override
    void deleteById(Long id);

}
