package com.example.ms.dao.repository;

import com.example.ms.dao.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository <RestaurantEntity,Long> {
}
