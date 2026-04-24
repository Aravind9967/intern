package com.Internproject.some.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {
 
    private static final String VIRALITY_KEY   = "post:%d:virality_score";
    private static final String BOT_COUNT_KEY  = "post:%d:bot_count";
    private static final String COOLDOWN_KEY   = "cooldown:bot_%d:human_%d";
    private static final String NOTIF_SENT_KEY = "notif:sent:user_%d";
    private static final String NOTIF_LIST_KEY = "user:%d:pending_notifs";
 
    private static final int MAX_BOT_REPLIES   = 100;
    private static final long COOLDOWN_MINUTES = 10;
    private static final long NOTIF_MINUTES    = 15;
 
    private final RedisTemplate<String, String> redisTemplate;
 
    public ViralityService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void addBotReplyScore(Long postId) {
        redisTemplate.opsForValue().increment(viralityKey(postId), 1);
    }
 
    public void addHumanLikeScore(Long postId) {
        redisTemplate.opsForValue().increment(viralityKey(postId), 20);
    }
 
    public void addHumanCommentScore(Long postId) {
        redisTemplate.opsForValue().increment(viralityKey(postId), 50);
    }
 
    public Long getViralityScore(Long postId) {
        String val = redisTemplate.opsForValue().get(viralityKey(postId));
        return val == null ? 0L : Long.parseLong(val);
    }

    public boolean incrementBotCountAndCheck(Long postId) {
        Long count = redisTemplate.opsForValue().increment(botCountKey(postId));
        if (count != null && count > MAX_BOT_REPLIES) {
        
            redisTemplate.opsForValue().decrement(botCountKey(postId));
            return false; 
        }
        return true; 
    }
 
    public Long getBotCount(Long postId) {
        String val = redisTemplate.opsForValue().get(botCountKey(postId));
        return val == null ? 0L : Long.parseLong(val);
    }
    public boolean isCooldownActive(Long botId, Long humanId) {
        String key = cooldownKey(botId, humanId);
        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exists)) {
            return true; 
        }
        redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(COOLDOWN_MINUTES));
        return false;
    }
    public void handleNotification(Long userId, String message) {
        String sentKey = notifSentKey(userId);
        Boolean alreadySent = redisTemplate.hasKey(sentKey);
 
        if (Boolean.TRUE.equals(alreadySent)) {
            redisTemplate.opsForList().rightPush(notifListKey(userId), message);
        } else {
           
            System.out.println("[PUSH NOTIFICATION] Sent to User " + userId + ": " + message);
         
            redisTemplate.opsForValue().set(sentKey, "1", Duration.ofMinutes(NOTIF_MINUTES));
        }
    }
 
    public java.util.List<String> popPendingNotifications(Long userId) {
        String key = notifListKey(userId);
        Long size = redisTemplate.opsForList().size(key);
        if (size == null || size == 0) return java.util.Collections.emptyList();
 
        java.util.List<String> messages = redisTemplate.opsForList().range(key, 0, size - 1);
        redisTemplate.delete(key);
        return messages == null ? java.util.Collections.emptyList() : messages;
    }
 
    public java.util.Set<String> getAllPendingNotifKeys() {
        java.util.Set<String> keys = redisTemplate.keys("user:*:pending_notifs");
        return keys == null ? java.util.Collections.emptySet() : keys;
    }
 
    private String viralityKey(Long postId)  { 
    	return String.format(VIRALITY_KEY, postId);
    	}
    private String botCountKey(Long postId)  { 
    	return String.format(BOT_COUNT_KEY, postId);
    	}
    private String cooldownKey(Long b, Long h) {
    	return String.format(COOLDOWN_KEY, b, h);
    	}
    private String notifSentKey(Long userId) {
    	return String.format(NOTIF_SENT_KEY, userId); 
    	}
    private String notifListKey(Long userId) { 
    	return String.format(NOTIF_LIST_KEY, userId);
    	}
}