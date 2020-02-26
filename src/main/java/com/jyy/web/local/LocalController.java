package com.jyy.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
public class LocalController {

    @GetMapping("/accountbind")
    private String accountBind(){
        return "local/customerbind";
    }



    @GetMapping("/login")
    private String login(){
        return "local/login";
    }
    @GetMapping("/changepsw")
    private String changepsw(){
        return "local/changepsw";
    }

}
