package com.example.ms.service.abstraction;

import com.example.ms.dto.request.RestaurantRequest;
import com.example.ms.dto.response.RestaurantResponse;
import java.util.List;

public interface RestaurantService {
    RestaurantResponse save(RestaurantRequest request);

    RestaurantResponse findById(Long id);

    List<RestaurantResponse> findAll();


}
