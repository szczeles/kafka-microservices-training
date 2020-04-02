package pl.training.kafka.shipping;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.training.kafka.model.OrderChange;
import pl.training.kafka.model.OrderStatus;

import java.time.Duration;
import java.util.*;

@Service
public class OrderChangeConsumer implements ApplicationRunner {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    private final KafkaConsumer<String, OrderChange> consumer;

    public OrderChangeConsumer() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092"); // or localhost:9092
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderChangeJSONDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "shipping-service");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("order-change"));
//        Collections.singletonList("order-created");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Consumer started");
        while (true) {
            ConsumerRecords<String, OrderChange> records = consumer.poll(Duration.ofSeconds(1));
//            Thread.sleep(10000);
            for (ConsumerRecord<String, OrderChange> record : records) {


                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                HashMap<TopicPartition, OffsetAndMetadata> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put(topicPartition, offsetAndMetadata);

                // NextOffset.for(record).build() -> possible improvement

                // first, move the offset of the current message
                consumer.commitSync(objectObjectHashMap);

                // then, process the message
                processRecord(record);
            }
//            consumer.commitSync();
        }
    }

    private void processRecord(ConsumerRecord<String, OrderChange> record) {
        System.out.println(record);
        if (record.value().getBefore() == null && record.value().getAfter().getStatus() == OrderStatus.CREATED) {
            System.out.println("Will generate label for order " + record.value().getAfter().getId());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            kafkaTemplate.send("shipping-label-generated", record.value().getAfter().getId(),
                    String.format("Shipping label for order %s generated", record.value().getAfter().getId()));
        }
    }
}
