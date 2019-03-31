package com.wonderqin.schedulingtask.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ScheduleTask
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-03-31 22:50
 **/
@Component
public class ScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRate(){
        LOGGER.info("fixedRate——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedDelay = 1000)
    public void reportCurrentTimeWithFixedDelay(){
        LOGGER.info("fixedDelay——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedRateString = "3000")
    public void reportCurrentTimeWithFixedRateString(){
        LOGGER.info("fixedRateString——the current time is {}" , FORMAT.format(new Date()));
    }

    @Scheduled(fixedDelayString = "4000")
    public void reportCurrentTimeWith(){
        LOGGER.info("fixedDelayString——the current time is {}" , FORMAT.format(new Date()));
    }
}
