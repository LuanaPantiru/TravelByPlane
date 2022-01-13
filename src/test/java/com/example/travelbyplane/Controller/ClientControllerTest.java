package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Model.Client;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Model.TravelClass;
import com.example.travelbyplane.Service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientService clientService;

    private static Client request;

    @BeforeAll
    public static void setup(){
        request = new Client("Luana", "Catalina", "123456789",null);
    }

    @Test
    void addNewClient() throws Exception {

        when(clientService.findByNameAndPassport(anyString(),anyString(),anyString())).thenReturn(new Client());

        mockMvc.perform(post("/travelByPlane/client/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void addClient() throws Exception {
        Client client = new Client("Luana", "Catalina", "123456789",false);
        client.setId(300);
        when(clientService.findByNameAndPassport(anyString(),anyString(),anyString())).thenReturn(client);

        mockMvc.perform(post("/travelByPlane/client/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClientsNotExist() throws Exception {
        when(clientService.getAllClients()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/client"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/travelByPlane/client"))
                .andExpect(status().isOk());
    }

    @Test
    void updateClientNotExist() throws Exception {
        when(clientService.patchClient(anyLong(), any(Client.class))).thenReturn(false);

        mockMvc.perform(patch("/travelByPlane/client/{id}", 10)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClient() throws Exception {
        when(clientService.patchClient(anyLong(), any(Client.class))).thenReturn(true);

        mockMvc.perform(patch("/travelByPlane/client/{id}", 10)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClientNotExists() throws Exception {
        when(clientService.deleteClient(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/client/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClient() throws Exception {
        when(clientService.deleteClient(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/client/{id}", anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void findFlightNoClient() throws Exception {
        when(clientService.findById(anyLong())).thenReturn(new Client());

        mockMvc.perform(get("/travelByPlane/client/{clientId}/searchFlight/{departureCity}/{arrivalCity}/{date}", anyLong(), "Bucharest", "Iasi", "16-02-2022"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findFlightNoTExist() throws Exception {
        Client client = new Client("Luana", "Pantiru","123456789", true);
        client.setId(12);
        when(clientService.findById(anyLong())).thenReturn(client);
        when(clientService.searchFlights(anyLong(),anyString(), anyString(), anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/client/{clientId}/searchFlight/{departureCity}/{arrivalCity}/{date}", anyLong(), "Bucharest", "Iasi", "16-02-2022"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findFlight() throws Exception {
        Client client = new Client("Luana", "Pantiru","123456789", true);
        client.setId(12);
        when(clientService.findById(anyLong())).thenReturn(client);
        List<List<FlightDto>> flights = new ArrayList<>();
        flights.add(new ArrayList<>());
        when(clientService.searchFlights(anyLong(),anyString(), anyString(), anyString())).thenReturn(flights);

        mockMvc.perform(get("/travelByPlane/client/{clientId}/searchFlight/{departureCity}/{arrivalCity}/{date}", anyLong(), "Bucharest", "Iasi", "16-02-2022"))
                .andExpect(status().isOk());
    }

    @Test
    void buyPlaneTicketNoClient() throws Exception {
        when(clientService.findById(anyLong())).thenReturn(new Client());

        List<PlaneTicketDto> planeTicketsRequest = new ArrayList<>();
        planeTicketsRequest.add(new PlaneTicketDto());
        mockMvc.perform(post("/travelByPlane/client/{clientId}/buyTickets", anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(planeTicketsRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void buyPlaneTicketCantBuy() throws Exception {
        Client client = new Client("Luana","Pantiru","123456789", true);
        client.setId(20);
        when(clientService.findById(anyLong())).thenReturn(client);
        List<PlaneTicketDto> planeTicketsRequest = new ArrayList<>();
        planeTicketsRequest.add(new PlaneTicketDto());
        when(clientService.addPlaneTicket(anyLong(),anyList())).thenReturn(false);

        mockMvc.perform(post("/travelByPlane/client/{clientId}/buyTickets", anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(planeTicketsRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buyPlaneTicket() throws Exception {
        Client client = new Client("Luana","Pantiru","123456789", true);
        client.setId(20);
        when(clientService.findById(anyLong())).thenReturn(client);
        List<PlaneTicketDto> planeTicketsRequest = new ArrayList<>();
        planeTicketsRequest.add(new PlaneTicketDto());
        when(clientService.addPlaneTicket(anyLong(),anyList())).thenReturn(true);

        mockMvc.perform(post("/travelByPlane/client/{clientId}/buyTickets", anyLong())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(planeTicketsRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTicketNotPossible() throws Exception {
        when(clientService.deletePlaneTicket(anyLong(),anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/client/{clientId}/deleteTicket/{planeTicketId}", anyLong(), anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTicket() throws Exception {
        when(clientService.deletePlaneTicket(anyLong(),anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/client/{clientId}/deleteTicket/{planeTicketId}", anyLong(), anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void showTicketsNoClient() throws Exception{
        when(clientService.findById(anyLong())).thenReturn(new Client());

        mockMvc.perform(get("/travelByPlane/client/{clientId}/tickets", anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void showTicketsNoTickets() throws Exception{
        Client client = new Client();
        client.setId(10);
        when(clientService.findById(anyLong())).thenReturn(client);
        when(clientService.findTickets(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/client/{clientId}/tickets", anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void showTickets() throws Exception{
        Client client = new Client();
        client.setId(10);
        when(clientService.findById(anyLong())).thenReturn(client);
        List<PlaneTicketDto> tickets = new ArrayList<>();
        tickets.add(new PlaneTicketDto());
        when(clientService.findTickets(anyLong())).thenReturn(tickets);

        mockMvc.perform(get("/travelByPlane/client/{clientId}/tickets", anyLong()))
                .andExpect(status().isOk());
    }
}