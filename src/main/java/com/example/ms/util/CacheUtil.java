package com.example.ms.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CacheUtil {

    RedissonClient redissonClient;

    private static final String RESTAURANT_MAP = "restaurants";

    // Save bir restoran (id üzrə)
    public void saveRestaurant(Long id, Object restaurant, Long expireTime, TemporalUnit unit) {
        // Fərdi bucket
        RBucket<Object> bucket = redissonClient.getBucket("restaurant:" + id);
        bucket.set(restaurant);
        bucket.expire(Duration.of(expireTime, unit));

        // Map-a da əlavə et (findAll üçün)
        RMap<Long, Object> map = redissonClient.getMap(RESTAURANT_MAP);
        map.put(id, restaurant);
    }

    // FindById
    public Object getRestaurant(Long id) {
        // Əvvəl fərdi bucket-dən oxu
        RBucket<Object> bucket = redissonClient.getBucket("restaurant:" + id);
        Object value = bucket.get();

        if (value != null) return value;

        // Əgər bucket boşdursa, map-dan yoxla
        RMap<Long, Object> map = redissonClient.getMap(RESTAURANT_MAP);
        return map.get(id);
    }

    // FindAll
    public Collection<Object> getAllRestaurants() {
        RMap<Long, Object> map = redissonClient.getMap(RESTAURANT_MAP);
        return map.values();
    }

    // Delete
    public void removeRestaurant(Long id) {
        // Bucket-dən sil
        RBucket<Object> bucket = redissonClient.getBucket("restaurant:" + id);
        bucket.delete();

        // Map-dan sil
        RMap<Long, Object> map = redissonClient.getMap(RESTAURANT_MAP);
        map.remove(id);
    }
}
