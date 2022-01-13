package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Mapper.FlightMapper;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("travelByPlane/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addClient(@RequestBody Client client){
        Client clientAux = clientService.findByNameAndPassport(client.getFirstName(),client.getLastName(),client.getPassportNo());
        if(clientAux.getId() != 0){
            clientAux.setAccount(true);
            clientService.addClient(clientAux);
            return ResponseEntity.ok().build();
        }
        else{
            client.setAccount(true);
            clientService.addClient(client);
            URI uri = WebMvcLinkBuilder.linkTo(ClientController.class).slash(client.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(){
        List<Client> clients = clientService.getAllClients();
        if(!clients.isEmpty()){
            return ResponseEntity.ok(clients);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClient(@PathVariable long id,@RequestBody Client client){
        if(clientService.patchClient(id,client)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id){
        if (clientService.deleteClient(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{clientId}/searchFlight/{departureCity}/{arrivalCity}/{date}")
    public ResponseEntity<List<List<FlightDto>>> findFlight(@PathVariable long clientId, @PathVariable String departureCity, @PathVariable String arrivalCity, @PathVariable String date){
        Client client = clientService.findById(clientId);
        if(client.getId() != 0) {
            List<List<FlightDto>> flights = clientService.searchFlights(clientId, departureCity, arrivalCity, date);
            if(!flights.isEmpty()) {
                return ResponseEntity.ok(flights);
            }
            else{
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{clientId}/buyTickets", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> buyPlaneTicket(@PathVariable long clientId, @RequestBody List<PlaneTicketDto> planeTicketDtos) {
        if(clientService.findById(clientId).getId() != 0){
            if(!clientService.addPlaneTicket(clientId, planeTicketDtos)){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{clientId}/deleteTicket/{planeTicketId}")
    public ResponseEntity deleteTicket(@PathVariable long clientId,@PathVariable long planeTicketId){
        if(clientService.deletePlaneTicket(clientId, planeTicketId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{clientId}/tickets")
    public ResponseEntity<List<PlaneTicketDto>> showTickets(@PathVariable long clientId){
        if(clientService.findById(clientId).getId() == 0) {
            return ResponseEntity.notFound().build();
        }else {
            List<PlaneTicketDto> tickets = clientService.findTickets(clientId);
            if (!tickets.isEmpty()) {
                return ResponseEntity.ok(tickets);
            }
            return ResponseEntity.noContent().build();
        }
    }

}