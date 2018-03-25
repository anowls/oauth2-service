package org.anowls.spring.oauth2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 身份认证异常处理
 *
 * @author haopeisong
 */
public class SwanWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private static final Logger logger = LoggerFactory.getLogger(SwanWebResponseExceptionTranslator.class);

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        OAuth2Exception oAuth2Exception = new OAuth2Exception("出错了！！");
        oAuth2Exception.addAdditionalInformation("code", "1");
        oAuth2Exception.addAdditionalInformation("data", "");
        oAuth2Exception.addAdditionalInformation("message", "用户名或密码错误!");
        oAuth2Exception.addAdditionalInformation("level", "warn");
        oAuth2Exception.addAdditionalInformation("showHits", "");
        logger.error("用户密码错误!", e);
        return new ResponseEntity<>(oAuth2Exception, HttpStatus.OK);
    }

}

