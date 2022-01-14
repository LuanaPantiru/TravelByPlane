package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Mapper.FlightMapper;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.Airport;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Service.AirportService;
import com.example.travelbyplane.Service.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  FlightDtoController.class)
class FlightDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;
    @MockBean
    private AirportService airportService;
    @MockBean
    private FlightMapper mapper;
    @MockBean
    private PlaneTicketMapper planeTicketMapper;

    private static FlightDto request;

    @BeforeAll
    public static void setup(){
        request = new FlightDto("16-02-2022","16-02-2022","16:00","16:45",45,"Bucharest Airport","Iasi Airport");
    }


    @Test
    void addFlightAirportsNotExist() throws Exception {
        when(airportService.findAirportByName(anyString())).thenReturn(new Airport());
        when(mapper.mapToEntity(any(FlightDto.class))).thenReturn(new Flight());
        when(flightService.addFlight(any(Flight.class),any(Airport.class), any(Airport.class))).thenReturn(new Flight());

        mockMvc.perform(post("/travelByPlane/flight/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addFlight() throws Exception {
        when(airportService.findAirportByName(anyString())).thenReturn(new Airport());
        when(mapper.mapToEntity(any(FlightDto.class))).thenReturn(new Flight());
        Flight flight = new Flight();
        flight.setId(20);
        when(flightService.addFlight(any(Flight.class),any(Airport.class), any(Airport.class))).thenReturn(flight);

        mockMvc.perform(post("/travelByPlane/flight/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllFlightsEmpty() throws Exception {
        when(flightService.getAllFlights()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/flight"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllFlights() throws Exception {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        when(flightService.getAllFlights()).thenReturn(flights);
        when(mapper.mapToDto(any(Flight.class))).thenReturn(new FlightDto());

        mockMvc.perform(get("/travelByPlane/flight"))
                .andExpect(status().isOk());
    }

    @Test
    void updateFlightNotExits() throws Exception  {
        when(mapper.mapToEntity(any(FlightDto.class))).thenReturn(new Flight());
        when(flightService.patchFlight(anyLong(),any(Flight.class))).thenReturn(false);

        mockMvc.perform(patch("/travelByPlane/flight/{id}", anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFlight() throws Exception  {
        when(mapper.mapToEntity(any(FlightDto.class))).thenReturn(new Flight());
        when(flightService.patchFlight(anyLong(),any(Flight.class))).thenReturn(true);

        mockMvc.perform(patch("/travelByPlane/flight/{id}", anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFlightNotExists() throws Exception {
        when(flightService.deleteFlight(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/flight/{id}",anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFlight() throws Exception {
        when(flightService.deleteFlight(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/flight/{id}",anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void addPlaneToFlightNotExists() throws Exception {
        when(flightService.addPlane(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/travelByPlane/flight/{flightId}/plane/{planeId}",anyLong(), anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addPlaneToFlight() throws Exception {
        when(flightService.addPlane(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/travelByPlane/flight/{flightId}/plane/{planeId}",anyLong(), anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void addCabinCrewToFlightNotExists() throws Exception {
        when(flightService.addCabinCrew(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/travelByPlane/flight/{flightId}/addCabinCrew/{cabinCrewId}",anyLong(), anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCabinCrewToFlight() throws Exception {
        when(flightService.addCabinCrew(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/travelByPlane/flight/{flightId}/addCabinCrew/{cabinCrewId}",anyLong(), anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCabinCrewToFlightNotExists() throws Exception {
        when(flightService.findFlight(anyLong())).thenReturn(new Flight());
        when(flightService.deleteEmployeeFromFlight(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/flight/{flightId}/eliminateCabinCrew/{cabinCrewId}",anyLong(), 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCabinCrewToFlight() throws Exception {
        when(flightService.findFlight(anyLong())).thenReturn(new Flight());
        when(flightService.deleteEmployeeFromFlight(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/flight/{flightId}/eliminateCabinCrew/{cabinCrewId}",anyLong(), 100))
                .andExpect(status().isOk());
    }

    @Test
    void getFlightTicketsNoFlight() throws Exception{
        when(flightService.findFlight(anyLong())).thenReturn(new Flight());

        mockMvc.perform(get("/travelByPlane/flight/{flightId}/tickets",anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFlightTicketsNoTickets() throws Exception{
        Flight flight = new Flight();
        flight.setId(10);
        when(flightService.findFlight(anyLong())).thenReturn(flight);
        when(flightService.getFlightTicket(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/flight/{flightId}/tickets",anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getFlightTickets() throws Exception{
        Flight flight = new Flight();
        flight.setId(10);
        when(flightService.findFlight(anyLong())).thenReturn(flight);
        List<PlaneTicket> tickets = new ArrayList<>();
        tickets.add(new PlaneTicket());
        when(flightService.getFlightTicket(anyLong())).thenReturn(tickets);
        when(planeTicketMapper.mapToDto(any(PlaneTicket.class))).thenReturn(new PlaneTicketDto());

        mockMvc.perform(get("/travelByPlane/flight/{flightId}/tickets",anyLong()))
                .andExpect(status().isOk());
    }
}