package pl.training.kafka.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.training.kafka.model.Order;
import pl.training.kafka.model.OrderChange;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrdersDB {

    @Autowired
    private OrderChangeSender orderChangeSender;

    private ConcurrentHashMap<String, Order> db = new ConcurrentHashMap<>();

    public Order get(String orderId) {
        return db.get(orderId);
    }

    public void save(Order order) {

        // get previous state
        Order previousOrderState = get(order.getId());

        db.put(order.getId(), order);

        // build change event
        OrderChange orderChange = new OrderChange();
        orderChange.setBefore(previousOrderState);
        orderChange.setAfter(order);
        orderChange.setTs(LocalDateTime.now());

        // & send
        orderChangeSender.send(order.getId(), orderChange);
    }
}
