package pl.training.kafka.pl.training.kafka.user;


import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.training.kafka.avromodel.User;
import pl.training.kafka.avromodel.UserAddress;
import pl.training.kafka.order.OrderCreateRequest;

import java.util.Properties;

@RestController
@RequestMapping("/user")
public class UserController {
    private final KafkaProducer<Object, Object> producer;

    public UserController() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // kafka?
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1000");

        producer = new KafkaProducer<>(properties);
    }


    @PostMapping
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserAddress userAddress = new UserAddress();
        userAddress.setCity(userRegistrationRequest.getCity());
        userAddress.setStreet(userRegistrationRequest.getStreet());

        User user = new User();
        user.setId(userRegistrationRequest.getId());
        user.setFirstName(userRegistrationRequest.getFirstName());
        user.setLastName(userRegistrationRequest.getLastName());
        user.setAddress(userAddress);

        producer.send(new ProducerRecord<>("user", user.getId(), user));
    }
}
