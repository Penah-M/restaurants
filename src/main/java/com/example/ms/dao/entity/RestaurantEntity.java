package com.example.ms.dao.entity;


import com.example.ms.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurants")
public class RestaurantEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String address;


    @Enumerated(EnumType.STRING)
    RestaurantStatus status;


    @CreationTimestamp
    LocalDateTime createdAt;


    @UpdateTimestamp
    LocalDateTime updatedAt;
}
