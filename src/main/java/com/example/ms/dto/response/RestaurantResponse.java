package com.example.ms.dto.response;


import com.example.ms.enums.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Data
@Builder
public class RestaurantResponse {

    Long id;

    String name;

    String address;

    RestaurantStatus status;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
