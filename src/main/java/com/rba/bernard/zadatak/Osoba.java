package com.rba.bernard.zadatak;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="osoba")
@AllArgsConstructor
public @Data class Osoba {

    public Osoba() { }

    @Id
    @Column(name="oib")
    private String oib;

    @Column(name = "ime")
    private String ime;

    @Column(name = "prezime")
    private String prezime;

    @Column(name = "status")
    private Status status;

}
