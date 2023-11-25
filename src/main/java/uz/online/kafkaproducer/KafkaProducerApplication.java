package uz.online.kafkaproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import uz.online.kafkaproducer.service.KafkaProducerService;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "uz.online")
public class KafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Bean
    public ApplicationRunner runner(KafkaProducerService service) {
        return args -> {
            kafkaAdmin.createOrModifyTopics(TopicBuilder.name("runtime-topic").build());
            service.send("sample", "signal1");
            service.sendByRouter("sample", "signal1".getBytes());
            service.sendByRouter(  "first", "first message");
        };
    }
}
