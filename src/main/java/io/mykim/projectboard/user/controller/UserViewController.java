package io.mykim.projectboard.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserViewController {
    @GetMapping("/users/sign-up")
    public String createUserView() {
        log.info("[GET] /users/sign-up  =>  create User(sign up) view");
        return "users/sign-up";
    }

    @GetMapping("/users/sign-in")
    public String authenticateUserView() {
        log.info("[GET] /users/sign-in  =>  authenticate User(sign in) view");
        return "users/sign-in";
    }

}
