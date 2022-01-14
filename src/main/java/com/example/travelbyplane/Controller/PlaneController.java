package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Model.Plane;
import com.example.travelbyplane.Service.FlightService;
import com.example.travelbyplane.Service.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("travelByPlane/plane")
public class PlaneController {

    @Autowired
    private PlaneService planeService;
    @Autowired
    private FlightService flightService;


    @GetMapping
    public ResponseEntity<List<Plane>> getAllPlanes(){
        List<Plane> planes = planeService.getPlanes();
        if(planes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(planes);
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addPlane(@RequestBody Plane plane){
        planeService.addPlane(plane);
        URI uri = WebMvcLinkBuilder.linkTo(PlaneController.class).slash("travelByPlane").slash("plane").slash(plane.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePlane(@PathVariable long id, @RequestBody Plane plane){
        if(planeService.updatePlane(id, plane)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePlane(@PathVariable long id){
        if(planeService.deletePlane(id, flightService.findByPlane(id))){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
