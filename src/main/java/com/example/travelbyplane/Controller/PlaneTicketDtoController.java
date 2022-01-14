package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Mapper.PlaneTicketMapper;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Service.PlaneTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/travelByPlane/planeTicket")
public class PlaneTicketDtoController {

    @Autowired
    private PlaneTicketService planeTicketService;
    @Autowired
    private PlaneTicketMapper mapper;

    @GetMapping
    public ResponseEntity<List<PlaneTicketDto>> getAllPlaneTickets(){
        List<PlaneTicket> planeTickets = planeTicketService.getAllPlaneTickets();
        if(planeTickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(planeTickets.stream().map(planeTicket -> mapper.mapToDto(planeTicket)).collect(Collectors.toList()));
    }

}
