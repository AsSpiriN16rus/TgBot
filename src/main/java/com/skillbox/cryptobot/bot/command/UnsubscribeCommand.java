package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final UserRepository userRepository;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            String idUser = message.getFrom().getId().toString();
            String price = userRepository.getSubscribe(idUser);
            if (price == null){
                answer.setText("Активные подписки отсутствуют");
                absSender.execute(answer);
            }else {
                userRepository.delPrice(idUser);
                answer.setText("Подписка отменена");
                absSender.execute(answer);
            }
        } catch (Exception e) {
            log.error("Ошибка возникла /unsubscribe методе", e);
        }
    }
}