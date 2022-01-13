package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.AirportDto;
import com.example.travelbyplane.Mapper.AirportMapper;
import com.example.travelbyplane.Model.Address;
import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Service.AirportService;
import com.example.travelbyplane.Service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  AirportDtoController.class)
class AirportDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AirportService airportService;
    @MockBean
    private FlightService flightService;
    @MockBean
    private AirportMapper mapper;

    @Test
    void getAirportsNotExists() throws Exception {
        when(airportService.getAirports()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/airport"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAirports() throws Exception {
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport());
        when(airportService.getAirports()).thenReturn(airports);

        mockMvc.perform(get("/travelByPlane/airport"))
                .andExpect(status().isOk());
    }

    @Test
    void addAirport() throws Exception {
        AirportDto request = new AirportDto("Aeroport", "Romania", "Bucharest", "Strada X","21");
        Airport airport = new Airport("Aeroport", new Address("Romania", "Bucharest", "Strada X","21"));
        when(mapper.mapToEntity(any(AirportDto.class))).thenReturn(airport);
        airport.setId(1);
        airport.getAddress().setId(1);
        when(airportService.addAirport(any(Airport.class))).thenReturn(airport);

        mockMvc.perform(post("/travelByPlane/airport/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    void updateAirportNotExist() throws Exception {
        AirportDto request = new AirportDto("Aeroport", "Romania", "Bucharest", "Strada X","21");
        when(airportService.updateAirport(anyLong(), any(Airport.class))).thenReturn(false);

        mockMvc.perform(put("/travelByPlane/airport/{id}",anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAirport() throws Exception {
        AirportDto request = new AirportDto("Aeroport", "Romania", "Bucharest", "Strada X","21");
        Airport airport = new Airport("Aeroport", new Address("Romania", "Bucharest", "Strada X","21"));
        when(mapper.mapToEntity(any(AirportDto.class))).thenReturn(airport);
        when(airportService.updateAirport(anyLong(), any(Airport.class))).thenReturn(true);

        mockMvc.perform(put("/travelByPlane/airport/{id}",anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAirportNotExist() throws Exception {
        when(airportService.deleteAirport(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/airport/{id}",anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAirport() throws Exception {
        when(airportService.deleteAirport(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/airport/{id}",anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void getFlightsFromAirportNotExist() throws Exception {
        when(airportService.getAirport(anyLong())).thenReturn(new Airport());

        mockMvc.perform(get("/travelByPlane/airport/{id}",anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFlightsFromAirportNoFlights() throws Exception {
        Airport airport = new Airport();
        airport.setId(200);
        when(airportService.getAirport(anyLong())).thenReturn(airport);
        when(flightService.getFlightsByAirport(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/airport/{id}",anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getFlightsFromAirport() throws Exception {
        Airport airport = new Airport();
        airport.setId(200);
        when(airportService.getAirport(anyLong())).thenReturn(airport);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        when(flightService.getFlightsByAirport(anyLong())).thenReturn(flights);

        mockMvc.perform(get("/travelByPlane/airport/{id}",anyLong()))
                .andExpect(status().isOk());
    }
}