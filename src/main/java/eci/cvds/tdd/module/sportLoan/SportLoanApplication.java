package eci.cvds.tdd.module.sportLoan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportLoanApplication {
	public static void main(String[] args) {
		SpringApplication.run(SportLoanApplication.class, args);
	}

}
