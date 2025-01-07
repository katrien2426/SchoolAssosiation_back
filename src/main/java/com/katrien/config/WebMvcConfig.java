package com.katrien.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.time.format.DateTimeFormatter;

/**
 * @author : Katrien
 * @description : Spring MVC的配置类，用于设置应用程序的MvcConfig
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 定义了一个日期格式字符串，用于 JSON 序列化和反序列化
    private static final String dateFormat = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        };
    }
}
