package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Address;
import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private FlightService flightService;


    public List<Airport> getAirports(){
        return airportRepository.findAll();
    }

    public Airport addAirport(Airport airport){
        Address address = new Address(airport.getAddress().getCountry(), airport.getAddress().getCity(), airport.getAddress().getStreet(), airport.getAddress().getNumber());
        Address savedAddress = addressService.addAddress(address);
        airport.setAddress(savedAddress);
        return airportRepository.save(airport);
    }

    public boolean updateAirport(long id,Airport airportUpdate){
        Optional<Airport> airport = airportRepository.findAirportById(id);
        if(addressService.updateAddress(id,airportUpdate.getAddress()) && airport.isPresent()){
            airportUpdate.setId(airport.get().getId());
            airportRepository.save(airportUpdate);
            return true;
        }
        return false;
    }

    public boolean deleteAirport(long id){
        Optional<Airport> airport = airportRepository.findAirportById(id);
        if(airport.isPresent()){
            if(!flightService.getFlightsForAirport(id).isEmpty()){
                throw new ForeignKeyException("This airport has flights!");
            }
            airportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Airport findAirportByName(String airportName){
        Optional<Airport> airport = airportRepository.findAirportByName(airportName);
        return airport.orElseGet(Airport::new);
    }

    public List<Airport> getAirportsByCity(String city){
        return airportRepository.findAirportsByAddress_City(city);
    }

    public Airport getAirport(long id){
        Optional<Airport> airport = airportRepository.findAirportById(id);
        return airport.orElseGet(Airport::new);
    }

    public List<Flight> getFlightsFromAirport(long id){
        return flightService.getFlightsByAirport(id);
    }
}
