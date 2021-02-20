package com.sang.demoaop.controller;

import com.sang.demoaop.architect.annotation.SystemControllerLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/index")
    @SystemControllerLog(description="测试")
    public String indexTest(){
        return "你好！！！";
    }

    @GetMapping("/zero")
    @SystemControllerLog(description="测试")
    public Object zeroTest(){
        return 1/0;
    }
}
