package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.PlaneTicket;
import com.example.travelbyplane.Repository.EmployeeRepository;
import com.example.travelbyplane.Repository.FlightRepository;
import com.example.travelbyplane.Repository.PlaneTicketRepository;
import org.hibernate.action.internal.OrphanRemovalAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public boolean deleteEmployee(long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            return false;
        }else{
            int count = employeeRepository.countFlights(id);
            if(count == 0){
                employeeRepository.delete(employee.get());
                return true;
            }
            throw new ForeignKeyException("This employee has flights!");
        }
    }

    public boolean updateEmployee(long id, Employee employeeForUpdate){
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            employeeForUpdate.setId(employee.get().getId());
            employeeForUpdate.setSalary(employee.get().getPosition());
            employeeRepository.save(employeeForUpdate);
            return true;
        }
        return false;
    }

    public Employee getEmployee(long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElseGet(Employee::new);
    }

    public List<Employee> findByFlight(long flightId){
        return employeeRepository.findByFlight(flightId);
    }
//
//    public List<Flight> getFlightsForEmployee(long id){
//        return flightService.findFlightByEmployee(id);
//    }
}
