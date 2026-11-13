package com.stj.khaiman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KhaimanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KhaimanApplication.class, args);
    }

}
