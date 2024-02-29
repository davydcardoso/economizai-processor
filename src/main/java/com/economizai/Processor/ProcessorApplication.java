package com.economizai.Processor;

import com.economizai.Processor.mail.EmailSender;
import com.economizai.Processor.queues.RedisConfig;
import com.economizai.Processor.queues.services.RedisMessageSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({RedisConfig.class, EmailSender.class})
@AutoConfigureAfter(RedisMessageSubscriber.class)
@SpringBootApplication
public class ProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}

}
