package com.Internproject.some.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Internproject.some.entity.Bot;
import com.Internproject.some.repository.BotRepository;

@Service
public class BotService {
 
    private final BotRepository botRepository;
 
    public BotService(BotRepository botRepository) {
        this.botRepository = botRepository;
    }
 
    public Bot createBot(String name, String personaDescription) {
        Bot bot = new Bot();
        bot.setName(name);
        bot.setPersonaDescription(personaDescription);
        return botRepository.save(bot);
    }
 
    public Bot getBotById(Long id) {
        return botRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bot not found with id: " + id));
    }
 
    public List<Bot> getAllBots() {
        return botRepository.findAll();
    }
 
    public Bot updateBot(Long id, String name, String personaDescription) {
        Bot bot = getBotById(id);
        bot.setName(name);
        bot.setPersonaDescription(personaDescription);
        return botRepository.save(bot);
    }
 
    public void deleteBot(Long id) {
        botRepository.deleteById(id);
    }
}
