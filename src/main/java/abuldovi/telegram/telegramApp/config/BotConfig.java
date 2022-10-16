package abuldovi.telegram.telegramApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.key}")
    String token;

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }

}
