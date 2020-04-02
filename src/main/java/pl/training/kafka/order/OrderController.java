package pl.training.kafka.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.training.kafka.model.Order;
import pl.training.kafka.model.OrderStatus;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderCreatedNotificationSender orderCreatedNotificationSender;

    @Autowired
    private OrdersDB ordersDB;

    @PostMapping
    public String createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        // save to DB

        String orderId = UUID.randomUUID().toString();
        Order order = new Order();
        order.setProductId(orderCreateRequest.getProductId());
        order.setQuantity(orderCreateRequest.getQuantity());
        order.setUserId(orderCreateRequest.getUserId());
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        ordersDB.save(order);

        orderCreatedNotificationSender.send(orderId);

        return orderId;
    }
}
