package org.anowls.spring.oauth2.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author haopeisong
 */
public class Md5PasswordEncoder extends DelegatingPasswordEncoder {

    private static String idForEncode = "MD5";
    private static Map<String, PasswordEncoder> encoders = new HashMap<>();

    static {
        encoders.put(idForEncode, new Md4PasswordEncoder());
    }

    public Md5PasswordEncoder() {
        super(idForEncode, encoders);
    }

}