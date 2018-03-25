package org.anowls.spring.oauth2.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户控制层
 *
 * @author NiBo
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private Environment environment;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取用户信息
     *
     * @param principal //用户信息类
     * @return Principal
     */
    @RequestMapping(value = "current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        logger.info("用户名:{}", principal.getName());
        return principal;
    }

}
