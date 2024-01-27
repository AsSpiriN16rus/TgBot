package com.skillbox.cryptobot.bot.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class User
{
    private String uuid;
    private String id;
    private BigDecimal price;
    private String last_notification;

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", last_notification=" + last_notification +
                '}';
    }
}
