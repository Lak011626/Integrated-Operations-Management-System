package com.SCM.IOMS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IomsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IomsApplication.class, args);
	}
	@Bean
    public CommandLineRunner generateHash(PasswordEncoder encoder) {
        return args -> {
            System.out.println("===========================================");
            System.out.println("MÃ HASH MỚI CỦA 123456 LÀ: ");
            System.out.println(encoder.encode("123456"));
            System.out.println("===========================================");
        };
    }
}
