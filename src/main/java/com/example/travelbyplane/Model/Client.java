package com.example.travelbyplane.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "account", nullable = false)
    private Boolean account;

    @OneToMany(mappedBy = "client")
    private List<PlaneTicket> planeTickets;

    public Client(){

    }

    public Client(String firstName, String lastName, String passportNo, Boolean account) {
        super(firstName,lastName,passportNo);
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getAccount() {
        return account;
    }

    public void setAccount(Boolean account) {
        this.account = account;
    }

    public void addTicket(PlaneTicket planeTicket){
        planeTickets.add(planeTicket);
    }

    public void setPlaneTickets(List<PlaneTicket> planeTickets) {
        this.planeTickets = planeTickets;
    }

    public void removeTicket(PlaneTicket planeTicket){
        planeTickets.remove(planeTicket);
    }
}
