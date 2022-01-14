package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Plane;
import com.example.travelbyplane.Repository.PlaneRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaneServiceTest {

    @Mock
    private PlaneRepository planeRepository;
    @Mock
    private FlightService flightService;
    @InjectMocks
    private PlaneService planeService;

    @Test
    @DisplayName("Don't add a plane with same configuration")
    void addPlaneSameConfiguration() {
        when(planeRepository.findPlaneByNameAndCompany(anyString(),anyString())).thenReturn(Optional.of(new Plane()));

        Plane plane = new Plane();
        plane.setName("avion");
        plane.setCompany("companie");
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> planeService.addPlane(plane));

        assertEquals("A plane with same name and company already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Plane added")
    void addPlaneSameSuccess() {
        when(planeRepository.findPlaneByNameAndCompany(anyString(),anyString())).thenReturn(Optional.empty());
        Plane savedPlane = new Plane();
        savedPlane.setId(100);
        when(planeRepository.save(any(Plane.class))).thenReturn(savedPlane);

        Plane plane = new Plane();
        plane.setName("avion");
        plane.setCompany("companie");
        Plane result = planeService.addPlane(plane);

        assertEquals(savedPlane.getId(), result.getId());
    }

    @Test
    void getPlanes() {
        when(planeRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(planeService.getPlanes().isEmpty());
    }

    @Test
    @DisplayName("Plane doesn't exists -> can't update")
    void updatePlaneNotExists() {
        when(planeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(planeService.updatePlane(anyLong(),new Plane()));
    }

    @Test
    @DisplayName("Plane updated")
    void updatePlaneSuccess() {
        Plane plane = new Plane();
        plane.setId(3);
        when(planeRepository.findById(anyLong())).thenReturn(Optional.of(plane));

        assertTrue(planeService.updatePlane(anyLong(),new Plane()));
    }

    @Test
    @DisplayName("Can't delete a plane which doesn't exists.")
    void deletePlaneNotExists() {
        when(planeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(planeService.deletePlane(anyLong(), new ArrayList<>()));
    }

    @Test
    @DisplayName("Can't delete a plane which has flights.")
    void deletePlaneHasFlights() {
        when(planeRepository.findById(anyLong())).thenReturn(Optional.of(new Plane()));
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());

        ForeignKeyException exception = assertThrows(ForeignKeyException.class,
                () -> planeService.deletePlane(anyLong(), flights));

        assertEquals("This plane has flights.", exception.getMessage());
    }

    @Test
    @DisplayName("Plane deleted")
    void deletePlaneSuccess() {
        when(planeRepository.findById(anyLong())).thenReturn(Optional.of(new Plane()));

        assertTrue(planeService.deletePlane(anyLong(), new ArrayList<>()));

    }

    @Test
    @DisplayName("Plane doesn't exist.")
    void findPlaneNotExist() {
        when(planeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertEquals(0, planeService.findPlane(anyLong()).getId());
    }

    @Test
    @DisplayName("Plane exist.")
    void findPlaneExist() {
        Plane plane = new Plane();
        plane.setId(16);
        when(planeRepository.findById(anyLong())).thenReturn(Optional.of(plane));
        assertEquals(plane.getId(), planeService.findPlane(anyLong()).getId());
    }
}