package com.study.common.filter;

import cn.hutool.core.util.StrUtil;
import com.study.common.utils.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * MDC日志拦截器
 *
 * @author hecong
 * <p>
 * 2023-08-07 18：:1:00
 */
@Component
public class Log4jMDCFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = StrUtil.EMPTY;
        try {
            traceId = request.getHeader(TraceIdUtil.TRACE_ID);
            logger.info("traceId:{}", traceId);
            if (StrUtil.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            TraceIdUtil.setTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
        return;
    }

}
