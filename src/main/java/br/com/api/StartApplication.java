package br.com.api;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartApplication {
	
	private static Logger logger = Logger.getLogger(StartApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
		logger.info("Startando aplicação");
	}

}
