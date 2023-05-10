package com.example.rssfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class RssfeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssfeedApplication.class, args);
	}

}
