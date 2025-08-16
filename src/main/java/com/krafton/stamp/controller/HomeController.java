package com.krafton.stamp.controller;

import com.krafton.stamp.security.PrincipalUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home(@AuthenticationPrincipal PrincipalUser user) {
        if (user == null) return "로그인하지 않았습니다.";
        return "환영합니다, " + user.getUser().getUsername() + "님!";
    }
}
