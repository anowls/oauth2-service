package org.anowls.spring.oauth2.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ActionInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionInterceptor.class);

    ObjectMapper mapper = new ObjectMapper();

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    public ActionInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HashMap hashMap = new HashMap();
        Enumeration<String> en = request.getHeaderNames();
        String deviceType = null;
        String request_server = null;
        String parent_spanId = null;

        while (en.hasMoreElements()) {
            String key = en.nextElement();
            if (!StringUtils.isEmpty(key)) {
                String head = request.getHeader(key);
                if (key.equals("user-agent")) {
                    deviceType = this.deviceType(head);
                }

                if (key.equals("request_server")) {
                    request_server = head;
                }

                if (key.equals("x-b3-parentspanid")) {
                    parent_spanId = head;
                }

                hashMap.put(key, head);
            }
        }

        this.startTime.set(System.currentTimeMillis());
        MDC.put("http_uri", request.getRequestURL().toString());
        MDC.put("client_ip", request.getRemoteAddr());
        MDC.put("http_method", request.getMethod());
        MDC.put("request_server", request_server);
        MDC.put("parent_spanId", parent_spanId);
        Map map = new HashMap<>(request.getParameterMap());
        if (Objects.nonNull(map.get("password"))) {
            map.remove("password");
        }

        MDC.put("param_data", this.mapper.writeValueAsString(map));
        MDC.put("api_method", ((HandlerMethod) handler).getMethod().getName());
        MDC.put("device_type", deviceType);
        MDC.put("request_header", this.mapper.writeValueAsString(hashMap));
        MDC.put("status_code", null);
        MDC.put("time_consume", null);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String time = System.currentTimeMillis() - this.startTime.get() + "ms";
        MDC.put("status_code", String.valueOf(response.getStatus()));
        MDC.put("time_consume", time);
        LOGGER.info("请求：{} 共耗时：{}", request.getRequestURL().toString(), time);
    }

    private String deviceType(String headType) {
        if (headType.contains("Android")) {
            return "Android端";
        }
        if (headType.contains("iPhone") || headType.contains("iOS")) {
            return "iPhone端";
        }
        return headType.contains("Windows") ? "PC端" : "未知设备";
    }
}

