package com.demo.controller;

import com.demo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    private RedisUtils<String> redisUtils;

    @RequestMapping(value = "/set")
    @ResponseBody
    public String set(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") String value
    ) {
        System.out.println("key: " + key + ", value: " + value);
        return String.valueOf(redisUtils.set(key, value));
    }

}
