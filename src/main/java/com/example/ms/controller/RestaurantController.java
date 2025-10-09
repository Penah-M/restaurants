package com.example.ms.controller;

import com.example.ms.dto.request.RestaurantRequest;
import com.example.ms.dto.response.RestaurantResponse;
import com.example.ms.service.abstraction.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE,makeFinal = true)
@RequestMapping("v1/restaurant")
public class RestaurantController {

    RestaurantService restaurantService;

    @PostMapping()
    RestaurantResponse save(@RequestBody RestaurantRequest request){
        return restaurantService.save(request);
    }

    @GetMapping("{id}/find")
    RestaurantResponse findById(@PathVariable Long id){
        return restaurantService.findById(id);
    }

    @GetMapping("findAll")
    List<RestaurantResponse> findAll(){
        return restaurantService.findAll();
    }
}
