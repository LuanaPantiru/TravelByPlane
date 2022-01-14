package com.example.travelbyplane.Service;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Exceptions.ClientBadActionException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Mapper.FlightMapper;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PlaneTicketService planeTicketService;
    @Mock
    private PlaneTicketMapper planeTicketMapper;
    @Mock
    private AirportService airportService;
    @Mock
    private FlightService flightService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private FlightMapper flightMapper;
    @InjectMocks
    private ClientService clientService;

    @Test
    @DisplayName("Add client")
    void addClient() {
        Client savedClient = new Client();
        savedClient.setId(3);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Client result = clientService.addClient(new Client());

        assertEquals(savedClient.getId(), result.getId());
    }

    @Test
    @DisplayName("Show all clients")
    void getAllClients() {
        List<Client> clients = new ArrayList<>();
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClients();

        assertEquals(clients, result);
    }

    @Test
    @DisplayName("Client doesn't exists.")
    void patchClientNotExists() {
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.empty());

        boolean result = clientService.patchClient(anyLong(),new Client());

        assertFalse(result);
    }

    @Test
    @DisplayName("Client doesn't have account.")
    void patchClientNoAccount() {
        Client client = new Client();
        client.setAccount(false);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.patchClient(anyLong(),new Client()));

        assertEquals("A client without account can't update his/her profile.",exception.getMessage());
    }

    @Test
    @DisplayName("Client updated.")
    void patchClientSuccess() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        boolean result = clientService.patchClient(anyLong(),new Client());

        assertTrue(result);
    }

    @Test
    @DisplayName("Client doesn't exists.")
    void deleteClientNotExists() {
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.empty());

        assertFalse(clientService.deleteClient(anyLong()));
    }

    @Test
    @DisplayName("Client doesn't have account.")
    void deleteClientNoAccount() {
        Client client = new Client();
        client.setAccount(false);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.deleteClient(anyLong()));

        assertEquals("A client without account can't delete his/her profile",exception.getMessage());
    }

    @Test
    @DisplayName("Client has tickets -> can't delete.")
    void deleteClientHasTickets() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<PlaneTicket> planeTickets = new ArrayList<>();
        PlaneTicket planeTicket = new PlaneTicket();
        planeTicket.setFlight(new Flight());
        planeTicket.getFlight().setDepartureDate("16-02-2022");
        planeTickets.add(planeTicket);
        when(planeTicketService.getPlaneTicketsByClient(anyLong())).thenReturn(planeTickets);

        ForeignKeyException exception = assertThrows(ForeignKeyException.class,
                () -> clientService.deleteClient(anyLong()));

        assertEquals("This client has tickets.",exception.getMessage());
    }

    @Test
    @DisplayName("Client deleted.")
    void deleteClientSuccess() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<PlaneTicket> planeTickets = new ArrayList<>();
        PlaneTicket planeTicket = new PlaneTicket();
        planeTicket.setFlight(new Flight());
        planeTicket.getFlight().setDepartureDate("10-01-2022");
        planeTickets.add(planeTicket);
        when(planeTicketService.getPlaneTicketsByClient(anyLong())).thenReturn(planeTickets);

        assertTrue(clientService.deleteClient(anyLong()));
    }

    @Test
    @DisplayName("Find client by full name and passport number.")
    void findByNameAndPassport() {
        when(clientRepository.findClientByFirstNameAndLastNameAndPassportNo(anyString(),anyString(),anyString())).thenReturn(Optional.empty());

        Client result = clientService.findByNameAndPassport(anyString(),anyString(),anyString());

        assertNull(result.getPassportNo());
    }

    @Test
    @DisplayName("Find client by id")
    void findById(){
        Client client = new Client();
        client.setId(1);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        Client result = clientService.findById(anyLong());

        assertEquals(client.getId(),result.getId());
    }

    @Test
    @DisplayName("Client doesn't exists.")
    void searchFlightsNoClient() {
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.empty());
        List<List<FlightDto>> flights = clientService.searchFlights(anyLong(),"Bucharest","Iasi","23-02-2022");
        assertTrue(flights.isEmpty());
    }

    @Test
    @DisplayName("Client doesn't have account.")
    void searchFlightsClientNoAccount() {
        Client client = new Client();
        client.setAccount(false);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.searchFlights(anyLong(),"Bucharest","Iasi","23-02-2022"));
        assertEquals("A client without account can't search flights.", exception.getMessage());
    }

    @Test
    @DisplayName("Departure/arrival airport doesn't exists.")
    void searchFlightsSuccessNoAirports() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<List<FlightDto>> flights = clientService.searchFlights(anyLong(), "", "", "");
        assertNotNull(flights);
    }

    @Test
    @DisplayName("No rute")
    void searchFlightsSuccessNoFlights() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport());
        when(airportService.getAirportsByCity(anyString())).thenReturn(airports);
        when(flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong())).thenReturn(new ArrayList<>());
        assertTrue(clientService.searchFlights(anyLong(), "", "", "").isEmpty());
    }

    @Test
    @DisplayName("Can't show a rute without plane.")
    void searchFlightsFlightsWithoutPlane() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport());
        when(airportService.getAirportsByCity(anyString())).thenReturn(airports);
        // private findFlights
        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        flights.add(flight);
        when(flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong())).thenReturn(flights);
        assertTrue(clientService.searchFlights(anyLong(), "Bucharest", "Iasi", "").isEmpty());
    }

    @Test
    @DisplayName("Can't show a rute without cabin crew.")
    void searchFlightsFlightsWithoutPilot() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport());
        when(airportService.getAirportsByCity(anyString())).thenReturn(airports);
        // private findFlights
        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        Plane plane = new Plane();
        plane.setId(1);
        flight.setPlane(plane);
        flights.add(flight);
        when(flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong())).thenReturn(flights);
        // private hasPlaneAndCabinCrew
        when(employeeService.findByFlight(anyLong())).thenReturn(new ArrayList<>());

        assertTrue(clientService.searchFlights(anyLong(), "Bucharest", "Iasi", "").isEmpty());
    }

    @Test
    @DisplayName("Show rute without transfer")
    void searchFlightsFlightsSuccessWithoutTransfer() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<Airport> departureAirports = new ArrayList<>();
        departureAirports.add(new Airport());
        List<Airport> arrivalAirports = new ArrayList<>();
        Airport arrivalAirport = new Airport();
        arrivalAirport.setId(3);
        arrivalAirports.add(arrivalAirport);
        when(airportService.getAirportsByCity(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    return departureAirports;
                }
                else {
                    return arrivalAirports;
                }
            }
        });
        // private findFlights
        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        flight.setId(12);
        flight.setArrivalAirport(arrivalAirport);
        Plane plane = new Plane();
        plane.setId(1);
        flight.setPlane(plane);
        flights.add(flight);
        when(flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong())).thenReturn(flights);
        // private hasPlaneAndCabinCrew
        Employee pilot = new Employee();
        pilot.setPosition(Position.PILOT);
        Employee attendant = new Employee();
        attendant.setPosition(Position.SENIOR_FLIGHT_ATTENDANT);
        List<Employee> employees = new ArrayList<>();
        employees.add(pilot);
        employees.add(attendant);
        when(employeeService.findByFlight(anyLong())).thenReturn(employees);

        assertFalse(clientService.searchFlights(anyLong(), "Bucharest", "Iasi", "").isEmpty());
    }

    @Test
    @DisplayName("Show rute with transfer")
    void searchFlightsFlightsSuccessWithTransfer() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        List<Airport> departureAirports = new ArrayList<>();
        departureAirports.add(new Airport());
        List<Airport> arrivalAirports = new ArrayList<>();
        Airport airport = new Airport();
        airport.setId(4);
        arrivalAirports.add(airport);
        when(airportService.getAirportsByCity(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    return departureAirports;
                }
                else {
                    return arrivalAirports;
                }
            }
        });
        // private findFlights
        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        flight.setId(12);
        Airport arrivalAirport = new Airport();
        arrivalAirport.setId(3);
        arrivalAirports.add(arrivalAirport);
        flight.setArrivalAirport(arrivalAirport);
        Plane plane = new Plane();
        plane.setId(1);
        flight.setPlane(plane);
        flights.add(flight);
        when(flightService.findFlightsByDepartureDateAndDepartureId(anyString(), anyLong())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0 || count == 2){
                    count ++;
                    return flights;
                }
                else {
                    count ++;
                    flights.get(0).setArrivalAirport(airport);
                    return new ArrayList<>();
                }

            }
        });
        // private hasPlaneAndCabinCrew
        Employee pilot = new Employee();
        pilot.setPosition(Position.PILOT);
        Employee attendant = new Employee();
        attendant.setPosition(Position.SENIOR_FLIGHT_ATTENDANT);
        List<Employee> employees = new ArrayList<>();
        employees.add(pilot);
        employees.add(attendant);
        when(employeeService.findByFlight(anyLong())).thenReturn(employees);

        assertFalse(clientService.searchFlights(anyLong(), "Bucharest", "Iasi", "23-02-2022").isEmpty());
    }

    @Test
    @DisplayName("Client who want to buy tickets doesn't exist.")
    void addPlaneTicketNoClient() {
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.empty());

        assertFalse(clientService.addPlaneTicket(anyLong(),new ArrayList<>()));
    }

    @Test
    @DisplayName("Client who want to buy tickets need to have account.")
    void addPlaneTicketClientNoAccount() {
        Client client = new Client();
        client.setAccount(false);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.addPlaneTicket(anyLong(),new ArrayList<>()));

        assertEquals("A client without account can't buy tickets.", exception.getMessage());
    }

    @Test
    @DisplayName("Client buy ticket for another person, but airports don't exists")
    void addPlaneTicketForAnotherPersonNoAirports() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.findClientByPassportNo(anyString())).thenReturn(Optional.empty());
        Client anotherClient = new Client();
        anotherClient.setAccount(false);
        when(clientRepository.save(any(Client.class))).thenReturn(anotherClient);
        when(airportService.findAirportByName(anyString())).thenReturn(new Airport());

        List<PlaneTicketDto> planeTicketDtos = new ArrayList<>();
        PlaneTicketDto planeTicketDto = new PlaneTicketDto();
        planeTicketDto.setPassportNo("123456789");
        planeTicketDto.setDepartureAirport("Bucharest Airport");
        planeTicketDto.setArrivalAirport("Iasi Airport");
        planeTicketDtos.add(planeTicketDto);
        boolean result = clientService.addPlaneTicket(anyLong(),planeTicketDtos);

        assertFalse(result);
    }

    @Test
    @DisplayName("Buy ticket for a flight which doesn't exists.")
    void addPlaneTicketNoFlight() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.findClientByPassportNo(anyString())).thenReturn(Optional.of(client));
        when(airportService.findAirportByName(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    Airport departureAirport = new Airport();
                    departureAirport.setId(2);
                    return departureAirport;
                }
                else {
                    Airport arrivalAirport = new Airport();
                    arrivalAirport.setId(3);
                    return arrivalAirport;
                }

            }
        });
        when(flightService.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong())).thenReturn(new Flight());

        List<PlaneTicketDto> planeTicketDtos = new ArrayList<>();
        PlaneTicketDto planeTicketDto = new PlaneTicketDto();
        planeTicketDto.setPassportNo("123456789");
        planeTicketDto.setDepartureAirport("Bucharest Airport");
        planeTicketDto.setDepartureDate("16-02-2022");
        planeTicketDto.setDepartureTime("16:00");
        planeTicketDto.setArrivalAirport("Iasi Airport");
        planeTicketDto.setArrivalDate("16-02-2022");
        planeTicketDto.setArrivalTime("16:45");
        planeTicketDtos.add(planeTicketDto);
        boolean result = clientService.addPlaneTicket(anyLong(),planeTicketDtos);

        assertFalse(result);
    }

    @Test
    @DisplayName("Not seats left.")
    void addPlaneTicketNotValidate() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.findClientByPassportNo(anyString())).thenReturn(Optional.of(client));
        PlaneTicket planeTicket = new PlaneTicket();
        planeTicket.setTravelClass(TravelClass.FIRST);
        when(planeTicketMapper.mapToEntity(any(PlaneTicketDto.class))).thenReturn(planeTicket);
        when(airportService.findAirportByName(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    Airport departureAirport = new Airport();
                    departureAirport.setId(2);
                    return departureAirport;
                }
                else {
                    Airport arrivalAirport = new Airport();
                    arrivalAirport.setId(3);
                    return arrivalAirport;
                }

            }
        });
        Flight flight = new Flight();
        flight.setId(1);
        Plane plane = new Plane();
        plane.setFirstSeats(0);
        flight.setPlane(plane);
        when(flightService.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong())).thenReturn(flight);
        List<PlaneTicket> planeTickets = new ArrayList<>();
        planeTickets.add(new PlaneTicket());
        when(planeTicketService.findTicketsByFlightAndTravelClass(any(Flight.class),any(TravelClass.class))).thenReturn(planeTickets);

        List<PlaneTicketDto> planeTicketDtos = new ArrayList<>();
        PlaneTicketDto planeTicketDto = new PlaneTicketDto();
        planeTicketDto.setPassportNo("123456789");
        planeTicketDto.setDepartureAirport("Bucharest Airport");
        planeTicketDto.setDepartureDate("16-02-2022");
        planeTicketDto.setDepartureTime("16:00");
        planeTicketDto.setArrivalAirport("Iasi Airport");
        planeTicketDto.setArrivalDate("16-02-2022");
        planeTicketDto.setArrivalTime("16:45");
        planeTicketDto.setTravelClass(TravelClass.FIRST);
        planeTicketDtos.add(planeTicketDto);
        boolean result = clientService.addPlaneTicket(anyLong(),planeTicketDtos);

        assertFalse(result);
    }

    @Test
    @DisplayName("Can't buy all tickets wanted.")
    void addPlaneTicketNotAll() {
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.findClientByPassportNo(anyString())).thenReturn(Optional.of(client));
        when(planeTicketMapper.mapToEntity(any(PlaneTicketDto.class))).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    PlaneTicket planeTicket = new PlaneTicket();
                    planeTicket.setTravelClass(TravelClass.ECONOMY);
                    return planeTicket;
                }
                else {
                    return new PlaneTicket();
                }

            }
        });
        when(airportService.findAirportByName(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count % 2 == 0){
                    count ++;
                    Airport departureAirport = new Airport();
                    departureAirport.setId(2);
                    return departureAirport;
                }
                else {
                    count ++;
                    Airport arrivalAirport = new Airport();
                    arrivalAirport.setId(3);
                    return arrivalAirport;
                }

            }
        });
        when(flightService.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    Flight flight = new Flight();
                    flight.setId(1);
                    Plane plane = new Plane();
                    plane.setEconomySeats(100);
                    flight.setPlane(plane);
                    return flight;
                }
                else {
                    return new Flight();
                }

            }
        });
        List<PlaneTicket> planeTickets = new ArrayList<>();
        planeTickets.add(new PlaneTicket());
        when(planeTicketService.findTicketsByFlightAndTravelClass(any(Flight.class),any(TravelClass.class))).thenReturn(planeTickets);

        List<PlaneTicketDto> planeTicketDtos = new ArrayList<>();
        PlaneTicketDto planeTicketDto = new PlaneTicketDto();
        planeTicketDto.setPassportNo("123456789");
        planeTicketDto.setDepartureAirport("Bucharest Airport");
        planeTicketDto.setDepartureDate("16-02-2022");
        planeTicketDto.setDepartureTime("16:00");
        planeTicketDto.setArrivalAirport("Iasi Airport");
        planeTicketDto.setArrivalDate("16-02-2022");
        planeTicketDto.setArrivalTime("16:45");
        planeTicketDto.setTravelClass(TravelClass.FIRST);
        planeTicketDtos.add(planeTicketDto);
        planeTicketDtos.add(planeTicketDto);
        boolean result = clientService.addPlaneTicket(anyLong(),planeTicketDtos);

        assertFalse(result);
    }

    @Test
    @DisplayName("Buy ticket with success")
    void addPlaneTicketSuccess(){
        Client client = new Client();
        client.setAccount(true);
        client.setPlaneTickets(new ArrayList<>());
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.findClientByPassportNo(anyString())).thenReturn(Optional.of(client));
        PlaneTicket planeTicket = new PlaneTicket();
        planeTicket.setTravelClass(TravelClass.BUSINESS);
        when(planeTicketMapper.mapToEntity(any(PlaneTicketDto.class))).thenReturn(planeTicket);
        when(airportService.findAirportByName(anyString())).thenAnswer(new Answer() {
            private int count = 0;
            public Object answer(InvocationOnMock invocation){
                if(count == 0){
                    count ++;
                    Airport departureAirport = new Airport();
                    departureAirport.setId(2);
                    return departureAirport;
                }
                else {
                    Airport arrivalAirport = new Airport();
                    arrivalAirport.setId(3);
                    return arrivalAirport;
                }

            }
        });
        Flight flight = new Flight();
        flight.setId(1);
        Plane plane = new Plane();
        plane.setBusinessSeats(10);
        flight.setPlane(plane);
        when(flightService.findFlightByDatesAndDestinations(anyString(),anyString(),anyString(),anyString(),anyLong(),anyLong())).thenReturn(flight);
        List<PlaneTicket> planeTickets = new ArrayList<>();
        planeTickets.add(new PlaneTicket());
        when(planeTicketService.findTicketsByFlightAndTravelClass(any(Flight.class),any(TravelClass.class))).thenReturn(planeTickets);
        when(planeTicketService.addPlaneTicket(any(PlaneTicket.class))).thenReturn(planeTicket);

        List<PlaneTicketDto> planeTicketDtos = new ArrayList<>();
        PlaneTicketDto planeTicketDto = new PlaneTicketDto();
        planeTicketDto.setPassportNo("123456789");
        planeTicketDto.setDepartureAirport("Bucharest Airport");
        planeTicketDto.setDepartureDate("16-02-2022");
        planeTicketDto.setDepartureTime("16:00");
        planeTicketDto.setArrivalAirport("Iasi Airport");
        planeTicketDto.setArrivalDate("16-02-2022");
        planeTicketDto.setArrivalTime("16:45");
        planeTicketDto.setTravelClass(TravelClass.BUSINESS);
        planeTicketDtos.add(planeTicketDto);
        boolean result = clientService.addPlaneTicket(anyLong(),planeTicketDtos);

        assertTrue(result);
    }

    @Test
    @DisplayName("Ticket doesn't exist.")
    void deletePlaneTicketNotExists() {
        when(planeTicketService.findTicketById(anyLong())).thenReturn(new PlaneTicket());

        assertFalse(clientService.deletePlaneTicket(1,anyLong()));
    }
    @Test
    @DisplayName("A client without account can't delete a ticket.")
    void deletePlaneTicketClientNotAccount() {
        PlaneTicket ticket = new PlaneTicket();
        Client client = new Client();
        client.setAccount(false);
        ticket.setId(4);
        ticket.setClient(client);
        when(planeTicketService.findTicketById(anyLong())).thenReturn(ticket);

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.deletePlaneTicket(1,anyLong()));

        assertEquals("Client needs to have account to delete a ticket!", exception.getMessage());
    }

    @Test
    @DisplayName("A client can't delete tickets that are not his/her.")
    void deletePlaneTicketNotOwn() {
        PlaneTicket ticket = new PlaneTicket();
        Client client = new Client();
        client.setAccount(true);
        client.setId(10);
        ticket.setId(4);
        ticket.setClient(client);
        when(planeTicketService.findTicketById(anyLong())).thenReturn(ticket);

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.deletePlaneTicket(1,anyLong()));

        assertEquals("Client can delete own ticket!", exception.getMessage());
    }

    @Test
    @DisplayName("Ticket delete with success")
    void deletePlaneTicketSuccess() {
        PlaneTicket ticket = new PlaneTicket();
        Client client = new Client();
        client.setAccount(true);
        client.setId(1);
        List<PlaneTicket> tickets = new ArrayList<>();
        tickets.add(ticket);
        client.setPlaneTickets(tickets);
        ticket.setId(4);
        ticket.setClient(client);
        when(planeTicketService.findTicketById(anyLong())).thenReturn(ticket);

        assertTrue(clientService.deletePlaneTicket(1,anyLong()));
    }

    @Test
    void findTicketsClientNoAccount(){
        Client client = new Client();
        client.setAccount(false);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));

        ClientBadActionException exception = assertThrows(ClientBadActionException.class,
                () -> clientService.findTickets(anyLong()));

        assertEquals("Client without account can't see own tickets", exception.getMessage());
    }

    @Test
    void findTicketsClient(){
        Client client = new Client();
        client.setAccount(true);
        when(clientRepository.findClientById(anyLong())).thenReturn(Optional.of(client));
        when(planeTicketService.getPlaneTicketsByClient(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0,clientService.findTickets(anyLong()).size());
    }
}