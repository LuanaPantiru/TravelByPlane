package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Exceptions.AvailabilityException;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PlaneService planeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PlaneTicketService planeTicketService;

    public Flight addFlight(Flight flight, Airport airportDeparture, Airport airportArrival){
        if(airportDeparture.getName() != null && airportArrival.getName() != null){
            flight.setDepartureAirport(airportDeparture);
            flight.setArrivalAirport(airportArrival);
            return flightRepository.save(flight);
        }
        return new Flight();
    }

    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    }

    public boolean patchFlight(long id, Flight flight){
        Optional<Flight> flightForUpdate = flightRepository.findById(id);
        if(flightForUpdate.isPresent()){
            flightForUpdate.get().setDepartureDate(flight.getDepartureDate());
            flightForUpdate.get().setDepartureTime(flight.getDepartureTime());
            flightForUpdate.get().setArrivalDate(flight.getArrivalDate());
            flightForUpdate.get().setArrivalTime(flight.getArrivalTime());
            flightForUpdate.get().setTime(flight.getTime());
            flightRepository.save(flightForUpdate.get());
            return true;
        }
        return false;
    }

    public boolean deleteFlight(long id){
        Optional<Flight> flightForDelete = flightRepository.findById(id);
        if(flightForDelete.isPresent()){
            List<PlaneTicket> planeTickets = planeTicketService.getPlaneTicketByFlight(id);
            if(!planeTickets.isEmpty()){
                throw new ForeignKeyException("This flight has passengers.");
            }
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Flight findFlight(long id){
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.isEmpty() ? new Flight() : flight.get();
    }

    public boolean addPlane(long flightId, long planeId){
        Optional<Flight> flight = flightRepository.findById(flightId);
        Plane plane = planeService.findPlane(planeId);
        if(flight.isPresent() && plane.getId() != 0){
            if(available(plane)){
                flight.get().setPlane(plane);
                flightRepository.save(flight.get());
                return true;
            }
            throw new AvailabilityException("All planes are not available!");
        }
        return false;
    }

    public Flight findFlightByDatesAndDestinations(String departureDate, String departureTime, String arrivalDate, String arrivalTime, long departureAirport, long arrivalAirport){
        Optional<Flight> optFlight = flightRepository.findFlightByDatesAndDestinations(departureDate, departureTime, arrivalDate, arrivalTime, departureAirport, arrivalAirport);
        return optFlight.isEmpty() ? new Flight() : optFlight.get();
    }

    public List<Flight> findFlightsByDepartureDateAndDepartureId(String date, long departureAirportId){
        return flightRepository.findFlightByDepartureDateAndDepartureAirport(date, departureAirportId);
    }

    public boolean addCabinCrew(long flightId, long employeeId){
        Optional<Flight> flight = flightRepository.findById(flightId);
        Employee employee = employeeService.getEmployee(employeeId);
        if(flight.isPresent() && employee.getId() != 0){
            Integer count = flightRepository.existsFlightWithEmployee(flightId, employeeId);
            if(count == 0) {
                flight.get().addEmployee(employee);
                flightRepository.save(flight.get());
                return true;
            }
            throw new AvailabilityException("This employee already attends this flight.");
        }
        return false;
    }

    public boolean deleteEmployeeFromFlight(long flightId, long employeeId){
        if(flightId != 0 && employeeId !=0){
            if(flightRepository.existsFlightWithEmployee(flightId, employeeId) == 1){
                Employee employee = employeeService.getEmployee(employeeId);
                Flight flight = flightRepository.findById(flightId).get();
                flight.removeEmployee(employee);
                flightRepository.save(flight);
                return true;
            }
        }
        return false;
    }

    public List<Flight> getFlightsByAirport(long airportId){
        return flightRepository.findFlightByDepartureAirportOrArrivalAirport(airportId);
    }

    public List<Flight> getFlightsForAirport(long airportId){
        return flightRepository.getFlightFromAirport(airportId);
    }

    public List<Flight> findFlightByEmployee(long employeeId){
        return flightRepository.findFlightByEmployee(employeeId);
    }

    public List<Flight> findByPlane(long planeId){
        return flightRepository.findFlightByPlane(planeId);
    }

    public List<PlaneTicket> getFlightTicket(long flightId){
        return planeTicketService.getFlightTickets(flightId);
    }
    private boolean available(Plane plane) {
        List<Flight> flights = flightRepository.findFlightByPlane(plane.getId());;
        return flights.size() < plane.getNumberPlanes();
    }


}
