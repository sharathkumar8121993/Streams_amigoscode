package com.amigoscode.streams.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

/** Plain unit test: no Spring context, so it runs in milliseconds. */
class PersonServiceTest {

    private PersonService service;

    @BeforeEach
    void setUp() {
        service = new PersonService(new PersonRepository(JsonMapper.builder().build()));
    }

    @Test
    void olderThanKeepsOnlyMatchingPeople() {
        List<Person> result = service.olderThan(50);

        assertThat(result).isNotEmpty().allSatisfy(p -> assertThat(p.age()).isGreaterThan(50));
    }

    @Test
    void youngestIsSortedAscendingAndLimited() {
        List<Person> result = service.youngest(3);

        assertThat(result).hasSize(3).extracting(Person::age).isSorted();
        assertThat(result.getFirst().age()).isEqualTo(service.ageStats().min());
    }

    @Test
    void findByEmailIsCaseInsensitiveAndEmptyWhenMissing() {
        assertThat(service.findByEmail("ALEX.GARCIA@example.com")).map(Person::firstName).contains("Alex");
        assertThat(service.findByEmail("nobody@example.com")).isEmpty();
    }

    @Test
    void statsAndReduceAgree() {
        AgeStats stats = service.ageStats();

        assertThat(stats.count()).isEqualTo(20);
        assertThat(stats.sum()).isEqualTo(service.totalAge());
        assertThat(service.oldest()).map(Person::age).contains(stats.max());
    }

    @Test
    void groupCountsAddUpToTotal() {
        Map<Gender, Long> counts = service.countByGender();

        assertThat(counts.values().stream().mapToLong(Long::longValue).sum()).isEqualTo(20);
    }

    @Test
    void partitionAlwaysHasBothKeys() {
        Map<Boolean, List<Person>> partition = service.partitionByAdult();

        assertThat(partition).containsKeys(true, false);
        assertThat(partition.get(false)).allSatisfy(p -> assertThat(p.age()).isLessThan(18));
    }
}
