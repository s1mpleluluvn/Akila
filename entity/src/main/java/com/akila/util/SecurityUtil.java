/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 *
 * @author minh
 */
public class SecurityUtil {

    /**
     * Method used to get the generated Private Key
     *
     * @param privateKeyResourcePath: The private key path in resources folder.
     * @return PrivateKey
     */
    public static PrivateKey getStoredPrivateKeyFromResource(String privateKeyResourcePath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        try (InputStream inputStream = SecurityUtil.class.getResourceAsStream(privateKeyResourcePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String fileText = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            fileText = fileText.substring(fileText.indexOf("\n"), fileText.lastIndexOf("\n"))
                    .replaceAll("\n", "").replaceAll("\r", "");
            PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(fileText));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(encodedPrivateKey);
        }

    }

    /**
     * Method used to get the generated X509Certificate
     *
     * @param publicCertificateResourcePath: The certificate path in resources
     * folder.
     * @return X509Certificate
     */
    public static X509Certificate getCertificatePublicKeyFromResource(String publicCertificateResourcePath) throws CertificateException, IOException {

        try (InputStream inputStream = SecurityUtil.class.getResourceAsStream(publicCertificateResourcePath);
                BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(bis);
            return (X509Certificate) cert;

        }

    }

    /**
     * Method used to get the generated Private Key
     *
     * @param inputStream: The private key input stream.
     * @return PrivateKey
     */
    public static PrivateKey getStoredPrivateKeyFromInputSteam(InputStream inputStream) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String fileText = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            fileText = fileText.substring(fileText.indexOf("\n"), fileText.lastIndexOf("\n"))
                    .replaceAll("\n", "").replaceAll("\r", "");
            PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(fileText));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(encodedPrivateKey);
        }

    }

    /**
     * Method used to get the generated X509Certificate.
     *
     * @param inputStream: The certificate input stream.
     * @return X509Certificate
     */
    public static X509Certificate getCertificatePublicKeyFromInputSteam(InputStream inputStream) throws CertificateException, IOException {

        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(bis);
            return (X509Certificate) cert;

        }

    }
}
