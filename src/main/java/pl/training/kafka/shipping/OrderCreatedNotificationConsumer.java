package pl.training.kafka.shipping;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class OrderCreatedNotificationConsumer implements ApplicationRunner {
    private final KafkaConsumer<String, String> consumer;

    public OrderCreatedNotificationConsumer() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092"); // or localhost:9092
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "shipping-service");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("order-created"));
//        Collections.singletonList("order-created");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Consumer started");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                processRecord(record);


                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                HashMap<TopicPartition, OffsetAndMetadata> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put(topicPartition, offsetAndMetadata);

                // NextOffset.for(record).build() -> possible improvement

                consumer.commitSync(objectObjectHashMap);
            }
//            consumer.commitSync();
        }
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        System.out.println(record);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
