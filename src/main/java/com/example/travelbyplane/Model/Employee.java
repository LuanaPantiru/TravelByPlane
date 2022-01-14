package com.example.travelbyplane.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee extends Person {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "salary")
    private Double salary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_distribution", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "flight_id"))
    private Set<Flight> flights;

    public Employee(){

    }

    public Employee(String firstName, String lastName, String passportNo, Position position) {
        super(firstName, lastName, passportNo);
        this.position = position;
        this.salary = calculateSalary(position);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Position position) {
        this.salary = calculateSalary(position);
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }

    private Double calculateSalary(Position position){
        switch (position){
            case SENIOR_FLIGHT_ATTENDANT:
                return 4000.00;
            case PILOT:
                return 25000.00;
            case COPILOT:
                return 8000.00;
            default:
                return 3000.00;
        }
    }

    public void addFlight(Flight flight){
        flights.add(flight);
    }
    public void removeFlight(Flight flight) {flights.remove(flight);}
}
