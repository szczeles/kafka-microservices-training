package pl.training.kafka.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShippingMain {
    public static void main(String[] args) {
        SpringApplication.run(ShippingMain.class, "--spring.main.web-application-type=none");
    }
}
