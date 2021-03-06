/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edu.education.contract.autoconfig;

import org.fisco.bcos.channel.client.P12Manager;
import org.fisco.bcos.channel.client.PEMManager;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

@Configuration
@ConfigurationProperties(prefix = "accounts")
@ConditionalOnProperty(prefix = "contract.bsn",name = "using",havingValue = "false", matchIfMissing = true)

public class AccountConfig {

    private String pemFile;
    private String p12File;
    private String password;
    private String pubkey;
    private String prikey;
    private static final Logger log = LoggerFactory.getLogger(AccountConfig.class);
    @Autowired
    private EncryptType encryptType;

    @Bean
    public Credentials getCredentials()
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchProviderException, CertificateException,
            IOException {
        return loadAccount();
    }

    // load pub-pri key account
    private Credentials loadAccount()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        return Credentials.create(prikey,pubkey);
    }

    // load pem account file
    private Credentials loadPemAccount()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        log.info("pem accounts : {}", pemFile);
        PEMManager pem = new PEMManager();
        pem.setPemFile("classpath:" + pemFile);
        pem.load();
        ECKeyPair keyPair = pem.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));
        System.out.println(credentials.getAddress());
        return credentials;
    }

    // load p12 account file
    private Credentials loadP12Account()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        log.info("p12 accounts : {}", p12File);
        P12Manager p12Manager = new P12Manager();
        p12Manager.setP12File("classpath:" + p12File);
        p12Manager.setPassword(password);
        p12Manager.load();
        ECKeyPair keyPair = p12Manager.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));
        System.out.println(credentials.getAddress());
        return credentials;
    }

    /**
     * @return the pemFile
     */
    public String getPemFile() {
        return pemFile;
    }

    /**
     * @param pemFile the pemFile to set
     */
    public void setPemFile(String pemFile) {
        this.pemFile = pemFile;
    }

    /**
     * @return the p12File
     */
    public String getP12File() {
        return p12File;
    }

    /**
     * @param p12File the p12File to set
     */
    public void setP12File(String p12File) {
        this.p12File = p12File;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getPrikey() {
        return prikey;
    }

    public void setPrikey(String prikey) {
        this.prikey = prikey;
    }
}
