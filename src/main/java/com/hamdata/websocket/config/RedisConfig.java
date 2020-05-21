package com.hamdata.websocket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamdata.websocket.redis.RedisPublishService;
import com.hamdata.websocket.redis.RedisPublishServiceImpl;
import com.hamdata.websocket.redis.listener.MessageSubscribeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Configuration
public class RedisConfig {

    @Resource
    private MessageSubscribeListener messageSubscribeListener;

    @Resource
    private Executor asyncTaskExecutor;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        final ObjectMapper mapper = new ObjectMapper();
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(mapper));
        return redisTemplate;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(messageSubscribeListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory,
                                                 MessageListenerAdapter messageListenerAdapter,
                                                 ChannelTopic topic) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, topic);
        container.setTaskExecutor(asyncTaskExecutor);
        return container;
    }

    @Bean
    RedisPublishService redisPublishService(RedisTemplate<String, Object>  redisTemplate,
                                            ChannelTopic topic) {
        return new RedisPublishServiceImpl(redisTemplate, topic);
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("topic");
    }
}
