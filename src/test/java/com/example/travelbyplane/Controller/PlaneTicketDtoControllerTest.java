package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Service.PlaneTicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  PlaneTicketDtoController.class)
class PlaneTicketDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaneTicketService planeTicketService;
    @MockBean
    private PlaneTicketMapper mapper;

    @Test
    void getAllPlaneTicketsEmpty() throws Exception {
        when(planeTicketService.getAllPlaneTickets()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/planeTicket"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllPlaneTickets() throws Exception {
        List<PlaneTicket> planeTickets = new ArrayList<>();
        planeTickets.add(new PlaneTicket());
        when(planeTicketService.getAllPlaneTickets()).thenReturn(planeTickets);
        when(mapper.mapToDto(any(PlaneTicket.class))).thenReturn(new PlaneTicketDto());

        mockMvc.perform(get("/travelByPlane/planeTicket"))
                .andExpect(status().isOk());
    }
}