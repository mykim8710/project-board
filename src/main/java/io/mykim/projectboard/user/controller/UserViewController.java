package io.mykim.projectboard.user.controller;

import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {
    private final UserService userService;

    @GetMapping("/users/sign-up")
    public String createUserView(Model model) {
        log.info("[GET] /users/sign-up  =>  create User(sign up) view");
        model.addAttribute("user", new UserCreateDto());
        return "users/sign-up";
    }

    @PostMapping("/users/sign-up")
    public String createUser(@Validated @ModelAttribute("user") UserCreateDto createDto, BindingResult bindingResult) {
        log.info("[POST] /users/sign-up  =>  create User(sign up)  UserCreateDto = {}", createDto);

        if(bindingResult.hasErrors()) {
            log.info("validation errors = {}", bindingResult);
            return "users/sign-up";
        }

        userService.createUser(createDto);

        return "redirect:/users/sign-in";
    }

    @GetMapping("/users/sign-in")
    public String authenticateUserView() {
        log.info("[GET] /users/sign-in  =>  authenticate User(sign in) view");
        return "users/sign-in";
    }

}
