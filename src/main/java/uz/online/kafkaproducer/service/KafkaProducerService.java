package uz.online.kafkaproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private @Qualifier(value = "stringKafkaProducer") KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private RoutingKafkaTemplate routingKafkaTemplate;

    public void send(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message)
                    .thenAccept(result -> log.info("Message sent to kafka topic: {}", result.getProducerRecord().topic()));
        } catch (Exception e) {
            log.error("Error while sending message to {}, error {}", topic, e.getMessage());
        }
    }

    public void sendByRouter(String topic, Object message) {
        try {
            routingKafkaTemplate.send(topic, message)
                    .thenAccept(result -> log.info("Message send By routingKafkaTemplate, message: {}", result.getProducerRecord().value()));
        } catch (Exception e) {
            log.error("Error while sending message, error: ", e.getMessage());
        }
    }
}
