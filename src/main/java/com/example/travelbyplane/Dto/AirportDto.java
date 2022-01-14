package com.example.travelbyplane.Dto;

public class AirportDto {

    private long id;
    private String name;
    private String country;
    private String city;
    private String street;
    private String number;

    public AirportDto() {
    }

    public AirportDto(String name, String country, String city, String street, String number) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
