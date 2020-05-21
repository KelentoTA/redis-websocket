package com.hamdata.websocket.controller;

import com.google.common.collect.Maps;
import com.hamdata.websocket.dto.MessageDTO;
import com.hamdata.websocket.redis.RedisPublishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.Map;

@RestController
public class WebSocketController {

    @Resource
    private RedisPublishService redisPublishService;

    @GetMapping("page")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }

    @GetMapping("/pushMsg/{message}")
    public void pushMessage(@PathVariable("message") String message) {
        MessageDTO messageDTO = new MessageDTO();
        Map<String, Object> data = Maps.newHashMap();
        data.put("message", message);
        messageDTO.setData(data);
        redisPublishService.publish(messageDTO);
    }

}
