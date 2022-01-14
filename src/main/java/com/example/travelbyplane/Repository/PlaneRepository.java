package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Flight;
import com.example.travelbyplane.Model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {
    @Override
    Optional<Plane> findById(Long id);

    @Override
    void deleteById(Long id);

    Optional<Plane> findPlaneByNameAndCompany(String name, String company);
}
