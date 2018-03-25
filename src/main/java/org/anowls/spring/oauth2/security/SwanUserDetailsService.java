package org.anowls.spring.oauth2.security;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.anowls.spring.oauth2.domain.SwanUserDetails;
import org.anowls.spring.oauth2.mapper.AuthMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * 用户信息查询
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @author haopeisong
 */
@Service
public class SwanUserDetailsService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(SwanUserDetailsService.class);

    @Autowired
    private AuthMapper authMapper;

    @Override
    public SwanUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("username: {}", username);
        SwanUserDetails userDetails = authMapper.findUserByName(username);
        if (Objects.isNull(userDetails)) {
            throw new UsernameNotFoundException("The user name you entered does not exist!");
        }
        List<String> permissions = authMapper.findAuthByUser(userDetails.getId());
        if (!CollectionUtils.isEmpty(permissions)) {
            List<SimpleGrantedAuthority> authorities = permissions.stream().map(o -> new SimpleGrantedAuthority(o)).collect(Collectors.toList());
            userDetails.setAuthorities(authorities);
        }
        return userDetails;
    }
}
