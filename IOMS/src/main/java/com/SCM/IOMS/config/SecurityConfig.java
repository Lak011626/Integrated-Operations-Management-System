package com.SCM.IOMS.config;
// Cấu hình bảo mật, chặn/mở các API
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Tắt bảo vệ CSRF để dễ dàng test API bằng Postman/Javascript
            .csrf(csrf -> csrf.disable()) 
            
            // Tạm thời cho phép truy cập TẤT CẢ các đường dẫn mà không cần đăng nhập
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            )
            
            // TẮT form đăng nhập mặc định của Spring Boot
            .formLogin(form -> form.disable()) 
            
            // Tắt xác thực HTTP Basic mặc định
            .httpBasic(basic -> basic.disable()); 
            
        return http.build();
    }
}