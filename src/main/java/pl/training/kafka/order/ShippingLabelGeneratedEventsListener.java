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


// ConsumerRecord(topic = order-change, partition = 0, leaderEpoch = 0, offset = 10, CreateTime = 1585830891108,
// serialized key size = 36, serialized value size = 285, headers = RecordHeaders(headers = [], isReadOnly = false),
// key = 0a9cf681-ff10-491e-9665-83b6980afac9,
// value = OrderChange{
// before=Order{id='0a9cf681-ff10-491e-9665-83b6980afac9', userId='Adam123', productId='556', quantity=42, status=CREATED},
// after=Order{id='0a9cf681-ff10-491e-9665-83b6980afac9', userId='Adam123', productId='556', quantity=42, status=PREPARED},
// ts=2020-04-02T14:34:51.105447})
