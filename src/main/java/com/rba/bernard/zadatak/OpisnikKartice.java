package com.rba.bernard.zadatak;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class OpisnikKartice {
    Osoba o;
    String fileName;
    long size;
}
