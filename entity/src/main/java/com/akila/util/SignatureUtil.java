/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.javatuples.Pair;
import org.springframework.security.crypto.codec.Hex;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author minh
 */
public class SignatureUtil {

    private final static String HEX_STRING = "0123456789ABCDEF";

    static {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

    /**
     * Parse token with format <username>:<uuid>.
     *
     * @param token Auth token.
     * @return Pair of username and token UUID.
     */
    public static Pair<String, UUID> parseAuthToken(String token) {
        if (token == null) {
            return null;
        }

        var fragments = token.split(":");
        if (fragments.length != 2) {
            return null;
        }

        try {
            return new Pair<>(fragments[0], UUID.fromString(fragments[1]));
        } catch (IllegalArgumentException iae) {
            // invalid UUID
            return null;
        }
    }

    /**
     * Calculate key to store to session storage.
     */
    public static String calculateSessionStorageKey(String username, UUID uuid) throws NoSuchAlgorithmException, InvalidKeyException {
        // create token with format <username>:<uuid>
        var token = String.format("%s:%s", username, uuid);

        // sign with HMAC_SHA_256
        var mac = Mac.getInstance("HmacSHA256");

        // use UUID as the signing key to generate HMAC
        byte[] signingKey = new byte[32];
        // UUID is 128-bit but we need 256 bit for HmacSHA256 so write the bytes twice
        ByteBuffer.wrap(signingKey)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        var secretKeySpec = new SecretKeySpec(signingKey, "HmacSHA256");
        mac.init(secretKeySpec);

        // create message
        var hmacMessage = token.getBytes(StandardCharsets.US_ASCII);
        var hmacSha256 = mac.doFinal(hmacMessage);
        var hmacString = new String(Hex.encode(hmacSha256));
        return String.format("%s:%s", username, hmacString);
    }

    /**
     * @param input: Input text
     * @param strPk: The private key string
     * @return The signature
     */
    public static String signSHA256RSA(String input, String strPk) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        byte[] b1 = Base64.getDecoder().decode(strPk);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(kf.generatePrivate(spec));
        privateSignature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] signed = privateSignature.sign();
        //return Base64.getEncoder().encodeToString(bytesToHex(signed).getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signed);
        //Link: https://www.quickprogrammingtips.com/java/how-to-create-sha256-rsa-signature-using-java.html
    }

    public static String signSHA256AndRSA(String input, String strPk) throws Exception {

        byte[] b1 = Base64.getDecoder().decode(strPk);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] outputDigest = messageDigest.digest();
        //sign SHA256 with RSA
        Signature rsaSignature = Signature.getInstance("RSA");
        rsaSignature.initSign(kf.generatePrivate(spec));
        rsaSignature.update(outputDigest);
        byte[] signed = rsaSignature.sign();
        return Base64.getEncoder().encodeToString(bytesToHex(signed).getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = HEX_STRING.toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * @param xmlString: The string in xml format.
     * @return The document dom.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document getXmlDocument(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try (InputStream fis = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))) {
            return dbf.newDocumentBuilder().parse(fis);
        }
    }

    /**
     * @param doc: The document dom.
     * @param privateKeyResourcePath: The private key path in resources folder.
     * @param publicCertificateResourcePath: The private key path in resources
     * folder.
     * @throws IOException
     * @throws CertificateException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static String generateXMLDigitalSignature(Document doc,
            String privateKeyResourcePath, String publicCertificateResourcePath) throws IOException, CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, MarshalException, XMLSignatureException, TransformerException, InvalidAlgorithmParameterException {

        //Create XML Signature Factory
        XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance("DOM");
        PrivateKey privateKey = SecurityUtil.getStoredPrivateKeyFromResource(privateKeyResourcePath);
        DOMSignContext domSignCtx = new DOMSignContext(privateKey, doc.getDocumentElement());

        //Config transforms.
        List<Transform> transforms = Arrays.asList(
                xmlSigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
                xmlSigFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (TransformParameterSpec) null));
        Reference ref = xmlSigFactory.newReference("", xmlSigFactory.newDigestMethod(DigestMethod.SHA1, null), transforms, null, null);
        SignedInfo signedInfo = xmlSigFactory.newSignedInfo(
                xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                        (C14NMethodParameterSpec) null),
                xmlSigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref));

        //Pass the certificate
        KeyInfo keyInfo = getKeyInfoFromCertificate(xmlSigFactory, publicCertificateResourcePath);

        //Create a new XML Signature
        XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, keyInfo);
        xmlSignature.sign(domSignCtx);

        //Convert to string
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer trans = transFactory.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StreamResult streamRes = new StreamResult(new ByteArrayOutputStream());
        trans.transform(new DOMSource(doc), streamRes);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) streamRes.getOutputStream();
        return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8).replaceAll("&#13;", "");
    }

    /**
     * Method used to get the Cer KeyInfo
     *
     * @param xmlSigFactory
     * @param publicCertificateResourcePath: The private key path in resources
     * folder.
     * @return KeyInfo
     */
    private static KeyInfo getKeyInfoFromCertificate(XMLSignatureFactory xmlSigFactory, String publicCertificateResourcePath) throws IOException, CertificateException {
        KeyInfo keyInfo;
        X509Certificate x509Certificate = SecurityUtil.getCertificatePublicKeyFromResource(publicCertificateResourcePath);
        KeyInfoFactory keyInfoFact = xmlSigFactory.getKeyInfoFactory();
        List<Object> x509Content = new ArrayList<>();
        x509Content.add(x509Certificate.getSubjectX500Principal().getName());
        x509Content.add(x509Certificate);
        keyInfo = keyInfoFact.newKeyInfo(Collections.singletonList(keyInfoFact.newX509Data(x509Content)));
        return keyInfo;
    }
}
