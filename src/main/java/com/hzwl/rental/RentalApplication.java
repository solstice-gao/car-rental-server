package com.hzwl.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author GA666666
 * @Date 2023/8/09 21:42
 */
@EnableAsync
@EnableWebMvc
@SpringBootApplication
@EnableTransactionManagement
public class RentalApplication {
	public static void main(String[] args) {
		SpringApplication.run(RentalApplication.class, args);
	}
}
