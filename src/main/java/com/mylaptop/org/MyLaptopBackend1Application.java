package com.mylaptop.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyLaptopBackend1Application {

	public static void main(String[] args) {
		SpringApplication.run(MyLaptopBackend1Application.class, args);
	}

}
