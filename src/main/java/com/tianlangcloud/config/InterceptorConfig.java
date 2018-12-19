package com.tianlangcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	@Bean
    LoginInterceptor localInterceptor() {
        return new LoginInterceptor();
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//这里可以添加多个拦截器
		//registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(localInterceptor()).addPathPatterns("/**").excludePathPatterns("/client/**");
		super.addInterceptors(registry);
	}
}
