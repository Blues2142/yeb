package com.xxxx.server.config.security;
import com.xxxx.server.service.IAdminService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Resource
    private IAdminService adminService;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Resource
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    @Bean
    protected SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        //使用JWT，不需要csrf
        http.csrf(withDefaults());
        //基于token，不需要session
        http.sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorizeHttpRequests->authorizeHttpRequests.requestMatchers("/login","/logout").permitAll().anyRequest().authenticated());
        //禁用缓存
        http.headers(headers->headers.cacheControl(withDefaults()));
        //添加jwt登陆授权拦截器
        http.addFilterBefore(jwtAuthencationTokenFilter(),UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling(exceptionHandling->exceptionHandling.accessDeniedHandler(restfulAccessDeniedHandler).authenticationEntryPoint(restAuthorizationEntryPoint));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return adminService::getAdminByUserName;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }


}
