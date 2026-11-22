# Streams_amigoscode

A Spring Boot 4.1 + Gradle (Kotlin DSL) app for practising the Java Streams API.

## Run

```bash
./gradlew bootRun          # starts on http://localhost:8080
./gradlew test             # unit + integration tests
./gradlew build            # fat jar in build/libs/
```

Requires JDK 21. The Gradle wrapper downloads Gradle itself.

## Endpoints

| Endpoint | Stream concept |
|---|---|
| `GET /api/people?olderThan=30` | `filter` |
| `GET /api/people/youngest?n=3` | `sorted` + `limit` |
| `GET /api/people/emails` | `map` |
| `GET /api/people/by-email/{email}` | `findFirst` → `Optional` |
| `GET /api/people/oldest` | `max(Comparator)` |
| `GET /api/people/stats/age` | `mapToInt` + `summaryStatistics` |
| `GET /api/people/stats/gender` | `groupingBy` + `counting` |
| `GET /api/people/names-by-gender` | `groupingBy` + `mapping` |
| `GET /actuator/health` | health check |

## Layout

```
src/main/java/com/amigoscode/streams/
├── StreamsApplication.java
└── person/
    ├── Person.java            record (immutable domain)
    ├── PersonRepository.java  loads data/people.json once
    ├── PersonService.java     one method per Stream concept
    └── PersonController.java  REST layer
```
