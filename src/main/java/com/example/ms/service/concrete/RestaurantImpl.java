package com.example.ms.service.concrete;


import com.example.ms.dao.entity.RestaurantEntity;
import com.example.ms.dao.repository.RestaurantRepository;
import com.example.ms.dto.request.RestaurantRequest;
import com.example.ms.dto.response.RestaurantResponse;
import com.example.ms.enums.RestaurantStatus;
import com.example.ms.exception.NotFoundException;
import com.example.ms.kafka.RestaurantEvent;
import com.example.ms.service.abstraction.RestaurantService;
import com.example.ms.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE,makeFinal = true)

public class RestaurantImpl implements RestaurantService {

    RestaurantRepository restaurantRepository;
    CacheUtil cacheUtil;

    KafkaTemplate<String, RestaurantEvent> kafkaTemplate;



    static  String TOPIC = "restaurant";


    @Override
    public RestaurantResponse save(RestaurantRequest request) {
        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .status(RestaurantStatus.ACTIVE)
                .build();

        restaurantRepository.save(restaurantEntity);
        RestaurantEvent event = RestaurantEvent.builder()
                .name(restaurantEntity.getName())
                .address(restaurantEntity.getAddress())
                .build();


        kafkaTemplate.send(TOPIC,event);
//        KAFKA------
//        kafkaTemplate.send(TOPIC, restaurantEntity.getName(),restaurantEntity.getAddress());
//
//        REDIS---

        cacheUtil.saveRestaurant(restaurantEntity.getId(), restaurantEntity,10L, ChronoUnit.MINUTES);

         RestaurantResponse response= mapToResponse(restaurantEntity);

         return  response;

    }

    @Override

    public RestaurantResponse findById(Long id) {

        RestaurantEntity cachedRestourant =(RestaurantEntity) cacheUtil.getRestaurant(id);
        if(cachedRestourant !=null){
            System.out.println("Redisden oxunur melumat..");
            RestaurantResponse response = mapToResponse(cachedRestourant);
            return response;
        }

        RestaurantEntity entity = restaurantRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Qeyd olunan ID tapilmadi"));
        cacheUtil.saveRestaurant(id,entity,10L,ChronoUnit.MINUTES);

        System.out.println("DATA BAZADAN oxuyub cache yazildi");

          RestaurantResponse restaurantResponse = mapToResponse(entity);

          return restaurantResponse;
    }

    @Override
    public List<RestaurantResponse> findAll() {
        Collection<Object> cached = cacheUtil.getAllRestaurants();


        List<RestaurantResponse> responses = new ArrayList<>();

        if(!cached.isEmpty()){
            System.out.println("Redisden oxunur melumat..");
            for(Object obj :cached){
                responses.add(mapToResponse((RestaurantEntity)obj));
            }
            return responses;
        }
        List<RestaurantEntity> entities = restaurantRepository.findAll();

        for(RestaurantEntity entity:entities){
                RestaurantResponse response= mapToResponse(entity);
                cacheUtil.saveRestaurant(entity.getId(),entity,10l,ChronoUnit.MINUTES);
                responses.add(response);
        }

        return responses;


    }

    RestaurantResponse mapToResponse(RestaurantEntity entity){
        return RestaurantResponse.builder()
                .name(entity.getName())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .id(entity.getId())
                .build();
    }
}
