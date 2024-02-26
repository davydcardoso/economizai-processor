package com.economizai.Processor.Jobs;

import com.economizai.Processor.Jobs.Processor.ProcessRecurrentTransactions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@EnableScheduling
public class CronMain {

    @Scheduled(cron = "1 * * * * *")
    public void checkPerHour() {
        new ProcessRecurrentTransactions().run();
    }

}
