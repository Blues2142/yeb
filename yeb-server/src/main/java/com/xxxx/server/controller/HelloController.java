package com.xxxx.server.controller;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Api(tags = "测试controller 接口")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
