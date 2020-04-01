package pl.training.kafka.order;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

@Service
public class OrderCreatedNotificationSender {

    private final KafkaProducer<String, String> producer;

    public OrderCreatedNotificationSender() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // kafka?
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(properties);
    }

    public void send(String orderId) {
        ProducerRecord producerRecord = new ProducerRecord("order-created", orderId, String.format("New order created: %s", orderId));
        producer.send(producerRecord);
    }
}
