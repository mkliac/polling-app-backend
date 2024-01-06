package voting.app.voting.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class VotingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingAppApplication.class, args);
	}

}
