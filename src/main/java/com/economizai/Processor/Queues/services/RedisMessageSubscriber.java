package com.economizai.Processor.Queues.services;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisMessageSubscriber implements MessageListener {

    Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("Receive message: {}", message);

        JSONObject json = new JSONObject(message.toString());

        System.out.println(json.get("task"));
    }
}
