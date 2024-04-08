package com.todd.toj_backend.config.security;

import com.todd.toj_backend.serviceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig{
    @Autowired 
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    // 自定义返回结果：没有权限访问时
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    // 自定义返回结果：没有登录或 token 过期时
    private RestfulAuthenticationEntryPoint restfulAuthenticationEntryPoint;

    @Autowired
    // JWT 拦截器
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        for(String url : ignoreUrlsConfig.getUrls()){
            http.authorizeHttpRequests(auth->auth
                    .requestMatchers(url).permitAll());
        }
        http
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //不通过Session获取SecurityContext
                .sessionManagement(session->{session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);})
                .exceptionHandling(customizer->customizer
                        .accessDeniedHandler(restfulAccessDeniedHandler)
                        .authenticationEntryPoint(restfulAuthenticationEntryPoint))
                .authorizeHttpRequests(auth->auth
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
