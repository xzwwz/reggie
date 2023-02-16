package com.zwh.filter;


import com.alibaba.fastjson.JSON;
import com.zwh.common.MyBaseContext;
import com.zwh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "logCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURi = request.getRequestURI();
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean match = check(uris, requestURi);
        if (match) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            MyBaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            MyBaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
//        log.info("拦截到请求 {}", request.getRequestURI());
//        filterChain.doFilter(request,response);
    }

    public boolean check(String[] uris, String requestUri) {
        for (String uri : uris) {
            if (PATH_MATCHER.match(uri, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
