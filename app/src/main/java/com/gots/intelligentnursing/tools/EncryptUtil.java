package com.gots.intelligentnursing.tools;

import android.os.Build;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zhqy
 * @date 2018/5/13
 */

public class EncryptUtil {

    /**
     * com.gots.intelligentnursing 32位的MD5加密
     */
    private static final String KEY = "86A106773FF85C59755369120DECF729";

    private static final byte[] IV = { 0x30, 0x31, 0x30, 0x32, 0x30,
            0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

    public static String encrypt(String data) throws Exception {
        byte[] rawKey = getRawKey(KEY.getBytes());
        byte[] result = encrypt(data.getBytes("utf-8"), rawKey);
        return toHex(result);
    }

    public static String decrypt(String encrypted) throws Exception {
        byte[] byteData = toByte(encrypted);
        byte[] rawKey = getRawKey(KEY.getBytes());
        byte[] result = decrypt(byteData, rawKey);
        return new String(result, "UTF8");
    }

    private static byte[] encrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return Ase(byteData, byteKey, Cipher.ENCRYPT_MODE);
    }

    private static byte[] decrypt(byte[] byteData, byte[] byteKey) throws Exception {
        return Ase(byteData, byteKey, Cipher.DECRYPT_MODE);
    }

    private static byte[] Ase(byte[] byteData, byte[] byteKey, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");
        cipher.init(mode, secretKeySpec, new IvParameterSpec(IV));
        return cipher.doFinal(byteData);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        keyGenerator.init(128, sr);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private static String toHex(byte[] buf) {
        final String hex = "0123456789ABCDEF";
        if (buf == null) {
            return "";
        }

        StringBuilder result = new StringBuilder(2 * buf.length);
        for (byte aBuf : buf) {
            result.append(hex.charAt((aBuf >> 4) & 0x0f)).append(
                    hex.charAt(aBuf & 0x0f));
        }
        return result.toString();
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * Creates a Provider and puts parameters
     */
    private static class CryptoProvider extends Provider {
        private CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}