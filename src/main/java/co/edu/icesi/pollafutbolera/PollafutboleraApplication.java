 package co.edu.icesi.pollafutbolera;

import co.edu.icesi.pollafutbolera.repository.TenantAwareRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(repositoryBaseClass = TenantAwareRepository.class)
public class  PollafutboleraApplication {
	public static void main(String[] args) {
		SpringApplication.run(PollafutboleraApplication.class, args);
	}

}
