package com.bbva.wallet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@OpenAPIDefinition(
		info=@Info(
				title = "Alkywall",
				version = "1.0.0",
				description = "Alkywall posee las funcionalidades necesarias para ser la mejor billetera virtual",
					contact=@Contact(
						name = "Tech Titans",
						email = "techtitans@bbva.com"
					),
					license = @License(
						name = "Alkywall license",
						url = "alkywall.bbva.com"
					)
		)
)

public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

}
