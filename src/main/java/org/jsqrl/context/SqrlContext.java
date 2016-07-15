package org.jsqrl.context;

import org.jsqrl.config.SqrlConfig;
import org.jsqrl.controller.SqrlController;
import org.jsqrl.service.InMemoryAuthenticationService;
import org.jsqrl.service.InMemoryUserService;
import org.jsqrl.service.SqrlNutService;
import org.jsqrl.server.JSqrlServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@Configuration
public class SqrlContext {

    @Bean
    public SqrlConfig sqrlConfig(){
        SqrlConfig sqrlConfig = new SqrlConfig();
        sqrlConfig.setIpAddressRequired(true);
        sqrlConfig.setSqrlVersion("1");
        sqrlConfig.setSfn("jsqrl-example");
        sqrlConfig.setNutExpirationSeconds(200L);
        sqrlConfig.setSqrlBaseUri(SqrlController.SQRL_ROOT);
        return sqrlConfig;
    }

    @Bean
    public SqrlNutService sqrlNutService(SqrlConfig sqrlConfig, Key aesKey) throws NoSuchAlgorithmException {
        return new SqrlNutService(new SecureRandom(), sqrlConfig, MessageDigest.getInstance("SHA-256"), aesKey);
    }

    @Bean
    public JSqrlServer authService(InMemoryUserService userService,
                                   InMemoryAuthenticationService authenticationService,
                                   SqrlNutService nutService,
                                   SqrlConfig config){
        return new JSqrlServer(userService, authenticationService, config, nutService);
    }

    /**
     * Creates the in memory private AES key.
     * This key could also be loaded by a keystore.
     * @return AES Key
     * @throws NoSuchAlgorithmException
     */
    @Bean
    public Key aesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        return keyGen.generateKey();
    }

}
