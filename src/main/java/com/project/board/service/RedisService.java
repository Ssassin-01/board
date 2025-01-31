package com.project.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Refresh Token 저장
     *
     * @param username 사용자 이름
     * @param refreshToken 저장할 Refresh Token
     * @param expiration 만료 시간 (밀리초)
     */
    public void saveRefreshToken(String username, String refreshToken, long expiration) {
        if (expiration <= 0) {
            throw new IllegalArgumentException("만료 시간이 0 이하일 수 없습니다.");
        }
        log.info("Saving Refresh Token for {} with expiration: {} ms", username, expiration);
        redisTemplate.opsForValue().set(
                generateKey(username),
                refreshToken,
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Refresh Token 가져오기
     *
     * @param username 사용자 이름
     * @return 저장된 Refresh Token (없으면 null)
     */
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(generateKey(username));
    }

    /**
     * Refresh Token 삭제
     *
     * @param username 사용자 이름
     */
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(generateKey(username));
    }

    /**
     * Redis 키 생성
     *
     * @param username 사용자 이름
     * @return Redis 키
     */
    private String generateKey(String username) {
        return "refreshToken:" + username;
    }
}
