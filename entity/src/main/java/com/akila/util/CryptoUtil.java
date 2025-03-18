package com.akila.util;

import com.akila.type.CryptoType;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    private final static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANNUDN6sbEhf31e/1THYHs6rXS4cMvOgG0ADgT5xuuFICXRywKlCih0ik3Lrhf8mI8ql1GrcdZa2/8hr3zad39p7uxNG7Xux4c072D78N/TEzr8OAxIMJBxkIJBdq724DSrzUGLK4FyYmV7iO76sac45OHFHw0leo3IfgYPwz5tNAgMBAAECgYEAtU7AN+8Qe8udT6iq3bfBPqjxKdVRtYlgt7wKD6v9nLh0PC8TsmP/IN4ASTFeZjivrnY113TLggIBU0+RU0eBvF5vNozfs0jr17eGVfO9d2PcilmmMm2cxS+/Zmms7HsVrbrp1fBIE72Mr5/sXRkfCV3oKREUprk+V8uxPNoFIkECQQD2j91x8c3NjIHQF0RlIwVZxn0ErVtOvjaQbi6H6NHkXlrAq1DU00SCWRGVA50j0Fr8DlJYgnNDYmBpD5stnszRAkEA22rri5aLGY6N8CwYME49BHjpRCufCbWiiBnjgy6/oGgUQe0y85HYUwENH+0kt+1MzrrS03Q/2YgePzF+78FVvQJACnOKtXvEd4QPNJzn30mevnF1dy9KAYp6kaC6BDTQQNfnDOe2I29ZJhVUc3aVwKDiWnDY+Lt+20peP9XThBmJIQJAOLjFN4qtXuf5TXM4tZQkNfnD47mbHXl1ENYQeKMnKqJ9SiW1nD3BvINzDyPQ8DNKPc8SHVPaBbQojZGQDQ7fyQJBANAXO+7fCC8Y0FnEIrz83D0JpmzKej5I0XwzHY+TewLc76aMxJ5ouqOUhpTS+dSnvgr8QIv55nQRreQfUfeyM5s=";
    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTVAzerGxIX99Xv9Ux2B7Oq10uHDLzoBtAA4E+cbrhSAl0csCpQoodIpNy64X/JiPKpdRq3HWWtv/Ia982nd/ae7sTRu17seHNO9g+/Df0xM6/DgMSDCQcZCCQXau9uA0q81BiyuBcmJle4ju+rGnOOThxR8NJXqNyH4GD8M+bTQIDAQAB";
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String encryptCustomize(String data, int randomNumberCipher, int randomNumberPos) {
        var rsaEncrypt = publicRsaEncrypt(data, publicKey);
        Random r = new Random();
        // int randomNumberCipher = r.ints(1, 1, 26).findFirst().getAsInt();
        var cipherEncrypt = encryptCaesarData(rsaEncrypt, randomNumberCipher);
        //int randomNumberPos = r.ints(1, 26, 99).findFirst().getAsInt();
        var posEncrypt = changePos(cipherEncrypt, randomNumberPos);
        StringBuilder sb = new StringBuilder();
        return sb.append("Avalonia").append(randomNumberCipher).append(randomNumberPos).append(posEncrypt).toString();
    }

    public static String encryptCaesarData(String inputStr, int shiftKey) {
        // convert inputStr into lower case   
        //inputStr = inputStr.toLowerCase();

        // encryptStr to store encrypted data   
        String encryptStr = "";

        // use for loop for traversing each character of the input string   
        for (int i = 0; i < inputStr.length(); i++) {
            // get position of each character of inputStr in ALPHABET   
            int pos = ALPHABET.indexOf(inputStr.charAt(i));
            if (pos < 0) {
                encryptStr += inputStr.charAt(i);
                continue;
            }

            // get encrypted char for each char of inputStr   
            int encryptPos = (shiftKey + pos) % 62;
            char encryptChar = ALPHABET.charAt(encryptPos);

            // add encrypted char to encrypted string   
            encryptStr += encryptChar;
        }

        // return encrypted string   
        return encryptStr;
    }

    // create decryptData() method for decrypting user input string with given shift key   
    public static String decryptCaesarData(String inputStr, int shiftKey) {
        // convert inputStr into lower case   
        //inputStr = inputStr.toLowerCase();

        // decryptStr to store decrypted data   
        String decryptStr = "";

        // use for loop for traversing each character of the input string   
        for (int i = 0; i < inputStr.length(); i++) {

            // get position of each character of inputStr in ALPHABET   
            int pos = ALPHABET.indexOf(inputStr.charAt(i));
            if (pos < 0) {
                decryptStr += inputStr.charAt(i);
                continue;
            }

            // get decrypted char for each char of inputStr   
            int decryptPos = (pos - shiftKey) % 62;

            // if decryptPos is negative   
            if (decryptPos < 0) {
                decryptPos = ALPHABET.length() + decryptPos;
            }
            char decryptChar = ALPHABET.charAt(decryptPos);

            // add decrypted char to decrypted string   
            decryptStr += decryptChar;
        }
        // return decrypted string   
        return decryptStr;
    }

    public static String deEncryptCustomize(String data, String device) {
        if (!device.startsWith("Avalonia")) {
            return null;
        }
        int randomNumberCipher = Integer.parseInt(device.substring(8, 10));
        int randomNumberPos = Integer.parseInt(device.substring(10, 12));
        var posDeEncrypt = changePos(data, -randomNumberPos);
        var cipherDeEncrypt = decryptCaesarData(posDeEncrypt, randomNumberCipher);
        var rsaDeEncrypt = privateRsaDecrypt(cipherDeEncrypt, privateKey);
        return rsaDeEncrypt;
    }

    public static String caesarCipher(String data, int pos) {
        StringBuilder sb = new StringBuilder();
        for (var charA : data.toCharArray()) {
            int ascii = (int) charA;
            int changePos = ascii + pos;
            if (changePos > 126) {
                changePos = (changePos % 126) + 33;
            }
            if (changePos < 33) {
                changePos = changePos + 126 - 33;
            }
            sb.append((char) changePos);
        }
        return sb.toString();
    }

    public static String changePos(String data, int pos) {
        StringBuilder sb = new StringBuilder();
        if (pos < 0) {
            sb.append(data.substring(data.length() + pos, data.length())).append(data.substring(0, data.length() + pos));
        } else {
            sb.append(data.substring(pos, data.length())).append(data.substring(0, pos));
        }
        return sb.toString();
    }

    public static String calculateChecksum(String s) throws Exception {

        byte[] bytes = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(bytes);
        byte[] digest = md.digest();

        return bytesToHexString(digest);

    }

    public static String encrypt(String s) {
        try {
            return calculateChecksum(s);
        } catch (Exception e) {
            return "";
        }
    }

    public static String md5(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytes = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        byte[] digest = md.digest();

        return bytesToHexString(digest);

    }

    public static byte[] hexStringToBytes(String hex) {

        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int len = hex.length() / 2;
        byte[] bytes = new byte[len];

        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) | Character.digit(hex.charAt(i + 1), 16));
        }

        return bytes;
    }

    public static String bytesToHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < bytes.length; ++i) {
            sb.append(Integer.toHexString((bytes[i] & 0xFF) + 0x100).substring(1));
        }

        return sb.toString();
    }

    public static String sha256(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String privateRsaEncrypt(String data, String privateKey) {
        return RSACryptor.privateEncrypt(data, privateKey);
    }

    public static String publicRsaEncrypt(String data, String publicKey) {
        try {
            return RSACryptor.publicEncrypt(data, publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            Logger.getLogger(CryptoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static String desEncrypt(String dataEncrypted, String privateKey) {
        return TripleDESCryptor.encrypt(dataEncrypted, privateKey);
    }

    public static String privateEncryptData(String data, String privateKey, CryptoType encryptType) {
        switch (encryptType) {
            case RSA:
                return privateRsaEncrypt(data, privateKey);
            case DES:
                return desEncrypt(data, privateKey);
            default:
                return privateRsaEncrypt(data, privateKey);
        }
    }

    public static String publicEncryptData(String data, String publicKey, CryptoType encryptType) {
        switch (encryptType) {
            case RSA:
                return publicRsaEncrypt(data, publicKey);
            case DES:
                return desEncrypt(data, publicKey);
            default:
                return publicRsaEncrypt(data, publicKey);
        }
    }

    public static String privateRsaDecrypt(String data, String privateKey) {
        return RSACryptor.privateDecrypt(data, privateKey);
    }

    public static String publicRsaDecrypt(String data, String publicKey) {
        return RSACryptor.public_decrypt(data, publicKey);
    }

    public static String desDecrypt(String dataEncrypted, String privateKey) {
        return TripleDESCryptor.decrypt(dataEncrypted, privateKey);
    }

    public static String privateDecryptData(String data, String privateKey, CryptoType encryptType) {
        switch (encryptType) {
            case RSA:
                return privateRsaDecrypt(data, privateKey);
            case DES:
                return desDecrypt(data, privateKey);
            default:
                return privateRsaDecrypt(data, privateKey);
        }
    }

    public static String publicDecryptData(String data, String publicKey, CryptoType encryptType) {
        switch (encryptType) {
            case RSA:
                return publicRsaDecrypt(data, publicKey);
            case DES:
                return desDecrypt(data, publicKey);
            default:
                return publicRsaDecrypt(data, publicKey);
        }
    }

    //@TODO: Bo nhe. Su dung checksum util 
    public static String createChecksum(String data, String key) {
        try {
            String check = "" + data + key;
            return CryptoUtil.md5(check);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
        }
        return "";
    }
}

class RSACryptor {

    private static byte[] append(byte[] array1, byte[] array2) {

        int len1 = array1.length;
        int len2 = array2.length;
        byte[] result = new byte[len1 + len2];

        for (int i = 0; i < len1; ++i) {
            result[i] = array1[i];
        }

        for (int i = 0; i < len2; ++i) {
            result[i + len1] = array2[i];
        }

        return result;
    }

    public static LinkedHashMap<String, String> generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        KeyPair key = keyGen.generateKeyPair();

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("public_key", Base64.getEncoder().encodeToString(key.getPublic().getEncoded()));
        map.put("private_key", Base64.getEncoder().encodeToString(key.getPrivate().getEncoded()));

        return map;
    }

    public static String publicEncrypt(String plainText, String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        if (plainText == null) {
            return null;
        }

        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPublicKey key = (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainTextBytes = plainText.getBytes("UTF-8");
        byte[] cipherBytes = new byte[0];

        int length = plainTextBytes.length;
        int offset = 0;
        while (offset < length) {

            int block_size;
            if (length - offset < 117) {
                block_size = length - offset;
            } else {
                block_size = 117;
            }

            byte[] block = new byte[block_size];
            for (int i = 0; i < block_size; ++i) {
                block[i] = plainTextBytes[offset + i];
            }

            byte[] cipherBlock = cipher.doFinal(block);
            cipherBytes = append(cipherBytes, cipherBlock);

            offset += block_size;
        }

        String cipherText = Base64.getEncoder().encodeToString(cipherBytes);

        return cipherText;

    }

    public static String privateEncrypt(String plainText, String privateKey) {
        try {
            if (plainText == null) {
                return null;
            }

            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPrivateKey key = (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = plainText.getBytes("UTF-8");
            byte[] cipherBytes = new byte[0];

            int length = plainTextBytes.length;
            int offset = 0;
            while (offset < length) {

                int block_size;
                if (length - offset < 117) {
                    block_size = length - offset;
                } else {
                    block_size = 117;
                }

                byte[] block = new byte[block_size];
                for (int i = 0; i < block_size; ++i) {
                    block[i] = plainTextBytes[offset + i];
                }

                byte[] cipherBlock = cipher.doFinal(block);
                cipherBytes = append(cipherBytes, cipherBlock);

                offset += block_size;
            }

            String cipherText = Base64.getEncoder().encodeToString(cipherBytes);

            return cipherText;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException ex) {
            return null;
        }
    }

    //// ThanhBL edit 2016-07-27
    public static String privateDecrypt(String cipherText, String privateKey) {

        try {
            if (cipherText == null) {
                return null;
            }

            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPrivateKey key = (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] plainTextBytes = new byte[0];

            int length = cipherBytes.length;
            int offset = 0;
            while (offset < length) {

                int block_size;
                if (length - offset < 128) {
                    block_size = length - offset;
                } else {
                    block_size = 128;
                }

                byte[] block = new byte[block_size];
                for (int i = 0; i < block_size; ++i) {
                    block[i] = cipherBytes[offset + i];
                }

                byte[] plainTextBlock = cipher.doFinal(block);
                plainTextBytes = append(plainTextBytes, plainTextBlock);

                offset += block_size;
            }

            return new String(plainTextBytes, "UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeySpecException ex) {
            return null;
        }
    }

    public static String public_decrypt(String cipherText, String publicKey) {
        try {
            if (cipherText == null) {
                return null;
            }

            byte[] keyBytes = Base64.getDecoder().decode(publicKey);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey key = (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(keyBytes));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] plainTextBytes = new byte[0];

            int length = cipherBytes.length;
            int offset = 0;
            while (offset < length) {

                int block_size;
                if (length - offset < 128) {
                    block_size = length - offset;
                } else {
                    block_size = 128;
                }

                byte[] block = new byte[block_size];
                for (int i = 0; i < block_size; ++i) {
                    block[i] = cipherBytes[offset + i];
                }

                byte[] plainTextBlock = cipher.doFinal(block);
                plainTextBytes = append(plainTextBytes, plainTextBlock);

                offset += block_size;
            }

            return new String(plainTextBytes, "UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeySpecException ex) {
            return null;
        }
    }
}

class TripleDESCryptor {

    public static String generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encrypt(String data, String key) {
        try {
            if (data == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("TripleDES");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes(), 0, key.length());
            String keymd5 = new BigInteger(1, md5.digest()).toString(16).substring(0, 24);
            SecretKeySpec keyspec = new SecretKeySpec(keymd5.getBytes(), "TripleDES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] plainTextBytes = data.getBytes("UTF-8");
            byte[] cipherBytes = cipher.doFinal(plainTextBytes);
            String cipherText = Base64.getEncoder().encodeToString(cipherBytes);
            return cipherText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException ex) {
            return null;
        }
    }

    public static String decrypt(String data, String key) {

        try {
            if (data == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("TripleDES");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes(), 0, key.length());
            String keymd5 = new BigInteger(1, md5.digest()).toString(16).substring(0, 24);
            SecretKeySpec keyspec = new SecretKeySpec(keymd5.getBytes(), "TripleDES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec);
            byte[] cipherBytes = Base64.getDecoder().decode(data);
            byte[] plainTextBytes = cipher.doFinal(cipherBytes);

            return new String(plainTextBytes, "UTF-8");
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            return null;
        }
    }
}
