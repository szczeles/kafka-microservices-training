package pl.training.kafka.order;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.training.kafka.model.Order;
import pl.training.kafka.model.OrderStatus;

@Component
public class ShippingLabelGeneratedEventsListener {

    @Autowired
    private OrdersDB ordersDB;

    @KafkaListener(topics = "shipping-label-generated", groupId = "order-service")
    public void listen(ConsumerRecord<String, String> event) {
        System.out.println("Notification arrived");
        System.out.println(event);
        String orderId = event.key();
        Order currentOrder = ordersDB.get(orderId);

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(currentOrder.getUserId());
        order.setProductId(currentOrder.getProductId());
        order.setQuantity(currentOrder.getQuantity());
        order.setStatus(OrderStatus.PREPARED);

        ordersDB.save(order);
    }
}
