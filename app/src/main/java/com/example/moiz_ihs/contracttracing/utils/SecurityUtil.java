package com.example.moiz_ihs.contracttracing.utils;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Moiz-IHS on 6/15/2017.
 */

public class SecurityUtil {

    /**
     * The Constant ALGORITHM.
     */
    private static final String ALGORITHM = "AES";

    /**
     * The Constant ITERATIONS.
     */
    private static final int ITERATIONS = 2;

    /**
     * The Constant keyValue.
     */
    private static final byte[] keyValue = new byte[]{'L', 'o', 'v', 'e', 'Y', 'o', 'u', 'r', 'P', 'a', 'k', 'i', 's', 't', 'a', 'n'};

    public static String encrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        String valueToEnc = null;
        String eValue = value;
        for (int i = 0; i < ITERATIONS; i++) {
            valueToEnc = salt + eValue;
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            eValue = Base64.encodeToString(encValue, Base64.DEFAULT);
        }
        return eValue;
    }

    public static String decrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        String dValue = null;
        String valueToDecrypt = value;
        for (int i = 0; i < ITERATIONS; i++) {
            byte[] decordedValue = Base64.decode(valueToDecrypt, Base64.DEFAULT);
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(salt.length());
            valueToDecrypt = dValue;
        }
        return dValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        // SecretKeyFactory keyFactory =
        // SecretKeyFactory.getInstance(ALGORITHM);
        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
        return key;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(encrypt("123456", "administrator"));
            //System.out.println();
            //System.out.println(decrypt("e/KpSN8ZM2zDlE2ySXn3JGtWusuZNLonqjDu4HUaDks=", "humap"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}