package com.example.travelbyplane.Service;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Exceptions.ClientBadActionException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Mapper.FlightMapper;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PlaneTicketService planeTicketService;
    @Autowired
    private PlaneTicketMapper planeTicketMapper;
    @Autowired
    private AirportService airportService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private EmployeeService employeeService;

    public Client addClient(Client client){
        return clientRepository.save(client);
    }

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    public boolean patchClient(long id, Client client){
        Optional<Client> clientForUpdate = clientRepository.findClientById(id);
        if(clientForUpdate.isPresent()){
            if(!clientForUpdate.get().getAccount()) {
                throw new ClientBadActionException("A client without account can't update his/her profile.");
            }
            clientForUpdate.get().setFirstName(client.getFirstName());
            clientForUpdate.get().setLastName(client.getLastName());
            clientForUpdate.get().setPassportNo(client.getPassportNo());
            clientRepository.save(clientForUpdate.get());
            return true;
        }
        return false;
    }

    public boolean deleteClient(long id){
        Optional<Client> client = clientRepository.findClientById(id);
        if(client.isPresent()){
            if(!client.get().getAccount()){
                throw new ClientBadActionException("A client without account can't delete his/her profile");
            }
//            if (!planeTicketService.getPlaneTicketsByClient(id).isEmpty()) {
//                throw new ForeignKeyException("This client has tickets.");
//            }
            if(hasTickets(planeTicketService.getPlaneTicketsByClient(id))){
                throw new ForeignKeyException("This client has tickets.");
            }
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Client findByNameAndPassport(String firstName, String lastName, String passport){
        Optional<Client> client = clientRepository.findClientByFirstNameAndLastNameAndPassportNo(firstName,lastName,passport);
        return client.orElseGet(Client::new);
    }

    public Client findById(long id){
        Optional<Client> client = clientRepository.findClientById(id);
        return client.orElseGet(Client::new);
    }

    public List<List<FlightDto>> searchFlights(long clientId, String departureCity, String arrivalCity, String date){
        Optional<Client> client = clientRepository.findClientById(clientId);
        List<List<FlightDto>> flights = new ArrayList<List<FlightDto>>();
        if(client.isPresent()) {
            if (!client.get().getAccount()) {
                throw new ClientBadActionException("A client without account can't search flights.");
            }
            List<Airport> departureAirports = airportService.getAirportsByCity(departureCity);
            List<Airport> arrivalAirports = airportService.getAirportsByCity(arrivalCity);
            if (!departureAirports.isEmpty() && !arrivalAirports.isEmpty()) {
                for (Airport departureAirport : departureAirports) {
                    for (Airport arrivalAirport : arrivalAirports) {
                        List<FlightDto> auxFlights = findFlights(date, departureAirport.getId(), arrivalAirport.getId(), 0, new ArrayList<FlightDto>());
                        if (!auxFlights.isEmpty()) {
                            flights.add(auxFlights);
                        }
                    }
                }
            }
        }
        return flights;
    }

    public boolean addPlaneTicket(long clientId, List<PlaneTicketDto> planeTicketDtos){
        Optional<Client> optClient = clientRepository.findClientById(clientId);
        if(optClient.isPresent()) {
            if (!optClient.get().getAccount()) {
                throw new ClientBadActionException("A client without account can't buy tickets.");
            }
            List<PlaneTicket> planeTickets = new ArrayList<>();
            for (PlaneTicketDto planeTicketDto : planeTicketDtos) {
                Optional<Client> optional = clientRepository.findClientByPassportNo(planeTicketDto.getPassportNo());
                Client client;
                if (optional.isPresent()) {
                    client = optional.get();
                }else
                    {
                        client = new Client(planeTicketDto.getFirstName(), planeTicketDto.getLastName(), planeTicketDto.getPassportNo(), false);
                        client.setPlaneTickets(new ArrayList<>());
                        client = clientRepository.save(client);
                    }
                PlaneTicket planeTicket = planeTicketMapper.mapToEntity(planeTicketDto);
                Airport departureAirport = airportService.findAirportByName(planeTicketDto.getDepartureAirport());
                Airport arrivalAirport = airportService.findAirportByName(planeTicketDto.getArrivalAirport());
                if(departureAirport.getId() !=0 && arrivalAirport.getId() != 0) {
                    Flight flight = flightService.findFlightByDatesAndDestinations(planeTicketDto.getDepartureDate(), planeTicketDto.getDepartureTime(), planeTicketDto.getArrivalDate(), planeTicketDto.getArrivalTime(), departureAirport.getId(), arrivalAirport.getId());
                    if (flight.getId() != 0) {
                        planeTicket.setClient(client);
                        planeTicket.setFlight(flight);
                        if (validate(planeTicket)) {
                            planeTickets.add(planeTicket);
                        }
                    }
                }
            }
            if (planeTickets.size() == planeTicketDtos.size()) {
                for (PlaneTicket ticket : planeTickets) {
                    ticket = planeTicketService.addPlaneTicket(ticket);
                    ticket.getClient().addTicket(ticket);
                }
                return true;
            }
        }
        return false;
    }

    public boolean deletePlaneTicket(long clientId, long planeTicketId){
        PlaneTicket planeTicket = planeTicketService.findTicketById(planeTicketId);
        if(planeTicket.getId() != 0) {
            if (!planeTicket.getClient().getAccount()) {
                throw new ClientBadActionException("Client needs to have account to delete a ticket!");
            }
            if(planeTicket.getClient().getId() != clientId){
                throw new ClientBadActionException("Client can delete own ticket!");
            }
            planeTicket.getClient().removeTicket(planeTicket);
            planeTicketService.deletePlaneTicket(planeTicketId);
            return true;
        }
        return false;
    }

    public List<PlaneTicketDto> findTickets(long id){
        if(clientRepository.findClientById(id).get().getAccount()){
            List<PlaneTicket> tickets = planeTicketService.getPlaneTicketsByClient(id);
            return tickets.stream().map(ticket -> planeTicketMapper.mapToDto(ticket)).collect(Collectors.toList());
        }
        throw new ClientBadActionException("Client without account can't see own tickets");

    }

    private List<FlightDto> findFlights(String date,long departureId, long arrivalId, Integer nrTransfer, List<FlightDto> flightDtos){
        List<Flight> flights = flightService.findFlightsByDepartureDateAndDepartureId(date, departureId);
        if(flights.isEmpty() && nrTransfer > 0){
            String nextDate = nextDay(date);
            flights = flightService.findFlightsByDepartureDateAndDepartureId(nextDate,departureId);
        }
        for(Flight flight : flights){
            if(hasPlaneAndCabinCrew(flight)) {
                flightDtos.add(flightMapper.mapToDto(flight));
                if (flight.getArrivalAirport().getId() == arrivalId && nrTransfer < 3) {
                    return flightDtos;
                } else {
                    return findFlights(date, flight.getArrivalAirport().getId(), arrivalId, nrTransfer + 1, flightDtos);
                }
            }else {
                break;
            }
        }
        return new ArrayList<>();
    }

    private boolean hasPlaneAndCabinCrew(Flight flight) {
        if(flight.getPlane() != null){
            List<Employee> employees = employeeService.findByFlight(flight.getId());
            boolean hasPilot = false;
            boolean hasSeniorAttendant = false;
            for(Employee employee: employees){
                if(employee.getPosition().equals(Position.PILOT)){
                    hasPilot = true;
                }
                if(employee.getPosition().equals(Position.SENIOR_FLIGHT_ATTENDANT)){
                    hasSeniorAttendant = true;
                }
            }
            return hasPilot && hasSeniorAttendant;
        }
        return false;
    }

    private String nextDay(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        String day = String.valueOf(localDate.getDayOfMonth()+1);
        day = day.length() == 1 ? "0" + day : day;
        String month = String.valueOf(localDate.getMonthValue());
        month = month.length() == 1 ? "0" + month : month;
        String  year = String.valueOf(localDate.getYear());
        return day + "-" + month + "-" + year;
    }

    private boolean validate(PlaneTicket planeTicket){
        List<PlaneTicket> planeTickets = planeTicketService.findTicketsByFlightAndTravelClass(planeTicket.getFlight(),planeTicket.getTravelClass());
        Integer nrSeats;
        switch (planeTicket.getTravelClass()){
            case FIRST:
                nrSeats = planeTicket.getFlight().getPlane().getFirstSeats();
                break;
            case BUSINESS:
                nrSeats = planeTicket.getFlight().getPlane().getBusinessSeats();
                break;
            default:
                nrSeats = planeTicket.getFlight().getPlane().getEconomySeats();
        }
        if(planeTickets.size() < nrSeats){
            return true;
        }
        return false;
    }

    private boolean hasTickets(List<PlaneTicket> tickets){
        LocalDate date = LocalDate.now();
        Integer day = date.getDayOfMonth();
        Integer month = date.getMonthValue();
        Integer year = date.getYear();
        for(PlaneTicket ticket : tickets){
            String ticketDate = ticket.getFlight().getDepartureDate();
            Integer ticketDay = Integer.valueOf(ticketDate.substring(0,2));
            Integer ticketMonth = Integer.valueOf(ticketDate.substring(3,5));
            Integer ticketYear = Integer.valueOf(ticketDate.substring(6));
            if(ticketYear > year || ticketMonth > month || ticketDay >= day ){
                return true;
            }
        }
        return false;
    }
}
