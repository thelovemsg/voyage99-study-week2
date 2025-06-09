package kr.hhplus.be.server.common.utils;

import kr.hhplus.be.server.common.messages.MessageCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class CryptoUtils {

    private final String secretKey;

    // 🔒 보안 강화: 명시적으로 안전한 모드와 패딩 지정
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding"; // GCM 모드 + NoPadding
    private static final String HASH_ALGORITHM = "SHA-256";

    // GCM 모드 설정
    private static final int GCM_IV_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 16; // 128 bits

    public CryptoUtils(@Value("${app.crypto.secret-key:my-secret-key-16}") String secretKey) {
        // 32바이트로 패딩 (AES-256)
        this.secretKey = String.format("%-32s", secretKey).substring(0, 32);
    }

    /**
     * 랜덤 토큰 ID 생성
     */
    public String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 보안 강화된 랜덤 토큰 생성 (SecureRandom 사용)
     */
    public String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * AES-GCM 암호화 (IntelliJ 경고 해결)
     */
    public String encrypt(String plainText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            // 🔒 GCM 모드 사용 (보안 강화)
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // 랜덤 IV(Initialization Vector) 생성
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // IV + 암호화된 데이터를 함께 저장
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encrypted, 0, encryptedWithIv, GCM_IV_LENGTH, encrypted.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            log.error("error : {}", e);
            throw new RuntimeException(MessageCode.TOKEN_CRYPTO_ERROR.format(), e);
        }
    }

    /**
     * AES-GCM 복호화
     */
    public String decrypt(String encryptedText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedText);

            // IV와 암호화된 데이터 분리
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[encryptedWithIv.length - GCM_IV_LENGTH];

            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encrypted, 0, encrypted.length);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("error : {}", e);
            throw new RuntimeException(MessageCode.TOKEN_CRYPTO_ERROR.getMessage());
        }
    }

    /**
     * SHA-256 해시 생성
     */
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.error("error : {}", e);
            throw new RuntimeException(MessageCode.TOKEN_CRYPTO_ERROR.getMessage());
        }
    }

    /**
     * 솔트와 함께 해시 생성 (패스워드 등에 사용)
     */
    public String hashWithSalt(String input, String salt) {
        return hash(input + salt);
    }

    /**
     * 랜덤 솔트 생성
     */
    public String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 토큰 검증용 체크섬 생성
     */
    public String generateChecksum(String tokenId, Long userId, String queueId) {
        String combined = tokenId + ":" + userId + ":" + queueId;
        return hash(combined).substring(0, 8); // 처음 8자리만 사용
    }

    // 🔒 추가 보안 기능들

    /**
     * 시간 기반 해시 생성 (TOTP 스타일)
     */
    public String generateTimeBasedHash(String input, long timeWindow) {
        long currentTimeWindow = System.currentTimeMillis() / (timeWindow * 1000);
        return hash(input + ":" + currentTimeWindow);
    }

    /**
     * 안전한 문자열 비교 (타이밍 공격 방지)
     */
    public boolean safeEquals(String a, String b) {
        if (a == null || b == null) {
            return Objects.equals(a, b);
        }

        if (a.length() != b.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }

        return result == 0;
    }

    /**
     * 강화된 랜덤 문자열 생성 (특정 길이)
     */
    public String generateSecureRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }

        return sb.toString();
    }
}