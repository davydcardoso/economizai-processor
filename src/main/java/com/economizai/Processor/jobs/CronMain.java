package com.economizai.Processor.jobs;

import com.economizai.Processor.jobs.Processor.ProcessRecurrentTransactions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class CronMain {


    @Scheduled(cron = "30 8 * * * 3")
    public void checkPerHour() {
        new ProcessRecurrentTransactions().run();
    }

}
