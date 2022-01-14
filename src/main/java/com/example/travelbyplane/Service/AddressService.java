package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Model.Address;
import com.example.travelbyplane.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public boolean updateAddress(long id,Address addressUpdate){
        Optional<Address> address = addressRepository.findAddressById(id);
        if(address.isPresent()){
            addressUpdate.setId(address.get().getId());
            addressRepository.save(addressUpdate);
            return true;
        }
        return false;
    }

    public Address addAddress(Address address){
        Optional<Address> optAddress = addressRepository.findAddressByCityAndStreetAndNumber(address.getCity(), address.getStreet(), address.getNumber());
        if(optAddress.isPresent()){
            throw new EntityExistsException("This address already exists!");
        }
        else{
            return addressRepository.save(address);
        }
    }

}
