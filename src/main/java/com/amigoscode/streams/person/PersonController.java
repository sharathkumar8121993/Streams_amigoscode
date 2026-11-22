package com.amigoscode.streams.person;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// No @Validated needed: Spring MVC validates @Min/@Max parameters itself and returns 400.
@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Person> olderThan(@RequestParam(defaultValue = "0") @Min(0) int olderThan) {
        return service.olderThan(olderThan);
    }

    @GetMapping("/youngest")
    public List<Person> youngest(@RequestParam(defaultValue = "3") @Min(1) @Max(100) int n) {
        return service.youngest(n);
    }

    @GetMapping("/emails")
    public List<String> emails() {
        return service.emails();
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<Person> byEmail(@PathVariable String email) {
        return ResponseEntity.of(service.findByEmail(email));
    }

    @GetMapping("/oldest")
    public ResponseEntity<Person> oldest() {
        return ResponseEntity.of(service.oldest());
    }

    @GetMapping("/stats/age")
    public AgeStats ageStats() {
        return service.ageStats();
    }

    @GetMapping("/stats/gender")
    public Map<Gender, Long> countByGender() {
        return service.countByGender();
    }

    @GetMapping("/names-by-gender")
    public Map<Gender, List<String>> namesByGender() {
        return service.namesByGender();
    }
}
