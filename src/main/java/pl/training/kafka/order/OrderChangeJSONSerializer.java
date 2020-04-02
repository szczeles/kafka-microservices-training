package pl.training.kafka.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import pl.training.kafka.model.OrderChange;

public class OrderChangeJSONSerializer implements Serializer<OrderChange> {
    @Override
    public byte[] serialize(String s, OrderChange orderChange) {
        try {
            return new ObjectMapper().writeValueAsBytes(orderChange);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
