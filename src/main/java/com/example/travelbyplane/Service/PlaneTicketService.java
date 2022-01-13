package com.example.travelbyplane.Service;

import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Model.TravelClass;
import com.example.travelbyplane.Repository.PlaneTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaneTicketService {

    @Autowired
    private PlaneTicketRepository planeTicketRepository;

    public PlaneTicket addPlaneTicket(PlaneTicket planeTicket){
        return  planeTicketRepository.save(planeTicket);
    }

    public List<PlaneTicket> getAllPlaneTickets(){
        return planeTicketRepository.findAll();
    }

    public boolean deletePlaneTicket(long id){
        Optional<PlaneTicket> planeTicket = planeTicketRepository.findById(id);
        if(planeTicket.isPresent()){
            planeTicketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public PlaneTicket findTicketById(long id){
        Optional<PlaneTicket> planeTicket = planeTicketRepository.findById(id);
        return planeTicket.orElseGet(PlaneTicket::new);
    }

    public List<PlaneTicket> findTicketsByFlightAndTravelClass(Flight flight, TravelClass travelClass){
        return planeTicketRepository.findPlaneTicketByFlightAndTravelClass(flight,travelClass);
    }

    public List<PlaneTicket> getPlaneTicketsByClient(long clientId){
        return planeTicketRepository.findPlaneTicketByClient(clientId);
    }

    public List<PlaneTicket> getPlaneTicketByFlight(long flightId){
        return planeTicketRepository.getPlaneTicketsByFlight(flightId);
    }

    public List<PlaneTicket> getFlightTickets(long flightId){
        return planeTicketRepository.getPlaneTicketByFlightSort(flightId);
    }

}
