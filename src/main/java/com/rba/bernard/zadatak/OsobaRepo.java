package com.rba.bernard.zadatak;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OsobaRepo extends JpaRepository<Osoba, String> {
    Optional<Osoba> findByOib(String oib);
    List<Osoba> findByOibContaining(String oib);
    boolean existsByOib(String oib);
    Osoba deleteDistinctByOib(String oib);
}
