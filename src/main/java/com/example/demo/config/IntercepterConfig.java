package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginIntercepter()).addPathPatterns("/**").excludePathPatterns(Arrays.asList("/login/**","/pages/login/**",
				"/css/**", "/js/**", "/lib/**", "/img/**"));
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}