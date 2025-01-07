package com.katrien.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * @author : Katrien
 * @description : Spring Security的配置类，用于设置应用程序的安全策略
 */
//表明这是一个Spring配置类
@Configuration
//启用Spring Security的Web安全功能
@EnableWebSecurity
public class SecurityConfig {

    //定义Spring Security的安全策略
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //允许跨域
            .cors().configurationSource(corsConfigurationSource())
            .and()
                //关闭跨站请求防护
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                //允许匿名访问
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/api/users/login", "/api/users/register").permitAll()
            .anyRequest().permitAll();
        return http.build();
    }

    //定义跨域配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        //允许来自http://localhost:5173的请求
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
