package com.example.travelbyplane.Model;

import javax.persistence.*;

@Entity
@Table(name = "planeTicket")
public class PlaneTicket {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private TravelClass travelClass;

    @Column(name = "price", nullable = false)
    private Double price;

    public PlaneTicket() {
    }

    public PlaneTicket(Client client, Flight flight, TravelClass travelClass, Double price) {
        this.client = client;
        this.flight = flight;
        this.travelClass = travelClass;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
