package org.fisco.bcos.config;

import org.fisco.bcos.component.LoginHandler;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class MyConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        LoginHandler loginHandler = new LoginHandler();
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginHandler);
        //拦截路径
        loginRegistry.addPathPatterns("/transaction","/receive","/due","/finance","/payback","/transfer");
    }
}
