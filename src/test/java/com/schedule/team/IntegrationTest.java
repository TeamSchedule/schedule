package com.schedule.team;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {
    @Autowired
    private Flyway flyway;

    public void clearDb() {
        flyway.clean();
        flyway.migrate();
    }
}