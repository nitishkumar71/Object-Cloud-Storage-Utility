package org.cloud.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = { "org.cloud.*" })
public class SpringBootTestMain {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestMain.class, args);
	}
}
