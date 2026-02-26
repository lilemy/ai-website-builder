package com.lilemy.aiwebsitebuilder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.lilemy.aiwebsitebuilder.mapper")
public class AiWebsiteBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiWebsiteBuilderApplication.class, args);
    }

}
