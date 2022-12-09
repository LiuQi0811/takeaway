package com.code.takeaway.filter;

import com.alibaba.fastjson.JSON;
import com.code.takeaway.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/28 15:14
 * 拦截器
 * 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取本次请求的网络URI地址
        String requestURI = request.getRequestURI(); // /backend/index.html
        log.info("拦截的请求{} " + requestURI);
        //定义不需要拦截处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/**",
                "/category/**",
                "/mq/**",
                "/test/**",
                "/employee/logout",
                "/backend/**"
        };
        // 判断本次请求是否需要拦截处理
        boolean check = check(urls, requestURI);
        if (check) { // 不需要拦截处理
            log.info("本次请求{}不需要拦截处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getSession().getAttribute("employee").equals(null)) { // 不需要拦截处理
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        // 如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {

        // 遍历  允许通过的url
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
