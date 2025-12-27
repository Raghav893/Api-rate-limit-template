package com.raghav.apiratelimittemplate.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.jedis.Bucket4jJedis;
import io.github.bucket4j.redis.jedis.cas.JedisBasedProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
//THIS WILL REMAIN SAME FOR EVERY PROJECT AS IT IS JUST A SETUP FOR REDIS
@Configuration
public class RedisConfig {
    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        return new JedisPool(config,"localhost",6379);
    }
    @Bean
    public JedisBasedProxyManager<String> proxyManager(JedisPool jedisPool){
        return Bucket4jJedis.casBasedBuilder(jedisPool)
                .keyMapper(Mapper.STRING).expirationAfterWrite(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1)) )
                .build();
    }
}
