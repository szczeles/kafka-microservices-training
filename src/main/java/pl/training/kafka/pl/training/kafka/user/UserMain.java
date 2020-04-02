package pl.training.kafka.pl.training.kafka.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserMain {
    public static void main(String[] args) {
        System.setProperty("server.port", "9000");
        SpringApplication.run(UserMain.class);
    }
}
