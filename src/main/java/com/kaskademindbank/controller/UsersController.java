package com.kaskademindbank.controller;

import com.kaskademindbank.entity.Users;
import com.kaskademindbank.service.IUsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Controller
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Validated Users user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "login";
        }
        return usersService.login(user,model,session);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Validated Users user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        return usersService.register(user,model);
    }

    @PostMapping("/edit")
    public String edit(Users newuser, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        session.setAttribute("username", user.getUserName());
        System.out.println(newuser.getUserName());
        return usersService.edit(newuser,model,session);
    }
}
