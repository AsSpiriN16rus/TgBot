package com.skillbox.cryptobot.bot.shedulers;

import com.skillbox.cryptobot.bot.model.User;
import com.skillbox.cryptobot.bot.repository.UserRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@AllArgsConstructor
public class PriceNotification
{
    @Value("${notification-time}")
    private Long notificationTime;


    private final CryptoCurrencyService service;
    private final UserRepository userRepository;
    private final AbsSender absSender;
    @Autowired
    public PriceNotification(CryptoCurrencyService service, UserRepository userRepository, AbsSender absSender) {
        this.service = service;
        this.userRepository = userRepository;
        this.absSender = absSender;
    }


    @Scheduled(fixedRateString = "${frequency-course-update}")
    public void computePrice() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formatDate = formatter.format(date);

        double bitcoinPrice = service.getBitcoinPrice();
        List<User> userList = userRepository.getUserNotification(bitcoinPrice);
        for (User user : userList){
            SendMessage sendMessage = new SendMessage();
            int result = 0;
            if (user.getLast_notification() == null){
                sendMessage(user,sendMessage,bitcoinPrice,formatDate);
            }else {
                try {
                    Date lastNotification = formatter.parse(user.getLast_notification());
                    Calendar cl = Calendar.getInstance();
                    cl.setTime(lastNotification);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(notificationTime);
                    cl.add(Calendar.MINUTE, Math.toIntExact(minutes));
                    result = cl.getTime().compareTo(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (result < 0) {
                    sendMessage(user,sendMessage,bitcoinPrice,formatDate);
                }
            }
        }
    }

    public void sendMessage(User user, SendMessage sendMessage, double bitcoinPrice, String formatDate){
        sendMessage.setChatId(user.getId());
        sendMessage.setText(String.format("Пора покупать, стоимость биткоина %s", bitcoinPrice));
        try {
            absSender.execute(sendMessage);
            userRepository.updateLastNotification(user.getId(), formatDate);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
