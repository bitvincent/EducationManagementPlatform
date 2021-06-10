package com.edu.education.contract.autoconfig;

import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "encrypt-type")
@ConditionalOnProperty(prefix = "contract.bsn",name = "using",havingValue = "false", matchIfMissing = true)

public class EncryptTypeConfig {

    private int encryptType;

    @Bean
    public EncryptType getEncryptType() {
        return new EncryptType(encryptType);
    }

    /**
     * @param encryptType the encryptType to set
     */
    public void setEncryptType(int encryptType) {
        this.encryptType = encryptType;
    }
}
