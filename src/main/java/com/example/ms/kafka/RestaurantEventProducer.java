package com.example.ms.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantEventProducer {

    private final KafkaTemplate<String, RestaurantEvent> kafkaTemplate;

    public void sendRestaurantEvent(RestaurantEvent event) {
        kafkaTemplate.send("restaurant", event);
    }
}