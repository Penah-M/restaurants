package com.example.ms.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Data
public class RestaurantRequest {

    @NotBlank(message = "Bos ola bilmez")
    String name;

    @NotBlank(message = "Bos ola bilmez")
    String address;
}
