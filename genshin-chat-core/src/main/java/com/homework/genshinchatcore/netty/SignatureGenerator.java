package com.homework.genshinchatcore.netty;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 生成时间戳和签名
 */
public class SignatureGenerator {
    
    // 请替换为你的实际值
    private static final String APP_KEY = "846892d47bb022f0ae271c6f0cb0fb8d";
    private static final String ACCESS_TOKEN = "5a0f504888ecba53ed9bccebb1597ee4";
    
    public static void main(String[] args) {
        try {
            // 生成当前时间戳（秒）
            long timestamp = 1758375096;
            
            // 计算签名
            String signature = calculateSignature(APP_KEY, timestamp, ACCESS_TOKEN);
            
            // 打印结果
            System.out.println("timestamp: " + timestamp);
            System.out.println("signature: " + signature);
            
        } catch (Exception e) {
            System.err.println("计算签名时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 计算签名
     */
    private static String calculateSignature(String appKey, long timestamp, String accessToken) throws Exception {
        // 构造签名字符串
        String signString = "appkey=" + appKey + "&timestamp=" + timestamp;
        
        // 使用 HmacSHA256 加密
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(accessToken.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        
        byte[] hashBytes = mac.doFinal(signString.getBytes(StandardCharsets.UTF_8));
        
        // Base64 编码
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}