package pl.training.kafka.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
import pl.training.kafka.model.OrderChange;

public class OrderChangeJSONSerializer implements Serializer<OrderChange> {
    @Override
    public byte[] serialize(String s, OrderChange orderChange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsBytes(orderChange);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
