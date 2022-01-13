package com.example.travelbyplane.Service;

import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Model.TravelClass;
import com.example.travelbyplane.Repository.PlaneTicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaneTicketServiceTest {

    @Mock
    private PlaneTicketRepository planeTicketRepository;
    @InjectMocks
    private PlaneTicketService planeTicketService;

    @Test
    void addPlaneTicket() {
        PlaneTicket savedTicket = new PlaneTicket();
        savedTicket.setId(10);
        when(planeTicketRepository.save(any(PlaneTicket.class))).thenReturn(savedTicket);

        assertEquals(savedTicket.getId(), planeTicketService.addPlaneTicket(new PlaneTicket()).getId());
    }

    @Test
    void getAllPlaneTickets() {
        when(planeTicketRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(planeTicketService.getAllPlaneTickets().isEmpty());
    }

    @Test
    @DisplayName("Ticket doesn't exist -> nothing to delete")
    void deletePlaneTicketNotExist() {
        when(planeTicketRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(planeTicketService.deletePlaneTicket(anyLong()));
    }

    @Test
    @DisplayName("Ticket deleted")
    void deletePlaneTicketSuccess() {
        when(planeTicketRepository.findById(anyLong())).thenReturn(Optional.of(new PlaneTicket()));

        assertTrue(planeTicketService.deletePlaneTicket(anyLong()));
    }

    @Test
    void findTicketById() {
        when(planeTicketRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertEquals(0,planeTicketService.findTicketById(anyLong()).getId());
    }

    @Test
    void findTicketsByFlightAndTravelClass() {
        when(planeTicketRepository.findPlaneTicketByFlightAndTravelClass(any(Flight.class),any(TravelClass.class))).thenReturn(new ArrayList<>());

        assertTrue(planeTicketService.findTicketsByFlightAndTravelClass(new Flight(),TravelClass.BUSINESS).isEmpty());
    }

    @Test
    void getPlaneTicketsByClient() {
        when(planeTicketRepository.findPlaneTicketByClient(anyLong())).thenReturn(new ArrayList<>());

        assertTrue(planeTicketService.getPlaneTicketsByClient(anyLong()).isEmpty());
    }

    @Test
    void getPlaneTicketByFlight() {
        when(planeTicketRepository.getPlaneTicketsByFlight(anyLong())).thenReturn(new ArrayList<>());

        assertTrue(planeTicketService.getPlaneTicketByFlight(anyLong()).isEmpty());
    }

    @Test
    void getFlightTickets(){
        when(planeTicketRepository.getPlaneTicketByFlightSort(anyLong())).thenReturn(new ArrayList<>());

        assertEquals(0, planeTicketService.getFlightTickets(anyLong()).size());
    }
}