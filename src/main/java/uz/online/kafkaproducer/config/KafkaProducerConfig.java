package uz.online.kafkaproducer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.producer.bootstrap-server}")
    private String bootstrapServer;

    //KafkaTemplates
    @Bean
    public KafkaTemplate<Object, Object> stringKafkaProducer() {
        var kafkaTemplate = new KafkaTemplate<>(defaultKafkaProducerFactory());
        kafkaTemplate.setDefaultTopic("sample");
        return kafkaTemplate;
    }

    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate() {
        var routingKafkaTemplateProducerFactory = new LinkedHashMap<Pattern, ProducerFactory<Object, Object>>();

        routingKafkaTemplateProducerFactory.put(Pattern.compile("sample"), byteValueProducerFactory());
        routingKafkaTemplateProducerFactory.put(Pattern.compile(".+"), defaultKafkaProducerFactory());

        return new RoutingKafkaTemplate(routingKafkaTemplateProducerFactory);
    }
    //end

    //KafkaProducer
    @Bean
    public ProducerFactory<Object, Object> defaultKafkaProducerFactory() {
        var defaultKafkaProducerFactoryConfig = new HashMap<String, Object>();
        defaultKafkaProducerFactoryConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        defaultKafkaProducerFactoryConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        defaultKafkaProducerFactoryConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(defaultKafkaProducerFactoryConfig);
    }

    @Bean
    public ProducerFactory<Object, Object> byteValueProducerFactory() {
        var byteValueKafkaProducerFactoryConfig = new HashMap<String, Object>();
        byteValueKafkaProducerFactoryConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        byteValueKafkaProducerFactoryConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        byteValueKafkaProducerFactoryConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

        return new DefaultKafkaProducerFactory<>(byteValueKafkaProducerFactoryConfig);
    }
    //end
}
