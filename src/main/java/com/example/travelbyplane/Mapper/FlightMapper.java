package com.example.travelbyplane.Mapper;

import com.example.travelbyplane.Dto.FlightDto;
import com.example.travelbyplane.Model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(target = "departureAirport", source = "departureAirport.name")
    @Mapping(target = "arrivalAirport", source = "arrivalAirport.name")
    FlightDto mapToDto(Flight flight);


    @Mapping(target = "departureAirport.name", source = "departureAirport")
    @Mapping(target = "arrivalAirport.name", source = "arrivalAirport")
    Flight mapToEntity(FlightDto flightDto);
}
