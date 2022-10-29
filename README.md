# Rješenje zadatka

### Opis
Implementiran je Controller REST interface sa običnim spring MVC pluginom.
Korištena je H2 in-memory baza (vidi application.properties).
Aplikacija sprema izdane kartice za korisnika u direktoriju `kartice/`.
Koristio sam Hibernate umjesto običnih SQL upita.
Koristio sam lombok za kraći kod.

Nisam odvajao kartice/osobe u zasebne kontrolere/servise iako bi semantički imalo smisla jer je zadatak prekratak.

### Testovi
Use caseovi su demonstrirani kroz e2e testove sa Mock MVC spring boot alatom.

### Referentna Spring Dokumentacija
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.0-SNAPSHOT/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.0-SNAPSHOT/maven-plugin/reference/html/#build-image)
