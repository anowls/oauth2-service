package org.anowls.spring.oauth2.config;

import org.anowls.spring.oauth2.interceptor.ActionInterceptor;
import org.anowls.spring.oauth2.security.SwanUserDetailsService;
import org.anowls.spring.oauth2.security.SwanWebResponseExceptionTranslator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


/**
 * OAuth2 Settings
 *
 * @author haopeisong
 */
@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class OAuth2AuthorizationConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TimeConfig timeConfig;

    @Autowired
    private SwanUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("browser")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("ui")
                .and()
                .withClient("swan-im-service")
                .secret("ABcd1234")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
        ;

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(new RedisTokenStore(lettuceConnectionFactory))
                .tokenServices(timeConfig.tokenService())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(new SwanWebResponseExceptionTranslator())
                //重写WebResponseExceptionTranslator对登录用户名跟密码异常进行了重新处理
                .addInterceptor(new ActionInterceptor());//对oauth2添加拦截器，不添加拦截器对oauth2 的方法无法拦截

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

}
