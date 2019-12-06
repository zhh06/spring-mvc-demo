package com.demo.listener;

import com.demo.config.redis.RedisContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

public class SpringMvcDemoListener extends ContextLoaderListener {

    @Override
    public final void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("hello world!");

        RedisContext.getInstance();

        super.contextInitialized(servletContextEvent);
    }
}
