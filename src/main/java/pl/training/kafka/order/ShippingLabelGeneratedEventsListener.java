package pl.training.kafka.order;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShippingLabelGeneratedEventsListener {

    @KafkaListener(topics = "shipping-label-generated", groupId = "order-service")
    public void listen(ConsumerRecord<String, String> event) {
        System.out.println("Notification arrived");
        System.out.println(event);

    }
}
