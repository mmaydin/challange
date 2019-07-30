package com.mma.challenge.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.mma.challenge.entity.CityLogData;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

	@Value("${kafka.bootstrapservers}")
	public String bootstrapServers;

	@Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "temp-groupid.group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return props;
    }

    @Bean
    public ConsumerFactory<String, CityLogData> consumerFactory() {
      return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
    		  new StringDeserializer(),
    		  new JsonDeserializer<>(CityLogData.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CityLogData>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CityLogData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}