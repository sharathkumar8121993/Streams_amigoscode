package com.amigoscode.streams.person;

/**
 * Immutable value object. Records give us equals/hashCode/toString for free,
 * which matters for streams: distinct(), groupingBy() and toSet() all rely on them.
 */
public record Person(
        int id,
        String firstName,
        String lastName,
        String email,
        Gender gender,
        int age) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}
