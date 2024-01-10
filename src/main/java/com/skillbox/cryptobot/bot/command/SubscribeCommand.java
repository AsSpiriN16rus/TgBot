package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.repository.UserRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final CryptoCurrencyService service;
    private final UserRepository userRepository;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            String idUser = message.getFrom().getId().toString();
            String[] textSplit = message.getText().split(" ");
            if (textSplit.length != 2){
                answer.setText("Не верно введена команда");
                absSender.execute(answer);
                answer.setText("/subscribe [число]");
                absSender.execute(answer);
            }else {
                String price = "0";
                int i = 0;
                for (String split : textSplit){
                    if (i != 1){
                        i++;
                    }else {
                        price = split;
                    }
                }
                userRepository.addPrice(idUser, price);
                answer.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD");
                absSender.execute(answer);
                answer.setText(String.format("Новая подписка создана на стоимость %s USD", price));
                absSender.execute(answer);
            }
        } catch (Exception e) {
            log.error("Ошибка возникла /subscribe методе", e);
        }
    }
}