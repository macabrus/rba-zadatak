package com.rba.bernard.zadatak;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OsobaService {

    @Value("${folder}")
    private String folder;
    private OsobaRepo repo;

    public OsobaService(OsobaRepo repo) {
        this.repo = repo;
    }

    public Osoba addOsoba(OsobaDto dto) {
        final var osoba = new Osoba(
            dto.getOib(),
            dto.getIme(),
            dto.getPrezime(),
            Status.NIJE_IZDANO
        );
        repo.saveAndFlush(osoba);
        return osoba;
    }

    public List<Osoba> pretraziOib(String oibPart) {
        final var result = repo.findByOibContaining(oibPart);
        if (result.isEmpty()) {
            throw new OsobaNotFoundException();
        }
        return result;
    }

    @Transactional
    public OpisnikKartice izdajKarticu(String oib) throws IOException {
        if (!repo.existsByOib(oib)) {
            throw new OsobaNotFoundException();
        }
        if (!Files.exists(Path.of(folder))) {
            Files.createDirectories(Path.of(folder));
        }
        final var fileName = "%s_%s.txt".formatted(oib, Instant.now().toEpochMilli());
        final var filePath = fileNameByOib(oib).orElse(Path.of(folder, fileName));
        final var file = filePath.toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        /* Mijenja se status u izdano */
        final var o = repo.findByOib(oib).orElseThrow();
        o.setStatus(Status.IZDANO);
        repo.save(o);

        /* Ispisujemo karticu */
        final var content = toKarticaContent(o);
        Files.writeString(filePath, content);

        /* Vraćam opisnik kartice na disku */
        return new OpisnikKartice(o, filePath.toString(), Files.size(filePath));
    }

    public OpisnikKartice dohvatiKarticu(String oib) throws IOException, KarticaNotFoundException {
        final var osoba = repo.findByOib(oib).orElseThrow(OsobaNotFoundException::new);
        final var filePath = fileNameByOib(oib).orElseThrow(KarticaNotFoundException::new);
        return new OpisnikKartice(osoba, filePath.toString(), Files.size(filePath));
    }

    private Optional<Path> fileNameByOib(String oib) throws IOException {
        return Files.list(Path.of(folder))
            .filter(p -> p.getFileName().toString().startsWith(oib + "_"))
            .findFirst();
    }

    private String toKarticaContent(Osoba o) {
        return """
            Ime: %s
            Prezime: %s
            OIB: %s
            Status: %s
            """.formatted(o.getIme(), o.getPrezime(), o.getOib(), o.getStatus());
    }

    @Transactional
    public Osoba removeOsoba(String oib) {
        var osoba = repo.findByOib(oib).orElseThrow(OsobaNotFoundException::new);
        repo.delete(osoba);
        return osoba;
    }
}
