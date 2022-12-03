package org.home.knowledge.sow.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Username logging interceptor allows for us to add usernames to logs
 * <p>
 * We are adding/removing username to logging MDC (Mapped Diagnostic Context
 * which is fancy
 * talk for we'll add something to the thread.local so that it stays in scope
 * the entire time the thread is in used) so that when we log we have the
 * current user's username added to the log
 */
@Component
public class UsernameLoggingInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        MDC.remove("username");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            MDC.put("username", username);
        }
        return true;
    }

}
