package com.example.RoomRadar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CronController {

    @GetMapping("/")
    public String healthCheck() {
        return "OK";
    }
}
