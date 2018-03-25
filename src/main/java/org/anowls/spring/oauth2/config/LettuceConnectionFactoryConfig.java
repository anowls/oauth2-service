package org.anowls.spring.oauth2.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;

/**
 * Redis 连接工厂（单机/集群）配置
 *
 * @author haopeisong
 */
@Configuration
@Order(1)
public class LettuceConnectionFactoryConfig {

    @Autowired
    private Environment environment;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        String hostName = environment.getProperty("spring.redis.host");
        RedisPassword redisPassword = RedisPassword.of(environment.getProperty("spring.redis.password"));
        if (!StringUtils.isEmpty(hostName)) {
            Integer port = environment.getProperty("spring.redis.port", Integer.class);
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
            redisStandaloneConfiguration.setPassword(redisPassword);
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        }
        List<String> nodes = Arrays.asList(environment.getProperty("spring.redis.cluster.nodes").split(","));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(nodes);
        redisClusterConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }
}
