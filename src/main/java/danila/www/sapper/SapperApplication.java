package danila.www.sapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(SapperApplication.class, args);
    }

}
