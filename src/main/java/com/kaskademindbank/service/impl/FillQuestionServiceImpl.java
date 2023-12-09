package com.kaskademindbank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.FillQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IFillQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Service
public class FillQuestionServiceImpl extends ServiceImpl<FillQuestionMapper, FillQuestion> implements IFillQuestionService {

    @Autowired
    private FillQuestionMapper fillQuestionMapper;
    @Autowired
    private UsersMapper usersMapper;
    //未处理文件问题
    @Override
    public String importByTemplate(FillQuestion fillQuestion, Model model, HttpSession session) {

        // 1. 数据校验
        if (fillQuestion == null || StringUtils.isEmpty(fillQuestion.getSubject())
                || StringUtils.isEmpty(fillQuestion.getDescription()) || StringUtils.isEmpty(fillQuestion.getAnswer())) {
            model.addAttribute("error", "Invalid fill question information");
            return "template_import"; // 返回导入页面，显示错误消息
        }

        // 2. 设置用户ID，你可能需要从 session 中获取用户信息
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            fillQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }
        // 3. 设置其他属性，比如 upTime 等
        fillQuestion.setUpTime(LocalDateTime.now());

        // 4. 题目导入，调用 Mapper 插入到数据库
        fillQuestionMapper.insert(fillQuestion);

        // 5. 添加成功信息到 model，用于在前端显示
        model.addAttribute("successMessage", "Fill question import successful");

        return "redirect:/import";
    }
}
