package com.example.ms.service.concrete;


import com.example.ms.dao.entity.RestaurantEntity;
import com.example.ms.dao.repository.RestaurantRepository;
import com.example.ms.dto.request.RestaurantRequest;
import com.example.ms.dto.response.RestaurantResponse;
import com.example.ms.exception.NotFoundException;
import com.example.ms.kafka.RestaurantEvent;
import com.example.ms.mapper.RestaurantMapper;
import com.example.ms.service.abstraction.RestaurantService;
import com.example.ms.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE,makeFinal = true)

public class RestaurantImpl implements RestaurantService {

    RestaurantMapper mapper;
    RestaurantRepository restaurantRepository;
    CacheUtil cacheUtil;

    KafkaTemplate<String, RestaurantEvent> kafkaTemplate;

    static  String TOPIC = "restaurant";

    @Override
    public RestaurantResponse save(RestaurantRequest request) {

        RestaurantEntity restaurantEntity = mapper.mapToEntity(request);

        restaurantRepository.save(restaurantEntity);
        RestaurantEvent event = mapper.event(restaurantEntity);

        kafkaTemplate.send(TOPIC,event);

        cacheUtil.saveRestaurant(restaurantEntity.getId(), restaurantEntity,10L, ChronoUnit.MINUTES);

         RestaurantResponse response= mapper.mapToResponse(restaurantEntity);
         return  response;
    }

    @Override
    public RestaurantResponse findById(Long id) {

        RestaurantEntity cachedRestourant =(RestaurantEntity) cacheUtil.getRestaurant(id);
        if(cachedRestourant !=null){
            log.info("Redisden oxunur melumat..");
            RestaurantResponse response = mapper.mapToResponse(cachedRestourant);
            return response;
        }

        RestaurantEntity entity = restaurantRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Qeyd olunan ID tapilmadi"));
        log.info("Melumat redise elave olundu");
        cacheUtil.saveRestaurant(id,entity,10L,ChronoUnit.MINUTES);

        log.info("DATA BAZADAN oxuyub cache yazildi");

          RestaurantResponse restaurantResponse = mapper.mapToResponse(entity);
          return restaurantResponse;
    }

    @Override
    public List<RestaurantResponse> findAll() {
        Collection<Object> cached = cacheUtil.getAllRestaurants();
        List<RestaurantResponse> responses = new ArrayList<>();

        if(!cached.isEmpty()){
            log.info("Redisden oxunur melumat..");
            for(Object obj :cached){
                responses.add(mapper.mapToResponse((RestaurantEntity)obj));
            }
            return responses;
        }
        List<RestaurantEntity> entities = restaurantRepository.findAll();

        for(RestaurantEntity entity:entities){
                RestaurantResponse response= mapper.mapToResponse(entity);
                cacheUtil.saveRestaurant(entity.getId(),entity,10l,ChronoUnit.MINUTES);
                responses.add(response);
        }
        return responses;
    }

}
