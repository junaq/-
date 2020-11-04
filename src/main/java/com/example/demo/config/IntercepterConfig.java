package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginIntercepter()).addPathPatterns("/**").excludePathPatterns(
				Arrays.asList("/login/**", "/pages/login/**", "/css/**", "/js/**", "/lib/**", "/img/**"));
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")// 必须字段，允许跨域的域名，可以用*表示允许任何域名使用
				.allowedMethods("*")// 可选字段，允许跨域的方法，使用*表示允许任何方法
				.allowCredentials(true)// 可选字段，布尔值，表示是否允许发送cookie信息
				.allowedHeaders("*")// 允许任何请求头
				 .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
				.maxAge(1000); // 可选字段，用来指定本次预检请求的有效期，单位为秒
	}

}