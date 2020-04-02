package pl.training.kafka.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import pl.training.kafka.model.OrderChange;

import java.io.IOException;

public class OrderChangeJSONDeserializer implements Deserializer<OrderChange> {
    @Override
    public OrderChange deserialize(String s, byte[] bytes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(bytes, OrderChange.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
