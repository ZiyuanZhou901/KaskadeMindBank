package com.kaskademindbank.service;

import com.kaskademindbank.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
public interface IUsersService extends IService<Users> {
    String login(Users user, Model model, HttpSession session);

    String register(Users user, Model model);
}
