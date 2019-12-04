package com.demo.listener;

import com.demo.config.redis.RedisContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

public class SpringMvcDemoListener extends ContextLoaderListener {

    @Override
    public final void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("hello world!");

        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[] {
                        "redis-context.xml"
                },
                RedisContext.class
        );

        super.contextInitialized(servletContextEvent);
    }
}
