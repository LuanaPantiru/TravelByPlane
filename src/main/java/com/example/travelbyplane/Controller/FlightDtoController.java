package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Mapper.FlightMapper;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.*;
import com.example.travelbyplane.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/travelByPlane/flight")
public class FlightDtoController {

    @Autowired
    private FlightService flightService;
    @Autowired
    private AirportService airportService;
    @Autowired
    private FlightMapper mapper;
    @Autowired
    private PlaneTicketMapper planeTicketMapper;

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addFlight(@RequestBody FlightDto flightDto){
        Airport airportDeparture = airportService.findAirportByName(flightDto.getDepartureAirport());
        Airport airportArrival = airportService.findAirportByName(flightDto.getArrivalAirport());
        Flight flight = flightService.addFlight(mapper.mapToEntity(flightDto), airportDeparture, airportArrival);
        if(flight.getId() != 0){
            URI uri = WebMvcLinkBuilder.linkTo(FlightDtoController.class).slash(flight.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FlightDto>> getAllFlights(){
        List<Flight> flights = flightService.getAllFlights();
        if(!flights.isEmpty()){
            return ResponseEntity.ok(flights.stream().map(flight -> mapper.mapToDto(flight)).collect(Collectors.toList()));
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateFlight(@PathVariable long id, @RequestBody FlightDto flightDto){
        Flight flight = mapper.mapToEntity(flightDto);
        if(flightService.patchFlight(id, flight)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFlight(@PathVariable long id){
        if(flightService.deleteFlight(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{flightId}/plane/{planeId}")
    public ResponseEntity addPlaneToFlight(@PathVariable long flightId, @PathVariable long planeId){
        if(flightService.addPlane(flightId, planeId)){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{flightId}/addCabinCrew/{cabinCrewId}")
    public ResponseEntity addCabinCrewToFlight(@PathVariable long flightId, @PathVariable long cabinCrewId){
        if(flightService.addCabinCrew(flightId,cabinCrewId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{flightId}/eliminateCabinCrew/{cabinCrewId}")
    public ResponseEntity deleteCabinCrewToFlight(@PathVariable long flightId, @PathVariable long cabinCrewId){
        if(flightService.deleteEmployeeFromFlight(flightService.findFlight(flightId).getId(), cabinCrewId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{flightId}/tickets")
    public ResponseEntity<List<PlaneTicketDto>> getFlightTicket(@PathVariable long flightId){
        if(flightService.findFlight(flightId).getId() != 0){
            List<PlaneTicket> tickets = flightService.getFlightTicket(flightId);
            if(tickets.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets.stream().map(planeTicket -> planeTicketMapper.mapToDto(planeTicket)).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();

    }
}
