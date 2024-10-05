package br.com.desafio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SanGiorgioApplicationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        SanGiorgioApplication.main(new String[]{});
        assertNotNull(context);
    }

}
