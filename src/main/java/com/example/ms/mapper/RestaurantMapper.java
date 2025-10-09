package com.example.ms.mapper;

import com.example.ms.dao.entity.RestaurantEntity;
import com.example.ms.dto.request.RestaurantRequest;
import com.example.ms.dto.response.RestaurantResponse;
import com.example.ms.enums.RestaurantStatus;
import com.example.ms.kafka.RestaurantEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RestaurantMapper {

    public RestaurantResponse mapToResponse(RestaurantEntity entity) {
        return RestaurantResponse.builder()
                .name(entity.getName())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .id(entity.getId())
                .build();
    }

    public RestaurantEntity mapToEntity(RestaurantRequest request) {
        return RestaurantEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .status(RestaurantStatus.ACTIVE)
                .build();
    }

    public RestaurantEvent event(RestaurantEntity entity) {
        return RestaurantEvent.builder()
                .name(entity.getName())
                .address(entity.getAddress())
                .build();
    }
}
