package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Override
    Optional<Flight> findById(Long id);

    @Override
    void deleteById(Long id);

    @Query(value = "select * from flight where departure_date = :departureDate and arrival_date = :arrivalDate " +
            "and departure_time = :departureTime and arrival_time = :arrivalTime and " +
            "departure_airport_id = :departureAirportId and arrival_airport_id = :arrivalAirportId", nativeQuery = true)
    Optional<Flight> findFlightByDatesAndDestinations(String departureDate, String departureTime,
                                                      String arrivalDate, String arrivalTime,
                                                      long departureAirportId, long arrivalAirportId);

    @Query(value = "select * from flight where departure_date = :date and departure_airport_id = :departureAirport", nativeQuery = true)
    List<Flight> findFlightByDepartureDateAndDepartureAirport(String date, long departureAirport);

    @Query(value = "select count(*) from employee_distribution where flight_id = :flightId and employee_id = :employeeId", nativeQuery = true)
    Integer existsFlightWithEmployee(long flightId, long employeeId);

    @Query(value = "select * from flight where departure_airport_id = :airportId or arrival_airport_id = :airportId", nativeQuery = true)
    List<Flight> findFlightByDepartureAirportOrArrivalAirport(long airportId);

    @Query(value = "select * from flight where departure_airport_id = :flightId or arrival_airport_id = :flightId", nativeQuery = true)
    List<Flight> getFlightFromAirport(long flightId);

    @Query(value = "select * from flight join employee_distribution on id = flight_id where employee_id = :employeeId", nativeQuery = true)
    List<Flight> findFlightByEmployee(long employeeId);

    @Query(value = "select * from flight where plane_id = :planeId", nativeQuery = true)
    List<Flight> findFlightByPlane(long planeId);

}
