package com.rba.bernard.zadatak;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class OsobaController {

    private OsobaService service;

    public OsobaController(OsobaService service) {
        this.service = service;
    }

    @PostMapping(path = "/osoba", produces = MediaType.APPLICATION_JSON_VALUE)
    Osoba addOsoba(@Valid @RequestBody OsobaDto dto) {
        return service.addOsoba(dto);
    }
    
    @GetMapping(path = "/osoba", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Osoba> findOsobe(@RequestParam(defaultValue = "") String oib) {
        return service.pretraziOib(oib);
    }

    @PostMapping(path = "/osoba/{oib}/kartica")
    @ResponseStatus(HttpStatus.CREATED)
    String izdajKarticu(@PathVariable String oib) throws IOException {
        var kartica = service.izdajKarticu(oib);
        return Files.readString(Path.of(kartica.getFileName()), StandardCharsets.UTF_8);
    }

    @GetMapping(path = "/osoba/{oib}/kartica")
    String dohvatiPostojecuKarticu(@PathVariable String oib) throws IOException {
        var kartica = service.dohvatiKarticu(oib);
        return Files.readString(Path.of(kartica.getFileName()), StandardCharsets.UTF_8);
    }

    @DeleteMapping(path = "/osoba/{oib}")
    Osoba obrišiOsobu(@PathVariable String oib) {
        return service.removeOsoba(oib);
    }
}
