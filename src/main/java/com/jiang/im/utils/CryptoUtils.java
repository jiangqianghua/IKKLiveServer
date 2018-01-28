package com.jiang.im.utils;

import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
        import java.security.interfaces.RSAPublicKey;
        import java.security.spec.PKCS8EncodedKeySpec;
        import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
        import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class CryptoUtils {

    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9tBkKemwo+YJmAJKxD1r1bxnh2CepQ1EBX12ikAblZUuu/bqRQtcBbD30FOlLh75VrM89EjiAdwfsIjyPJyACpb5uINL2/hX5UNI7HB0812pHzCQf2an1expf2q//CvH142kDmhGV0wJEJc2oUM2jZM9tjvemzxIMFvw8Fh6O7wIDAQAB";
    public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL20GQp6bCj5gmYAkrEPWvVvGeHYJ6lDUQFfXaKQBuVlS679upFC1wFsPfQU6UuHvlWszz0SOIB3B+wiPI8nIAKlvm4g0vb+FflQ0jscHTzXakfMJB/ZqfV7Gl/ar/8K8fXjaQOaEZXTAkQlzahQzaNkz22O96bPEgwW/DwWHo7vAgMBAAECgYB8xxBNfxbToMJvm2LhiasXkrtgfNAhmoCWuYaaXcfbbVRpxsTHtUI80lixymtlXeUI74Dkt65oeWLBpAdXVbHMShNzYnd8lZgZ0o3q547yauuU6/Qm0TkfSs3AJQnAhvFNPWaCioYTuB7Qd5I8Webvi1Iq5ENOur+9BV2w7iHOOQJBAN6TvRGJu4bi1SjTOEla+xKsewF8WRyH84e1mUUATv/9IcIg7rleE1xmWZ2kBYhz7ZqrLgA25P5TsrKnf+tTzTMCQQDaMKO1wTxKZmbWj2Vrvaqa+XYQwWc5ayVN6UZMRcX57NcJKnu98DIkHgAHGTEpkkSK9hvyUfhx/RfQ6P/GRN9VAkAeG3vqwGfGci6FTDUD7zOhLgWhiNGBA37Ur6Lg647EH9iOKug/2NLjn9Cle0doUs4kKt2ZF9PX9zhn5DJwgbBzAkB8jDtHKxDhX2bgn2ZDhDb40+GjWuMzkYfv23M3YrTKH3n4T3nnSE828rENXEIF+uqJcEcvob7yQojvxbELWpEpAkAIAskJJT3R9RKUgHi1fXKALPeIu/lOGam8EbmMfqtjV/ynCO4PdQtTJp8SUZ+jYJOR9HAfOd9S++BFiijjRHJw";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String TIMESTAMP = "timestamp";
    public static final String MAC = "mac";
    public static final String RANDOM = "random";
    public static final String SALT = "salt";
    public static final String[] keys = {ACCOUNT,PASSWORD,TIMESTAMP,MAC,RANDOM};
    public static final String[] tokenKeys = {SALT,ACCOUNT,MAC,TIMESTAMP};
    public static final String charsetName = "ISO-8859-1" ;
    static {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
//            Map<String, Object> keyMap = RSAUtils.genKeyPair();
//            publicKey = RSAUtils.getPublicKey(keyMap);
//            privateKey = RSAUtils.getPrivateKey(keyMap);
//            System.err.println("公钥: \n\r" + publicKey);
//            System.err.println("私钥： \n\r" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

//    public static void main(String[] args) throws  Exception{
//        String source = "1|1|1517022979|H60-L11|-999139940";
//        System.out.println("\r加密前文字：\r\n" + source);
//        byte[] data = source.getBytes("ISO-8859-1");
//        byte[] encodedData = encryptByPublicKey(data, publicKey);
//        String encodeStr = new String(encodedData,charsetName);
//        System.out.println("加密后文字：\r\n" + encodeStr);
//        byte[] newbyte = encodeStr.getBytes("ISO-8859-1");
//        byte[] decodedData = decryptByPrivateKey(newbyte, privateKey);
//        String target = new String(decodedData);
//        System.out.println("解密后文字: \r\n" + target);
//    }
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] decryptedData = new byte[encryptedData.length];
        for(int i = 0 ; i < encryptedData.length ; i++){
            decryptedData[i] = (byte)(encryptedData[i] - (byte)1);
        }
        return decryptedData;
        /**
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
         **/
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] decryptedData = new byte[data.length];
        for(int i = 0 ; i < data.length ; i++){
            decryptedData[i] = (byte)(data[i] + (byte)1);
        }
        return decryptedData;
        /**
        byte[] decryptedData = new byte[data.length];
        for(int i = 0 ; i < data.length ; i++){
            decryptedData[i] = (byte)(data[i] + (byte)1);
        }
        return decryptedData;
        /**
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
         **/
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {

        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 解析登陆信息
     * @param token
     * @return
     */
    public static Map<String,String> parseLoginInfo(String token)throws Exception{
        if(StringUtils.isEmpty(token))
            return null ;
        byte[] tokenBytes = token.getBytes(CryptoUtils.charsetName) ;
        byte[] decodedData = CryptoUtils.decryptByPrivateKey(tokenBytes, privateKey);
        String loginInfo = new String(decodedData,charsetName);
        String[] values = loginInfo.split("&");
        if(values == null || values.length != 5){
            throw new Exception("登陆数据格式错误");
        }
        Map<String,String> map = new HashMap<String,String>();
        for(int i = 0 ; i < keys.length ; i++){
            map.put(keys[i],values[i]);
        }
        return map ;
    }

    /**
     * 获取盐值
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getEncryptCient(String salt,String account,String mac)throws Exception{
        Long timestamp = System.currentTimeMillis()/1000;
        String format = "%s&%s&%s&%s";
        String formatStr = String.format(format,salt,account,mac,timestamp+"");
        return new String(encryptByPublicKey(formatStr.getBytes(charsetName),publicKey),charsetName);
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public static Map<String,String> decodeToken(String token) throws Exception{
        byte[] decodedData = decryptByPrivateKey(token.getBytes(charsetName), privateKey);
        String tokenStr = new String(decodedData,charsetName);
        //"盐值｜用户名｜mac｜时间"
        String[] values = tokenStr.split("&");
        if(values == null || values.length != 4){
            throw new Exception("解析token错误");
        }
        Map<String,String> map = new HashMap<String,String>();
        for(int i = 0 ; i < values.length ; i++){
            map.put(tokenKeys[i],values[i]);
        }
        return map ;
    }

}