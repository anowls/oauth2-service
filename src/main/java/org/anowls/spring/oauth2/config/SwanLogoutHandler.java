package org.anowls.spring.oauth2.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;


/**
 * 登出拦截
 *
 * @author keets
 */
public class SwanLogoutHandler implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwanLogoutHandler.class);


    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(lettuceConnectionFactory);
        Assert.notNull(redisTokenStore, "tokenStore must be set");
        String token = request.getHeader("Authorization");
        Assert.hasText(token, "token must be set");

        token = token.substring(6).replace(" ", "");
        OAuth2AccessToken existingAccessToken = redisTokenStore.readAccessToken(token);
        OAuth2RefreshToken refreshToken;
        if (existingAccessToken != null) {
            if (existingAccessToken.getRefreshToken() != null) {
                LOGGER.info("remove refreshToken!", existingAccessToken.getRefreshToken());
                refreshToken = existingAccessToken.getRefreshToken();
                redisTokenStore.removeRefreshToken(refreshToken);
            }
            LOGGER.info("remove existingAccessToken!", existingAccessToken);
            redisTokenStore.removeAccessToken(existingAccessToken);
        }
        LOGGER.info("登出成功!{}", token);
    }


}
