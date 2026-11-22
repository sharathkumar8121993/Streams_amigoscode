package com.amigoscode.streams.person;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Loads mock data once at startup. The list is unmodifiable, so every
 * operation on it has to go through a stream and produce a new result
 * instead of mutating shared state.
 */
@Repository
public class PersonRepository {

    private final List<Person> people;

    public PersonRepository(JsonMapper jsonMapper) {
        try (InputStream in = new ClassPathResource("data/people.json").getInputStream()) {
            this.people = List.copyOf(jsonMapper.readValue(in, new TypeReference<List<Person>>() {}));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load data/people.json", e);
        }
    }

    public List<Person> findAll() {
        return people;
    }
}
