package pl.training.kafka.order;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import pl.training.kafka.model.OrderChange;

import java.util.Properties;

@Service
public class OrderChangeSender {

    private final KafkaProducer<String, OrderChange> producer;

    public OrderChangeSender() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // kafka?
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderChangeJSONSerializer.class.getName());
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1000");

        producer = new KafkaProducer<>(properties);
    }

    public void send(String orderId, OrderChange orderChange) {
        ProducerRecord producerRecord = new ProducerRecord("order-change", orderId, orderChange);
        producer.send(producerRecord);
    }
}
