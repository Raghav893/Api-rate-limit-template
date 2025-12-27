package com.raghav.apiratelimittemplate.Filters;

import com.raghav.apiratelimittemplate.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private  final ProxyManager proxyManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();//To get user current path
        String ip = request.getRemoteAddr();//To get users current ip
        String method =request.getMethod();//TO get current HTTP method

        BucketConfiguration configuration;

        if (path.equals("/auth/login")&& method.equals("POST")){
            configuration = RateLimitConfig.loginLimiter();//to set per api filter config  to each path
        }
         else if (path.equals("/payments/create")) {
        configuration = RateLimitConfig.PaymentLimiter();
        }
         else {
        configuration = null;
         }
        if (configuration == null){
            filterChain.doFilter(request,response);
            return;
        }

        String key =ip +":"+method+ ":" +path;
        Bucket bucket = proxyManager.getProxy(key,()->configuration);//It injects key and configuration in proxyManager

        if (bucket.tryConsume(1)){
            filterChain.doFilter(request,response);
        }
        else {
            response.setStatus(429);
            response.getWriter().write("Too many Requests !!!!!!!");
        }
    }
}
