package com.example.travelbyplane.Mapper;

import com.example.travelbyplane.Dto.AirportDto;
import com.example.travelbyplane.Model.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "number", source = "address.number")
    AirportDto mapToDto(Airport airport);

    @Mapping(target = "address.country", source = "country")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.number", source = "number")
    Airport mapToEntity(AirportDto airportDto);
}
