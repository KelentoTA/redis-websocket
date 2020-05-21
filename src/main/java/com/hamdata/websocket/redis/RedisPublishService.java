package com.hamdata.websocket.redis;

import com.hamdata.websocket.dto.MessageDTO;

public interface RedisPublishService {

    void publish(MessageDTO messageDTO);
}
