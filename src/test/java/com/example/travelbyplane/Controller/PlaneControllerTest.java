package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Model.Plane;
import com.example.travelbyplane.Service.FlightService;
import com.example.travelbyplane.Service.PlaneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers =  PlaneController.class)
class PlaneControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlaneService planeService;
    @MockBean
    private FlightService flightService;

    private static Plane request;

    @BeforeAll
    public static void setup(){
        request = new Plane("Airbus A330-200", "KLM", 18, 36, 214, 3);
    }

    @Test
    void getAllPlanesEmpty() throws Exception {
        when(planeService.getPlanes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/plane"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllPlanes() throws Exception {
        List<Plane> planes = new ArrayList<>();
        planes.add(new Plane());
        when(planeService.getPlanes()).thenReturn(planes);

        mockMvc.perform(get("/travelByPlane/plane"))
                .andExpect(status().isOk());
    }

    @Test
    void addPlane() throws Exception {
        Plane plane = request;
        plane.setId(100);
        when(planeService.addPlane(any(Plane.class))).thenReturn(plane);

        mockMvc.perform(post("/travelByPlane/plane/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void updatePlaneNotExits() throws Exception {
        when(planeService.updatePlane(anyLong(), any(Plane.class))).thenReturn(false);

        mockMvc.perform(put("/travelByPlane/plane/{id}", 100)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePlane() throws Exception {
        when(planeService.updatePlane(anyLong(), any(Plane.class))).thenReturn(true);

        mockMvc.perform(put("/travelByPlane/plane/{id}", 100)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deletePlaneNotExists() throws Exception {
        when(flightService.findByPlane(anyLong())).thenReturn(new ArrayList<>());
        when(planeService.deletePlane(anyLong(),anyList())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/plane/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlane() throws Exception {
        when(flightService.findByPlane(anyLong())).thenReturn(new ArrayList<>());
        when(planeService.deletePlane(anyLong(),anyList())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/plane/{id}", anyLong()))
                .andExpect(status().isOk());
    }
}