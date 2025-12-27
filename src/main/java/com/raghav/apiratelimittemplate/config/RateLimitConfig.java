package com.raghav.apiratelimittemplate.config;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.jedis.Bucket4jJedis;

import java.time.Duration;

public class RateLimitConfig {
    public static BucketConfiguration loginLimiter(){
        return BucketConfiguration.builder()
                .addLimit(Limit ->
                        Limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
                .build();
        //LimitChanges here capacity tell  the capacity of bucket and refil greddy tells number
        //Numbers of tokens to be refil in the bucket in a given duration of time

    }
    public static BucketConfiguration PaymentLimiter(){
        return BucketConfiguration.builder()
                .addLimit(Limit ->
                        Limit.capacity(2).refillGreedy(2,Duration.ofMinutes(1)))
                .build();
    }
}
//WE CAN DIFFERENT BUCKETCONFIG FUNCTION LIKE SIGNUP LIMITER AND MANY MORE