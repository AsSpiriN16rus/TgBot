package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final UserRepository userRepository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
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
                answer.setText(String.format("Вы подписаны на стоимость биткоина %s USD", price));
                absSender.execute(answer);
            }
        } catch (Exception e) {
            log.error("Ошибка возникла /get_subscription методе", e);
        }
    }
}