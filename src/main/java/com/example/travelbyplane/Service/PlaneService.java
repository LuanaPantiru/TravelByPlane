package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.EntityExistsException;
import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Plane;
import com.example.travelbyplane.Repository.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaneService {

    @Autowired
    private PlaneRepository planeRepository;

    public Plane addPlane(Plane plane){
        Optional<Plane> optionalPlane = planeRepository.findPlaneByNameAndCompany(plane.getName(),plane.getCompany());
        if(optionalPlane.isPresent()){
            throw new EntityExistsException("A plane with same name and company already exists.");
        }
        return planeRepository.save(plane);
    }

    public List<Plane> getPlanes(){
        return planeRepository.findAll();
    }

    public boolean updatePlane(long id, Plane planeForUpdate){
        Optional<Plane> plane = planeRepository.findById(id);
        if(plane.isPresent()){
            planeForUpdate.setId(plane.get().getId());
            planeRepository.save(planeForUpdate);
            return true;
        }
        return false;
    }

    public boolean deletePlane(long id, List<Flight> planeFlights){
        Optional<Plane> plane = planeRepository.findById(id);
        if(plane.isPresent()){
            if(!planeFlights.isEmpty()){
                throw new ForeignKeyException("This plane has flights.");
            }
            planeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Plane findPlane(long id){
        Optional<Plane> plane = planeRepository.findById(id);
        if(plane.isEmpty()){
            return new Plane();
        }
        return plane.get();
    }


}
