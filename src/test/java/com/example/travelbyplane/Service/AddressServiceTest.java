package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Model.Address;
import com.example.travelbyplane.Repository.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressService addressService;

    @Test
    @DisplayName("Address doesn't exists in database.")
    void updateAddressNotExists() {
        when(addressRepository.findAddressById(anyLong())).thenReturn(Optional.empty());

        boolean result = addressService.updateAddress(anyLong(),new Address());

        assertFalse(result);
    }

    @Test
    @DisplayName("Address exists in database -> address updated")
    void updateAddressSuccess(){
        Address address = new Address();
        address.setId(3);
        when(addressRepository.findAddressById(anyLong())).thenReturn(Optional.of(address));

        boolean result = addressService.updateAddress(anyLong(), new Address());

        assertTrue(result);

    }

    @Test
    @DisplayName("Address exists")
    void addAddressExists() {
        when(addressRepository.findAddressByCityAndStreetAndNumber(anyString(), anyString(), anyString())).thenReturn(Optional.of(new Address()));

        Address address = new Address();
        address.setCity("Bucharest");
        address.setStreet("Calea Bucurestilor");
        address.setNumber("23");
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> addressService.addAddress(address));

        assertEquals("This address already exists!", exception.getMessage());
    }

    @Test
    @DisplayName("Address doesn't exist -> address can be added")
    void addAddressNotExists() {
        when(addressRepository.findAddressByCityAndStreetAndNumber(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
        Address address = new Address();
        address.setCity("Bucharest");
        address.setStreet("Calea Bucurestilor");
        address.setNumber("23");
        Address savedAddress = new Address();
        savedAddress.setId(100);
        when(addressRepository.save(address)).thenReturn(savedAddress);

        Address result = addressService.addAddress(address);

        assertEquals(savedAddress.getId(), result.getId());

    }

}