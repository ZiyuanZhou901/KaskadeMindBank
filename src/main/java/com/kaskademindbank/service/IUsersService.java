package com.kaskademindbank.service;

import com.kaskademindbank.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ui.Model;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
public interface IUsersService extends IService<Users> {
    String login(Users user, Model model);

    String register(Users user, Model model);
}
