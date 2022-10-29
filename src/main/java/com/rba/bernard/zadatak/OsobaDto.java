package com.rba.bernard.zadatak;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class OsobaDto {
    @Pattern(regexp = "\\d{11}")
    private String oib;
    @Size(min = 1)
    private String ime;
    @Size(min = 1)
    private String prezime;
}
