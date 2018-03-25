package org.anowls.spring.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 认证服务-程序入口
 *
 * @author haopeisong
 */
@SpringBootApplication(scanBasePackages = "org.anowls.spring")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ServletComponentScan
public class OAuth2Application {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2Application.class, args);
    }
}
