package com.example.travelbyplane.Service;

import com.example.travelbyplane.Exceptions.ForeignKeyException;
import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Position;
import com.example.travelbyplane.Repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private FlightService flightService;
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("Add employee")
    void addEmployee() {
        Employee employee = new Employee();
        employee.setId(5);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertEquals(employeeService.addEmployee(new Employee()).getId(), employee.getId());
    }

    @Test
    @DisplayName("Show all employees")
    void getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(employees,result);
    }

    @Test
    @DisplayName("Employee doesn't exist -> can't delete")
    void deleteEmployeeNotExist() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(employeeService.deleteEmployee(anyLong()));
    }

    @Test
    @DisplayName("Employee has flights -> can't delete")
    void deleteEmployeeHasFlights() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee()));
        when(employeeRepository.countFlights(anyLong())).thenReturn(3);

        ForeignKeyException exception = assertThrows(ForeignKeyException.class,
                () -> employeeService.deleteEmployee(anyLong()));

        assertEquals("This employee has flights!", exception.getMessage());
    }

    @Test
    @DisplayName("Employee deleted")
    void deleteEmployeeSuccess() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee()));
        when(employeeRepository.countFlights(anyLong())).thenReturn(0);

        assertTrue(employeeService.deleteEmployee(anyLong()));
    }


    @Test
    @DisplayName("Can't update an employee who doesn't exist.")
    void updateEmployeeNotExist() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(employeeService.updateEmployee(anyLong(), new Employee()));
    }

    @Test
    @DisplayName("Update success")
    void updateEmployeeSuccess() {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setPosition(Position.PILOT);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        assertTrue(employeeService.updateEmployee(anyLong(), new Employee()));
    }

    @Test
    @DisplayName("Get employee by id")
    void getEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertTrue(employeeService.getEmployee(anyLong()).getId() == 0);
    }

    @Test
    @DisplayName("Show employees which represent cabin crew for a specific flight")
    void findByFlight() {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findByFlight(anyLong())).thenReturn(employees);

        assertEquals(employeeService.findByFlight(anyLong()),employees);
    }

}