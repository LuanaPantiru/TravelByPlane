package com.example.travelbyplane.Mapper;

import com.example.travelbyplane.Dto.PlaneTicketDto;
import com.example.travelbyplane.Model.PlaneTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PlaneTicketMapper {

    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    @Mapping(target = "passportNo", source = "client.passportNo")
    @Mapping(target = "departureDate", source = "flight.departureDate")
    @Mapping(target = "arrivalDate", source = "flight.arrivalDate")
    @Mapping(target = "departureTime", source = "flight.departureTime")
    @Mapping(target = "arrivalTime", source = "flight.arrivalTime")
    @Mapping(target = "departureAirport", source = "flight.departureAirport.name")
    @Mapping(target = "arrivalAirport", source = "flight.arrivalAirport.name")
    PlaneTicketDto mapToDto(PlaneTicket planeTicket);

    @Mapping(target = "client.firstName", source = "firstName")
    @Mapping(target = "client.lastName", source = "lastName")
    @Mapping(target = "client.passportNo", source = "passportNo")
    @Mapping(target = "flight.departureDate", source = "departureDate")
    @Mapping(target = "flight.arrivalDate", source = "arrivalDate")
    @Mapping(target = "flight.departureTime", source = "departureTime")
    @Mapping(target = "flight.arrivalTime", source = "arrivalTime")
    @Mapping(target = "flight.departureAirport.name", source = "departureAirport")
    @Mapping(target = "flight.arrivalAirport.name", source = "arrivalAirport")
    PlaneTicket mapToEntity(PlaneTicketDto planeTicketDto);
}
