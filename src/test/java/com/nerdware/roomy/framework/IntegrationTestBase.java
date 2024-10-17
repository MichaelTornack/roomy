package com.nerdware.roomy.framework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class IntegrationTestBase
{
    @Autowired
    protected MockMvc client;

    public IntegrationTestBase() {
        // Is called for every test
    }

    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:latest")
        .withDatabaseName("db")
        .withUsername("root")
        .withPassword("admin")
        .withReuse(true);

    @BeforeAll
    static void beforeAll()
    {
        // Is executed before the constructor of IntegrationTestBase
        // Is called for every test class (fixture)

        if(!mySql.isRunning())
            mySql.start();
    }

    @BeforeEach
    void beforeEach()
    {
        // is called before every test
        // is called after constructor of IntegrationTestBase
        int i = 0;
    }

    @AfterEach
    void afterEach()
    {

    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry)
    {
        registry.add("spring.datasource.url", mySql::getJdbcUrl);
        registry.add("spring.datasource.username", mySql::getUsername);
        registry.add("spring.datasource.password", mySql::getPassword);
    }
}
