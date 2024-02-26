package com.economizai.Processor.Jobs;

import com.economizai.Processor.Jobs.Processor.ProcessRecurrentTransactions;
import com.economizai.Processor.Queues.services.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@EnableScheduling
public class CronMain {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Scheduled(cron = "1 * * * * *")
    public void checkPerHour() {
//        new ProcessRecurrentTransactions().run();

        redisMessagePublisher.publish("HELLO");

    }

}
