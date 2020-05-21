package com.hamdata.websocket.redis;

import com.hamdata.websocket.dto.MessageDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class RedisPublishServiceImpl implements RedisPublishService{

    private RedisTemplate<String, Object> redisTemplate;

    private ChannelTopic topic;

    public RedisPublishServiceImpl(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(MessageDTO messageDTO) {
        redisTemplate.convertAndSend(topic.getTopic(), messageDTO);
    }
}
