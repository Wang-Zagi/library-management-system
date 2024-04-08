package cn.edu.xidian.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);

		System.out.println("\n");
		System.out.println("SpringBoot is starting......");

	}
}
