package com.Internproject.some.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Internproject.some.entity.Bot;
import com.Internproject.some.service.BotService;

@RestController
@RequestMapping("/api/bots")
public class BotController {
 
    private final BotService botService;
 
    public BotController(BotService botService) {
        this.botService = botService;
    }
    @PostMapping
    public ResponseEntity<Bot> createBot(@RequestBody Map<String, Object> body) {
        String name              = (String) body.get("name");
        String personaDescription = (String) body.get("personaDescription");
        return ResponseEntity.ok(botService.createBot(name, personaDescription));
    }
    @GetMapping
    public ResponseEntity<List<Bot>> getAllBots() {
        return ResponseEntity.ok(botService.getAllBots());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Bot> getBot(@PathVariable Long id) {
        return ResponseEntity.ok(botService.getBotById(id));
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<Bot> updateBot(@PathVariable Long id,
                                         @RequestBody Map<String, Object> body) {
        String name              = (String) body.get("name");
        String personaDescription = (String) body.get("personaDescription");
        return ResponseEntity.ok(botService.updateBot(id, name, personaDescription));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBot(@PathVariable Long id) {
        botService.deleteBot(id);
        return ResponseEntity.ok("Bot deleted.");
    }
}
