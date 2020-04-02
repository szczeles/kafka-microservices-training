package pl.training.kafka.order;

import org.joda.time.DateTime;
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

    @Autowired
    private OrderEventSourcingSender orderEventSourcingSender;

    private ConcurrentHashMap<String, Order> db = new ConcurrentHashMap<>(); // rocksdb

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

        pl.training.kafka.avromodel.Order avroOrder = new pl.training.kafka.avromodel.Order();
        avroOrder.setId(order.getId());
        avroOrder.setProductId(order.getProductId());
        avroOrder.setQuantity(order.getQuantity());
        avroOrder.setStatus(pl.training.kafka.avromodel.OrderStatus.valueOf(order.getStatus().name()));
        avroOrder.setUpdatedAt(DateTime.now());
        avroOrder.setUserId(order.getUserId());
        orderEventSourcingSender.send(avroOrder);
    }
}
