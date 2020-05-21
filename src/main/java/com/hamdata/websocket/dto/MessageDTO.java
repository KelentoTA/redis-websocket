package com.hamdata.websocket.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import java.io.Serializable;

@Data
public class MessageDTO<T> implements Serializable {

    private static final long serialVersionUID = -2012173805944156376L;

    private T data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
