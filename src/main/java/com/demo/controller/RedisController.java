package com.demo.controller;

import com.demo.utils.RedisUtilsFirst;
import com.demo.utils.RedisUtilsSecond;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/redis")
public class RedisController {

    @Resource
    private RedisUtilsFirst<String> redisUtilsFirst;

    @Resource
    private RedisUtilsSecond<String> redisUtilsSecond;

    @RequestMapping(value = "/set1")
    @ResponseBody
    public String set1(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") String value
    ) {
        System.out.println("key: " + key + ", value: " + value);
        return String.valueOf(redisUtilsFirst.set(key, value));
    }

    @RequestMapping(value = "/set2")
    @ResponseBody
    public String set2(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") String value
    ) {
        System.out.println("key: " + key + ", value: " + value);
        return String.valueOf(redisUtilsSecond.set(key, value));
    }

}
