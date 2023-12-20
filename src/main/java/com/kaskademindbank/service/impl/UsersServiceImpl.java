package com.kaskademindbank.service.impl;

import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author ZiyuanZhou
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public String login(Users user, Model model, HttpSession session) {
        System.out.println(user);
        String username = user.getUserName();
        String password = user.getPassword();
        if (username == null || password == null) {
            model.addAttribute("error", "Username and password are required");
            return "login";
        }

        Users foundUser = usersMapper.findByUsername(username);
        if (foundUser == null || !foundUser.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("user", user);

        return "redirect:/import";
    }

    @Override
    public String register(Users user, Model model) {

        String username = user.getUserName();
        String password = user.getPassword();
        String email = user.getEmail();

        if (username == null || password == null || email == null) {
            model.addAttribute("error", "Username, password, and email are required");
            return "register";
        }

        Users foundUser = usersMapper.findByUsername(username);
        if (foundUser != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        foundUser = usersMapper.findByEmail(email);
        if (foundUser != null) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        Users newUser = new Users();
        newUser.setUserName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        usersMapper.insert(newUser);

        return "login";
    }

    @Override
    public String edit(Users newuser, Model model, HttpSession session) {
        String curName=session.getAttribute("username").toString();
        Users user=usersMapper.findByUsername(curName);
        String username = newuser.getUserName();
        String password = newuser.getPassword();
        String email = newuser.getEmail();
        if (username.length() >=1) {
            user.setUserName(username);
        }
        if (password.length()>=1) {
            user.setPassword(password);
        }
        if (email.length()>=1) {
            user.setEmail(email);
        }
        usersMapper.updateById(user);
        session.setAttribute("user", user);
        return "redirect:/profile";
    }
}

