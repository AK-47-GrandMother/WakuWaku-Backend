package dev.backend.wakuwaku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WakuWakuApplication {

	public static void main(String[] args) {
		SpringApplication.run(WakuWakuApplication.class, args);
	}
}
