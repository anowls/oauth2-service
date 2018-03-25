package org.anowls.spring.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 自定义redis的缓存时间
 * @author csj
 */
@Configuration
@Order(2)
public class TimeConfig {
    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;
    @Autowired
    private Environment environment;

    /**
     * 设置 tokenservice
     * @return  AuthorizationServerTokenServices
     */
    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setAccessTokenValiditySeconds(
            Integer.parseInt(environment.getProperty("token.access")));
        service.setRefreshTokenValiditySeconds(
            Integer.parseInt(environment.getProperty("token.refresh")));
        service.setReuseRefreshToken(false);
        service.setSupportRefreshToken(true);
        service.setTokenStore(new RedisTokenStore(lettuceConnectionFactory));
        return service;
    }
}