package pl.training.kafka.order;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import pl.training.kafka.avromodel.Order;
import pl.training.kafka.model.OrderChange;

import java.util.Properties;

@Service
public class OrderEventSourcingSender {

    private final KafkaProducer<String, Order> producer;

    public OrderEventSourcingSender() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // kafka?
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1000");

        producer = new KafkaProducer<>(properties);
    }

    public void send(Order order) {
        ProducerRecord producerRecord = new ProducerRecord("order-change", order.getId(), order);
        producer.send(producerRecord);
    }
}
