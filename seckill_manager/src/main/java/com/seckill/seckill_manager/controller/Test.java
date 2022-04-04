package com.seckill.seckill_manager.controller;

import com.seckill.seckill_manager.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/test")
    public Response test(){
        return Response.success("ok",0);
    }
}
