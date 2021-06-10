package com.edu.education.helper.encryptHelper;

import org.apache.commons.codec.binary.Base64;
import org.fisco.bcos.web3j.crypto.tool.ECCDecrypt;
import org.fisco.bcos.web3j.crypto.tool.ECCEncrypt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class ECCEncryptHelper {
    private BigInteger prikey;
    private BigInteger pubkey;
    private ECCEncrypt eccEncrypt;
    private ECCDecrypt eccDecrypt;

    public ECCEncryptHelper(BigInteger prikey, BigInteger pubkey) {
        this.prikey = prikey;
        this.pubkey = pubkey;
        eccEncrypt = new ECCEncrypt(pubkey);
        eccDecrypt = new ECCDecrypt(prikey);
    }

    public ECCEncryptHelper(String prikey, String pubkey) {
        this.prikey = new BigInteger(prikey);
        this.pubkey = new BigInteger(pubkey);
        eccEncrypt = new ECCEncrypt(this.pubkey);
        eccDecrypt = new ECCDecrypt(this.prikey);
    }

    public ECCEncryptHelper(){}

    public String Encrypt(String content) {
        try {
            byte[] cipher = eccEncrypt.encrypt(content.getBytes());
            String cipher_base64 = Base64.encodeBase64String(cipher);
            return cipher_base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Decrypt(String cipher){
        try {
            byte[] result_byte = eccDecrypt.decrypt(Base64.decodeBase64(cipher));
            String result = new String(result_byte, StandardCharsets.UTF_8);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Encrypt(BigInteger pubkey,String content){
        try {
            ECCEncrypt eccencrypt = new ECCEncrypt(pubkey);
            byte[] cipher = eccencrypt.encrypt(content.getBytes());
            String cipher_base64 = Base64.encodeBase64String(cipher);
            return cipher_base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Decrypt(BigInteger prikey,String cipher){
        try {
            ECCDecrypt eccdecrypt = new ECCDecrypt(prikey);
            byte[] result_byte = eccdecrypt.decrypt(Base64.decodeBase64(cipher));
            String result = new String(result_byte, StandardCharsets.UTF_8);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
