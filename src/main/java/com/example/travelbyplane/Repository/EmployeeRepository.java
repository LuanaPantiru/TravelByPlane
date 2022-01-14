package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Employee;
import com.example.travelbyplane.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "select count(*) from employee_distribution where employee_id = :employeeId", nativeQuery = true)
    int countFlights(long employeeId);

    Optional<Employee> findByPassportNo(String passport);

    @Query(value = "select * from employee join employee_distribution on id = employee_id where flight_id = :flightId", nativeQuery = true)
    List<Employee> findByFlight(long flightId);
}
