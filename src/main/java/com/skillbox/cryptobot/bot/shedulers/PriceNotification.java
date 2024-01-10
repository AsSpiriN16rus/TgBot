package com.skillbox.cryptobot.bot.shedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PriceNotification
{
    @Value("${notification-time}")
    private String notificationTime;



    @Scheduled(fixedRateString = "${frequency-course-updates}")
    public void computePrice(){
        System.out.println("----");
        System.out.println(notificationTime);
    }


}
