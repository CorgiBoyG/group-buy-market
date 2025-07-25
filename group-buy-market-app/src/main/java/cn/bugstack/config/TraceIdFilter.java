package cn.bugstack.config;


import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Program: group-buy-market
 * @Package: cn.bugstack.config
 * @Description: 用户每次前端的请求然后会到后端过各种程序节点，但有可能节点分布在不同机器，
 * 那么需要用ELK来看这种分布式日志，就是依赖日志产生时会生成的唯一的用户的trace-id
 * @Author: Daniel G
 * @Create: 2025-07-25 18:21:48
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    private static String TRACE_ID = "trace-id";//和logback-spring.xml中的trace-id对应

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = UUID.randomUUID().toString();//可以用雪花算法或者hutool代替
            MDC.put(TRACE_ID, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
