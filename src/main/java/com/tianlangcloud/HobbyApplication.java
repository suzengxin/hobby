package com.tianlangcloud;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HobbyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HobbyApplication.class, args);
	}
	
	/**
     * 文件上传配置
     * 
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("102400KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("1024000KB");
        return factory.createMultipartConfig();
    }
}
