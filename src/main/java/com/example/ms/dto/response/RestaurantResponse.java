package com.example.ms.dto.response;


import com.example.ms.enums.RestaurantStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
