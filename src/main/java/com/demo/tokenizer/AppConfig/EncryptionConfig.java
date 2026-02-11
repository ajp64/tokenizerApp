package com.demo.tokenizer.AppConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

@Configuration
public class EncryptionConfig
{
    @Value("${encrypt.password}")
    private String password;
    @Bean
    TextEncryptor textEncryptor() {
        String salt = KeyGenerators.string().generateKey();
        return Encryptors.text(password, salt);
    }

}
