package com.example.travelbyplane.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "plane")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "business_seats", nullable = false)
    private Integer businessSeats;

    @Column(name = "first_seats", nullable = false)
    private Integer firstSeats;

    @Column(name = "economy_seats", nullable = false)
    private Integer economySeats;

    @Column(name = "number_plane", nullable = false)
    private Integer numberPlanes;

    public Plane() {
    }

    public Plane(String name, String company, Integer businessSeats, Integer firstSeats, Integer economySeats, Integer numberPlanes) {
        this.name = name;
        this.company = company;
        this.businessSeats = businessSeats;
        this.firstSeats = firstSeats;
        this.economySeats = economySeats;
        this.numberPlanes = numberPlanes;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(Integer businessSeats) {
        this.businessSeats = businessSeats;
    }

    public Integer getFirstSeats() {
        return firstSeats;
    }

    public void setFirstSeats(Integer firstSeats) {
        this.firstSeats = firstSeats;
    }

    public Integer getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(Integer economySeats) {
        this.economySeats = economySeats;
    }

    public Integer getNumberPlanes() {
        return numberPlanes;
    }

    public void setNumberPlanes(Integer numberPlanes) {
        this.numberPlanes = numberPlanes;
    }
}
