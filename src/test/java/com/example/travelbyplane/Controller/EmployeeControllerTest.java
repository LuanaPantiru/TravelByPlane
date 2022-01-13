package com.example.travelbyplane.Controller;

import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Position;
import com.example.travelbyplane.Service.EmployeeService;
import com.example.travelbyplane.Service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private FlightService flightService;

    private static Employee request;

    @BeforeAll
    public static void setup(){
        request = new Employee();
        request.setFirstName("Luana");
        request.setLastName("Pantiru");
        request.setPassportNo("123456789");
        request.setFlights(new HashSet<>());
    }

    @Test
    void addEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Luana");
        employee.setLastName("Pantiru");
        employee.setPassportNo("123456789");
        employee.setId(20);
        employee.setFlights(new HashSet<>());
        when(employeeService.addEmployee(new Employee())).thenReturn(employee);
        mockMvc.perform(post("/travelByPlane/employee/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllEmployeesEmpty() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/employee"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee());
        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        mockMvc.perform(get("/travelByPlane/employee"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEmployeeNotExist() throws Exception {
        when(employeeService.deleteEmployee(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/travelByPlane/employee/{id}", anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee() throws Exception {
        when(employeeService.deleteEmployee(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/travelByPlane/employee/{id}", anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmployeeNotExist() throws Exception {
        when(employeeService.updateEmployee(anyLong(),any(Employee.class))).thenReturn(false);

        mockMvc.perform(put("/travelByPlane/employee/{id}", 12)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee() throws Exception {
        when(employeeService.updateEmployee(anyLong(),any(Employee.class))).thenReturn(true);

        mockMvc.perform(put("/travelByPlane/employee/{id}", 12)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeNotExist() throws Exception {
        when(employeeService.getEmployee(anyLong())).thenReturn(new Employee());

        mockMvc.perform(get("/travelByPlane/employee/{id}", anyLong()))
                 .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeeNoFlights() throws Exception {
        Employee employee = new Employee();
        employee.setId(120);
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);
        when(flightService.findFlightByEmployee(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/travelByPlane/employee/{id}", anyLong()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(120);
        when(employeeService.getEmployee(anyLong())).thenReturn(employee);
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());

        when(flightService.findFlightByEmployee(anyLong())).thenReturn(flights);

        mockMvc.perform(get("/travelByPlane/employee/{id}", anyLong()))
                .andExpect(status().isOk());
    }
}