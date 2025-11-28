package com.wimoor.erp.thirdparty.service.impl;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 海外仓密钥管理工具
 */
public class OverseasWarehouseKeyManager {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    // 密钥文件路径（建议放在安全的位置）
    private static final String PRIVATE_KEY_FILE = "config/overseas_private.key";
    private static final String PUBLIC_KEY_FILE = "config/overseas_public.key";

    /**
     * 生成并保存密钥对
     */
    public static void generateAndSaveKeyPair() throws Exception {
        // 创建配置目录
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 保存私钥
        savePrivateKey(keyPair.getPrivate());

        // 保存公钥
        savePublicKey(keyPair.getPublic());

        System.out.println("✅ 海外仓RSA密钥对生成成功！");
        System.out.println("私钥文件: " + PRIVATE_KEY_FILE);
        System.out.println("公钥文件: " + PUBLIC_KEY_FILE);
    }

    /**
     * 保存私钥到文件
     */
    private static void savePrivateKey(PrivateKey privateKey) throws Exception {
        String path = new ClassPathResource(PRIVATE_KEY_FILE).getPath();
        try (FileOutputStream fos = new FileOutputStream(path)) {
            String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            fos.write(privateKeyBase64.getBytes());
        }
    }

    /**
     * 保存公钥到文件
     */
    private static void savePublicKey(PublicKey publicKey) throws Exception {
        String path = new ClassPathResource(PUBLIC_KEY_FILE).getPath();
        try (FileOutputStream fos = new FileOutputStream(path)) {
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            fos.write(publicKeyBase64.getBytes());
        }
    }

    /**
     * 加载私钥
     */
    public static PrivateKey loadPrivateKey() throws Exception {
        String path = new ClassPathResource(PRIVATE_KEY_FILE).getPath();
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            String privateKeyBase64 = new String(keyBytes);

            byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        }
    }

    /**
     * 加载公钥
     */
    public static PublicKey loadPublicKey() throws Exception {
        String path = new ClassPathResource(PUBLIC_KEY_FILE).getPath();
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            String publicKeyBase64 = new String(keyBytes);

            byte[] decodedKey = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        }
    }

    /**
     * 获取公钥的Base64字符串（用于发送给电子口岸）
     */
    public static String getPublicKeyBase64() throws Exception {
        PublicKey publicKey = loadPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 获取私钥的Base64字符串
     */
    public static String getPrivateKeyBase64() throws Exception {
        PrivateKey privateKey = loadPrivateKey();
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 检查密钥是否存在
     */
    public static boolean keysExist() {
        String privateKeyPath = new ClassPathResource(PRIVATE_KEY_FILE).getPath();
        String publicKeyPath = new ClassPathResource(PUBLIC_KEY_FILE).getPath();
        return new File(privateKeyPath).exists() && new File(publicKeyPath).exists();
    }
}