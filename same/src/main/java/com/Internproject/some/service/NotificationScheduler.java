package com.Internproject.some.service;

import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
 
    private final ViralityService viralityService;
 
    public NotificationScheduler(ViralityService viralityService) {
        this.viralityService = viralityService;
    }
    @Scheduled(fixedRate = 5000)
    public void sweepPendingNotifications() {
        System.out.println(" Starting notification sweep...");
 
        Set<String> pendingKeys = viralityService.getAllPendingNotifKeys();
 
        if (pendingKeys.isEmpty()) {
            System.out.println(" No pending notifications found.");
            return;
        }
 
        for (String key : pendingKeys) {
            String[] parts = key.split(":");
            if (parts.length < 2) continue;
 
            Long userId;
            try {
                userId = Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            List<String> messages = viralityService.popPendingNotifications(userId);
 
            if (messages.isEmpty()) continue;
            String first = messages.get(0);
            int others = messages.size() - 1;
 
            String summary = others > 0
                    ? first + " and " + others + " others interacted with your posts."
                    : first;
 
            System.out.println("[SUMMARIZED PUSH NOTIFICATION] User " + userId + ": " + summary);
        }
 
        System.out.println("[CRON] Notification sweep complete.");
    }
}
