package com.akila;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Log4j2
@EntityScan("com.akila.entity")
@EnableJpaRepositories(basePackages = "com.akila.repository")
public class MySQLTestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MySQLTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("haha");
    }
}