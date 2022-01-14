package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Exceptions.AvailabilityException;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Repository.FlightRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private PlaneService planeService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private PlaneTicketService planeTicketService;
    @InjectMocks
    private FlightService flightService;

    @Test
    @DisplayName("Can't add a flight when departure or arrival airport doesn't exist")
    void addFlightAirportNotExists() {

        Flight flight = new Flight();
        Airport airport = new Airport();
        flight.setDepartureAirport(airport);
        flight.setArrivalAirport(airport);
        Flight result = flightService.addFlight(flight, airport, airport );

        assertEquals(0, result.getId());
    }

    @Test
    @DisplayName("Flight added")
    void addFlightAirportSuccess() {
        Flight savedFlight = new Flight();
        savedFlight.setId(2);
        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        Flight flight = new Flight();
        Airport airportDeparture = new Airport();
        Airport airportArrival = new Airport();
        airportDeparture.setName("Departure Airport");
        flight.setDepartureAirport(airportDeparture);
        airportArrival.setName("Arrival Airport");
        flight.setArrivalAirport(airportArrival);
        Flight result = flightService.addFlight(flight, airportDeparture, airportArrival );

        assertNotEquals(0, result.getId());
    }

    @Test
    @DisplayName("Show all flights")
    void getAllFlights() {
        when(flightRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(flightService.getAllFlights().isEmpty());
    }

    @Test
    @DisplayName("Flight doesn't exist -> can't update")
    void patchFlightNotExists() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(flightService.patchFlight(anyLong(), new Flight()));
    }

    @Test
    @DisplayName("Flight updated")
    void patchFlightSuccess() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));

        Flight flight = new Flight();
        flight.setDepartureDate("16-02-2022");
        flight.setDepartureTime("16:00");
        flight.setArrivalDate("16-02-2022");
        flight.setArrivalTime("16:45");
        flight.setTime(45);
        assertTrue(flightService.patchFlight(anyLong(), flight));
    }

    @Test
    @DisplayName("Flight doesn't exist -> can't delete")
    void deleteFlightNotExists() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(flightService.deleteFlight(anyLong()));
    }

    @Test
    @DisplayName("Flight has passengers -> can't delete")
    void deleteFlightHasPassengers() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        List<PlaneTicket> planeTickets = new ArrayList<>();
        planeTickets.add(new PlaneTicket());
        when(planeTicketService.getPlaneTicketByFlight(anyLong())).thenReturn(planeTickets);

        ForeignKeyException exception = assertThrows(ForeignKeyException.class,
                () -> flightService.deleteFlight(anyLong()));

        assertEquals("This flight has passengers.", exception.getMessage());
    }

    @Test
    @DisplayName("Flight deleted")
    void deleteFlightSuccess() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        when(planeTicketService.getPlaneTicketByFlight(anyLong())).thenReturn(new ArrayList<>());

        assertTrue(flightService.deleteFlight(anyLong()));
    }

    @Test
    @DisplayName("Show all flights")
    void findFlight() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertEquals(0,flightService.findFlight(anyLong()).getId());
    }

    @Test
    @DisplayName("Can't add plane for a flight which doesn't exists or can't a plane which doesn't exist")
    void addPlaneFlightPlaneNotExist() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(planeService.findPlane(anyLong())).thenReturn(new Plane());

        assertFalse(flightService.addPlane(10,anyLong()));
    }

    @Test
    @DisplayName("Can't add an unavailable plane for a flight")
    void addPlaneNotAvailable() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        Plane plane = new Plane();
        plane.setId(100);
        plane.setNumberPlanes(1);
        when(planeService.findPlane(anyLong())).thenReturn(plane);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        when(flightRepository.findFlightByPlane(anyLong())).thenReturn(flights);

        AvailabilityException exception = assertThrows(AvailabilityException.class,
                () -> flightService.addPlane(10, anyLong()));

        assertEquals("All planes are not available!", exception.getMessage());
    }

    @Test
    @DisplayName("Add plane for a flight with success")
    void addPlaneSuccess() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        Plane plane = new Plane();
        plane.setId(100);
        plane.setNumberPlanes(1);
        when(planeService.findPlane(anyLong())).thenReturn(plane);
        when(flightRepository.findFlightByPlane(anyLong())).thenReturn(new ArrayList<>());

        assertTrue(flightService.addPlane(10,anyLong()));
    }

    @Test
    void findFlightByDatesAndDestinations() {
        when(flightRepository.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong())).thenReturn(Optional.empty());

        Flight result = flightService.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong());

        assertEquals(0, result.getId());
    }

    @Test
    void findFlightsByDepartureDateAndDepartureId() {
        when(flightRepository.findFlightByDepartureDateAndDepartureAirport(anyString(), anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong()).size());
    }

    @Test
    @DisplayName("Flight or employee doesn't exist.")
    void addCabinCrewNoFlightOrEmployee() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(employeeService.getEmployee(anyLong())).thenReturn(new Employee());

        assertFalse(flightService.addCabinCrew(100,anyLong()));
    }

    @Test
    @DisplayName("Employee unavailable.")
    void addCabinCrewEmployeeNotAvailable() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        Employee employee = new Employee();
        employee.setId(11);
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);
        when(flightRepository.existsFlightWithEmployee(anyLong(),anyLong())).thenReturn(1);

        AvailabilityException exception = assertThrows(AvailabilityException.class,
                () -> flightService.addCabinCrew(100,anyLong()));

       assertEquals("This employee already attends this flight.", exception.getMessage());
    }

    @Test
    @DisplayName("Add employee with success.")
    void addCabinCrewSuccess() {
        Flight flight = new Flight();
        flight.setEmployees(new HashSet<>());
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));
        Employee employee = new Employee();
        employee.setId(11);
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);
        when(flightRepository.existsFlightWithEmployee(anyLong(),anyLong())).thenReturn(0);

        assertTrue(flightService.addCabinCrew(100,anyLong()));
    }

    @Test
    @DisplayName("Can't remove a employee from a flight if employee or flight doesn't exist.")
    void deleteEmployeeFromFlightNotExistEmployeeOrFlight() {
        assertFalse(flightService.deleteEmployeeFromFlight(0,0));
    }

    @Test
    @DisplayName("No employee assigned for flight -> nothing to delete")
    void deleteEmployeeFromFlightNoEmployee() {
        when(flightRepository.existsFlightWithEmployee(anyLong(),anyLong())).thenReturn(0);

        assertFalse(flightService.deleteEmployeeFromFlight(10,1));
    }

    @Test
    @DisplayName("Employee remove from flight")
    void deleteEmployeeFromFlightSuccess() {
        when(flightRepository.existsFlightWithEmployee(anyLong(),anyLong())).thenReturn(1);
        Employee employee = new Employee();
        Flight flight = new Flight();
        Set<Employee> employeeSet = new HashSet<>();
        employeeSet.add(employee);
        flight.setEmployees(employeeSet);
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));

        assertTrue(flightService.deleteEmployeeFromFlight(10,1));
    }

    @Test
    void getFlightsByAirport() {
        when(flightRepository.findFlightByDepartureAirportOrArrivalAirport(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.getFlightsByAirport(anyLong()).size());
    }

    @Test
    void getFlightsForAirport() {
        when(flightRepository.getFlightFromAirport(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.getFlightsForAirport(anyLong()).size());
    }

    @Test
    void findFlightByEmployee() {
        when(flightRepository.findFlightByEmployee(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.findFlightByEmployee(anyLong()).size());
    }

    @Test
    void findByPlane() {
        when(flightRepository.findFlightByPlane(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.findByPlane(anyLong()).size());
    }

    @Test
    void getFlightTicket(){
        when(planeTicketService.getFlightTickets(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, flightService.getFlightTicket(anyLong()).size());
    }
}