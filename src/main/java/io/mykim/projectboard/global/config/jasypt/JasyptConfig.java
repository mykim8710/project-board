package io.mykim.projectboard.global.config.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
    @Value("${jasypt.encryptor.secret-key}")
    private String secretKey;

    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;

    @Value("${jasypt.encryptor.pool-size}")
    private String poolSize;

    @Value("${jasypt.encryptor.key-obtention-iterations}")
    private String keyObtentionIterations;

    @Bean
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(secretKey); // 암호화할 때 사용하는 키
        config.setAlgorithm(algorithm);                                      // 암호화 알고리즘
        config.setKeyObtentionIterations(keyObtentionIterations);            // 반복할 해싱 회수
        config.setPoolSize(poolSize);                                        // 인스턴스 pool
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }
}
