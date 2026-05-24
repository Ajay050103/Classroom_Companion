package com.classroom.config;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.classroom.bot.ClassroomBot;

import jakarta.annotation.PostConstruct;

@Configuration
public class TelegramBotInitializer {

    private final ClassroomBot classroomBot;

    public TelegramBotInitializer(
            ClassroomBot classroomBot
            
    ) {
        this.classroomBot = classroomBot;
    }

    @PostConstruct
    public void init() {

        try {

            TelegramBotsApi botsApi =
                    new TelegramBotsApi(
                            DefaultBotSession.class
                    );

            botsApi.registerBot(
                    classroomBot
            );

            System.out.println(
                    "Telegram Bot Registered Successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}