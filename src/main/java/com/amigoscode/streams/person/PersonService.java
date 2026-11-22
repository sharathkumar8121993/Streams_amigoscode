package com.amigoscode.streams.person;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

/**
 * Each method demonstrates one core Stream idea. Read them top to bottom:
 * they go from simple stateless operations to collectors that build maps.
 */
@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    /** filter: an intermediate, stateless, lazy operation. */
    public List<Person> olderThan(int age) {
        return repository.findAll().stream()
                .filter(p -> p.age() > age)
                .toList();
    }

    /** sorted + limit: a stateful operation followed by short-circuiting. */
    public List<Person> youngest(int n) {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(Person::age).thenComparing(Person::lastName))
                .limit(n)
                .toList();
    }

    /** map: transform each element into something else. */
    public List<String> emails() {
        return repository.findAll().stream()
                .map(Person::email)
                .sorted()
                .toList();
    }

    /** findFirst: terminal and short-circuiting, returns Optional because it may find nothing. */
    public Optional<Person> findByEmail(String email) {
        return repository.findAll().stream()
                .filter(p -> p.email().equalsIgnoreCase(email))
                .findFirst();
    }

    /** max with a Comparator: a reduction that returns Optional. */
    public Optional<Person> oldest() {
        return repository.findAll().stream()
                .max(Comparator.comparingInt(Person::age));
    }

    /** anyMatch / allMatch: short-circuiting boolean terminals. */
    public boolean anyoneOlderThan(int age) {
        return repository.findAll().stream().anyMatch(p -> p.age() > age);
    }

    /** mapToInt + summaryStatistics: primitive streams avoid boxing and give count/min/max/avg in one pass. */
    public AgeStats ageStats() {
        return AgeStats.from(repository.findAll().stream()
                .mapToInt(Person::age)
                .summaryStatistics());
    }

    /** groupingBy with a downstream collector. TreeMap gives stable key ordering in responses. */
    public Map<Gender, Long> countByGender() {
        return repository.findAll().stream()
                .collect(groupingBy(Person::gender, TreeMap::new, counting()));
    }

    /** groupingBy + mapping: group, then transform each group's elements. */
    public Map<Gender, List<String>> namesByGender() {
        return repository.findAll().stream()
                .collect(groupingBy(Person::gender, TreeMap::new, mapping(Person::fullName, toList())));
    }

    /** partitioningBy: a groupingBy specialised to a predicate, always has both true and false keys. */
    public Map<Boolean, List<Person>> partitionByAdult() {
        return repository.findAll().stream()
                .collect(partitioningBy(p -> p.age() >= 18));
    }

    /** reduce: the general fold that sum(), count() and max() are built on. */
    public int totalAge() {
        return repository.findAll().stream()
                .map(Person::age)
                .reduce(0, Integer::sum);
    }
}
