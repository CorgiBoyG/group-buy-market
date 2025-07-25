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
    // OncePerRequestFilter ：确保每个请求只执行一次过滤

    private static String TRACE_ID = "trace-id";//和logback-spring.xml中的trace-id对应

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = UUID.randomUUID().toString();//可以用雪花算法或者hutool代替
            MDC.put(TRACE_ID, traceId); //为每个请求生成唯一的 trace-id.在logback-spring.xml中%X{trace-id} 会输出这个trace-id
            // 在业务代码中记录日志时，trace-id 会自动包含在日志中
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

/**
 * 分布式日志收集架构
 * ## 日志收集链路
 * - 1.TraceIdFilter 为每个请求生成唯一标识
 * - 2.Logback 格式化日志并通过 TCP 发送到 Logstash
 * - 3.Logstash 接收、处理并转发日志到 Elasticsearch
 * - 4.Elasticsearch 存储和索引日志数据
 * - 5.Kibana 提供日志查询和可视化界面
 * <p>
 * ## 请求处理流程
 * 客户端请求
 * ↓
 * Servlet 容器 (Tomcat)
 * ↓
 * Spring Boot 过滤器链
 * ↓
 * TraceIdFilter.doFilterInternal() ← 在这里！
 * ↓
 * 其他过滤器 (Security, CORS 等)
 * ↓
 * DispatcherServlet
 * ↓
 * Controller 方法
 * ## 网络连接架构
 * 应用程序 (Spring Boot)
 * ↓ TCP:4560 (LogstashTcpSocketAppender)
 * Logstash 容器
 * ↓ HTTP:9200 (es:9200)
 * Elasticsearch 容器
 * ↑ HTTP:9200 (elasticsearch:9200)
 * Kibana 容器
 */