package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.AirportDto;
import com.example.travelbyplane.Mapper.AirportMapper;
import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Service.AirportService;
import com.example.travelbyplane.Service.FlightService;
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
@RequestMapping("travelByPlane/airport")
public class AirportDtoController {

    @Autowired
    private AirportService airportService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private AirportMapper mapper;

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAirports(){
        List<Airport> airports = airportService.getAirports();
        if(!airports.isEmpty()){
            return ResponseEntity.ok(airports.stream().map(airport -> mapper.mapToDto(airport)).collect(Collectors.toList()));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAirport(@RequestBody AirportDto airportDto){
        Airport airport = mapper.mapToEntity(airportDto);
        airportService.addAirport(airport);
        URI uri = WebMvcLinkBuilder.linkTo(AirportDtoController.class).slash(airport.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAirport(@PathVariable long id, @RequestBody AirportDto airportDto){
        Airport airport = mapper.mapToEntity(airportDto);
        if(airportService.updateAirport(id,airport)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAirport(@PathVariable long id){
        if(airportService.deleteAirport(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Flight>> getFlightsFromAirport(@PathVariable long id){
        Airport airport = airportService.getAirport(id);
        if(airport.getId() != 0){
            List<Flight> flights = flightService.getFlightsByAirport(id);
            if(flights.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(flights);
        }
        return ResponseEntity.notFound().build();
    }
}
