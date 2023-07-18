package laheezy.community.util;

import java.security.SecureRandom;

public class JwtSecretKeyGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) {
        generateSecretKey();
    }

    public static void generateSecretKey() {
        // 랜덤한 바이트 배열 생성
        byte[] keyBytes = new byte[256];
        new SecureRandom().nextBytes(keyBytes);

        // Base64 인코딩
        String secretKey = generateRandomString(keyBytes);

        // 생성된 키 출력
        System.out.println("Generated Secret Key: " + secretKey);
    }

    private static String generateRandomString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int index = Math.abs(b % ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
}
