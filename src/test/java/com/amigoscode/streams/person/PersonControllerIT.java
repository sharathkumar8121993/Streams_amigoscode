package com.amigoscode.streams.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/** Boots the full context and exercises the HTTP layer end to end. */
@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIT {

    @Autowired
    MockMvcTester mvc;

    @Test
    void returnsYoungest() {
        assertThat(mvc.get().uri("/api/people/youngest?n=2"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.length()").isEqualTo(2);
    }

    @Test
    void returns404ForUnknownEmail() {
        assertThat(mvc.get().uri("/api/people/by-email/nobody@example.com"))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void rejectsInvalidLimit() {
        assertThat(mvc.get().uri("/api/people/youngest?n=0"))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void healthIsUp() {
        assertThat(mvc.get().uri("/actuator/health"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.status").isEqualTo("UP");
    }
}
