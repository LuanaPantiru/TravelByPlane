package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Client;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Model.TravelClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaneTicketRepository extends JpaRepository<PlaneTicket, Long> {

    @Override
    Optional<PlaneTicket> findById(Long id);

    @Override
    void deleteById(Long id);

    @Query(value = "select * from plane_ticket where client_id = :clientId", nativeQuery = true)
    List<PlaneTicket> findPlaneTicketByClient(long clientId);

    List<PlaneTicket> findPlaneTicketByFlightAndTravelClass(Flight flight, TravelClass travelClass);

    @Query(value = "select * from plane_ticket where flight_id = :flightId", nativeQuery = true)
    List<PlaneTicket> getPlaneTicketsByFlight(Long flightId);

    @Query(value = "select * from plane_ticket where flight_id = :flightId order by travel_class", nativeQuery = true)
    List<PlaneTicket> getPlaneTicketByFlightSort(long flightId);

}
