package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findAirportById(Long aLong);

    Optional<Airport> findAirportByAddress_CountryAndAddress_City(String country,String city);

    Optional<Airport> findAirportByName(String name);

    Optional<Airport> findAirportByAddress_Id(long id);

    List<Airport> findAirportsByAddress_City(String city);

    @Override
    void deleteById(Long aLong);


}
