package com.mis.backend.interceptor;

import com.mis.backend.anootation.NoAuth;
import com.mis.backend.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("RoleInterceptor preHandle");
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 如果方法或类上有 @NoAuth，直接放行
            if (handlerMethod.getMethodAnnotation(NoAuth.class) != null ||
                    handlerMethod.getBeanType().getAnnotation(NoAuth.class) != null) {
                return true;
            }
        }
        Integer type = BaseContext.getCurrentType();  // 从 ThreadLocal 获取
        if (type == null || type != 1) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("No access");
            response.setStatus(403);
            return false;
        }
        return true;
    }
}
