package com.economizai.Processor.queues.services;

import com.economizai.Processor.jobs.Processor.ProcessSendRecoverPasswordEmail;
import com.economizai.Processor.queues.dto.RecoverPassword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RedisMessageSubscriber implements MessageListener {

    Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        JSONObject json = new JSONObject(message.toString());

        String taskRunning = json.getString("task");

        if (Objects.equals(taskRunning, "")) {
            return;
        }

        if (Objects.equals(taskRunning, "recover-password")) {
            System.out.println("Process Queue in Running -> Recover Password");
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                new ProcessSendRecoverPasswordEmail().run(objectMapper.readValue(json.get("data").toString(), RecoverPassword.class));
            } catch (JsonProcessingException e) {
                System.out.println("Error in convert JSON string in RecoverPassword.class: "+e.getMessage());
            }
        }
    }
}
