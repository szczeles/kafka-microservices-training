package pl.training.kafka.shipping;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import pl.training.kafka.avromodel.Order;
import pl.training.kafka.avromodel.OrderStatus;
import pl.training.kafka.avromodel.User;

import java.util.Collections;
import java.util.Properties;

public class KafkaStreamsBasedShippingService {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "shipping-service-2");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        properties.setProperty(StreamsConfig.EXACTLY_ONCE, "true");
        properties.setProperty(StreamsConfig.STATE_DIR_CONFIG, "/tmp/shipping-service");

        SpecificAvroSerde<User> userSerde = new SpecificAvroSerde<>();
        userSerde.configure(
                Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"),
                false
        );

        SpecificAvroSerde<Order> orderSerde = new SpecificAvroSerde<>();
        userSerde.configure(
                Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"),
                false
        );

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        GlobalKTable<String, User> users =
                streamsBuilder.globalTable("user", Consumed.with(Serdes.String(), userSerde));

        streamsBuilder.stream("order", Consumed.with(Serdes.String(), orderSerde))
                .filter((orderId, order) -> order.getStatus() == OrderStatus.CREATED)
                .join(users,
                        (orderId, order) -> order.getUserId().toString(),
                        (order, user) -> "Please send product " + order.getProductId() + " to address " + user.getAddress())
                .to("shipping-labels-commands", Produced.with(Serdes.String(), Serdes.String()));

        Topology topology = streamsBuilder.build();

        new KafkaStreams(topology, properties).start();
    }
}
