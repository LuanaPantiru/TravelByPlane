package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Position;
import com.example.travelbyplane.Service.EmployeeService;
import com.example.travelbyplane.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("travelByPlane/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FlightService flightService;


    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addEmployee(@RequestBody Employee employee){
        employee.setSalary(employee.getPosition());
        employeeService.addEmployee(employee);
        URI uri = WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(employee.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(){
        List<Employee> employees = employeeService.getAllEmployees();
        if(employees.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable long id){
        if(employeeService.deleteEmployee(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEmployee(@PathVariable long id, @RequestBody Employee employee){
        if(employeeService.updateEmployee(id,employee)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Flight>> getEmployee(@PathVariable long id){
        Employee employee = employeeService.getEmployee(id);
        if(employee.getId() != 0){
            List<Flight> flights = flightService.findFlightByEmployee(id);
            if(flights.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(flights);
        }
        return ResponseEntity.notFound().build();
    }
}
