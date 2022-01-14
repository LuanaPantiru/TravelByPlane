package com.example.travelbyplane.Repository;

import com.example.travelbyplane.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientById(Long id);

    @Override
    void deleteById(Long id);

    Optional<Client> findClientByPassportNo(String passport);

    Optional<Client> findClientByFirstNameAndLastNameAndPassportNo(String firstName, String lastName, String passport);

}
