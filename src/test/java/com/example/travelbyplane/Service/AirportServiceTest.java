package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Address;
import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Repository.AirportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AddressService addressService;
    @Mock
    private FlightService flightService;
    @InjectMocks
    private AirportService airportService;

    @Test
    @DisplayName("Get all airports")
    void getAirports() {
        List<Airport> airports = new ArrayList<>();
        when(airportRepository.findAll()).thenReturn(airports);

        List<Airport> result = airportService.getAirports();

        assertEquals(airports, result);
    }

    @Test
    @DisplayName("Can't add two airport on the same address")
    void addAirportWrongAddress() {
        Address address = new Address();
        address.setCity("Bucharest");
        address.setStreet("Calea Bucurestilor");
        address.setNumber("23");

        EntityExistsException addressException = new EntityExistsException("This address already exists!");
        when(addressService.addAddress(any(Address.class))).thenThrow(addressException);

        Airport airport = new Airport();
        airport.setAddress(address);
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> airportService.addAirport(airport) );

        assertEquals(addressException.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Add airport and address")
    void addAirportCorrectAddress() {
        Address savedAddress = new Address();
        savedAddress.setId(100);
        savedAddress.setCity("Bucharest");
        savedAddress.setStreet("Calea Bucurestilor");
        savedAddress.setNumber("23");
        when(addressService.addAddress(any(Address.class))).thenReturn(savedAddress);

        Airport savedAirport = new Airport();
        savedAirport.setId(100);
        when(airportRepository.save(any(Airport.class))).thenReturn(savedAirport);

        Airport airport = new Airport();
        Address address = new Address();
        address.setCity("Bucharest");
        address.setStreet("Calea Bucurestilor");
        address.setNumber("23");
        airport.setAddress(address);
        Airport result = airportService.addAirport(airport);

        assertEquals(savedAirport.getId(), result.getId());
    }

    @Test
    @DisplayName("Can't update airport if address can't be updated.")
    void updateAirportCantUpdateAddress() {
        Airport airportForUpdate = new Airport();
        airportForUpdate.setId(3);
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.of(airportForUpdate));
        when(addressService.updateAddress(anyLong(),any(Address.class))).thenReturn(false);

        Airport airport = new Airport();
        Address address = new Address();
        airport.setAddress(address);
        boolean result = airportService.updateAirport(anyLong(), airport);

        assertFalse(result);
    }

    @Test
    @DisplayName("Can't update airport if airport id doesn't exist.")
    void updateAirportNotExists() {
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.empty());
        when(addressService.updateAddress(anyLong(),any(Address.class))).thenReturn(true);

        Airport airport = new Airport();
        Address address = new Address();
        airport.setAddress(address);
        boolean result = airportService.updateAirport(anyLong(), airport);

        assertFalse(result);
    }

    @Test
    @DisplayName("Airport and address updated.")
    void updateAirportSuccess() {
        Airport airportForUpdate = new Airport();
        airportForUpdate.setId(3);
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.of(airportForUpdate));
        when(addressService.updateAddress(anyLong(),any(Address.class))).thenReturn(true);

        Airport airport = new Airport();
        Address address = new Address();
        airport.setAddress(address);
        boolean result = airportService.updateAirport(anyLong(), airport);

        assertTrue(result);
    }

    @Test
    @DisplayName("Airport doesn't exists -> nothing to delete")
    void deleteAirportNotExists() {
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.empty());
        boolean result = airportService.deleteAirport(anyLong());
        assertFalse(result);
    }

    @Test
    @DisplayName("Airport has flights -> can't delete")
    void deleteAirportHasFlights() {
        Airport airport = new Airport();
        airport.setId(3);
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.of(airport));
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        when(flightService.getFlightsForAirport(anyLong())).thenReturn(flights);

        ForeignKeyException exception = assertThrows(ForeignKeyException.class,
                () -> airportService.deleteAirport(anyLong()));

        assertEquals("This airport has flights!", exception.getMessage());
    }

    @Test
    @DisplayName("Airport doesn't have flights -> can delete")
    void deleteAirportNoFlights() {
        Airport airport = new Airport();
        airport.setId(3);
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.of(airport));
        List<Flight> flights = new ArrayList<>();
        when(flightService.getFlightsForAirport(anyLong())).thenReturn(flights);

        boolean result = airportService.deleteAirport(anyLong());

        assertTrue(result);
    }

    @Test
    @DisplayName("find airport by name")
    void findAirportByName() {
        when(airportRepository.findAirportByName(anyString())).thenReturn(Optional.empty());

        Airport result = airportService.findAirportByName(anyString());

        assertNull(result.getName());
    }

    @Test
    void getAirportsByCity() {
        List<Airport> airports = new ArrayList<>();
        when(airportRepository.findAirportsByAddress_City(anyString())).thenReturn(airports);

        List<Airport> result = airportService.getAirportsByCity(anyString());

        assertEquals(airports,result);
    }

    @Test
    void getAirport() {
        Airport airport = new Airport();
        airport.setId(3);
        when(airportRepository.findAirportById(anyLong())).thenReturn(Optional.of(airport));

        Airport result = airportService.getAirport(anyLong());

        assertEquals(airport.getId(), result.getId());
    }

    @Test
    void getFlightsFromAirport() {
        List<Flight> flights = new ArrayList<>();
        when(flightService.getFlightsByAirport(anyLong())).thenReturn(flights);

        List<Flight> result = airportService.getFlightsFromAirport(anyLong());

        assertEquals(flights, result);
    }
}