package com.kaskademindbank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kaskademindbank.entity.SelectQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.SelectQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.ISelectQuestionService;
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
public class SelectQuestionServiceImpl extends ServiceImpl<SelectQuestionMapper, SelectQuestion> implements ISelectQuestionService {
    @Autowired
    private SelectQuestionMapper selectQuestionMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Override
    public String importByTemplate(SelectQuestion selectQuestion, Model model, HttpSession session) {
        // 1. 数据校验
        if (selectQuestion == null || StringUtils.isEmpty(selectQuestion.getSubject())
                || StringUtils.isEmpty(selectQuestion.getDescription())
                || StringUtils.isEmpty(selectQuestion.getAnswerA())
                || StringUtils.isEmpty(selectQuestion.getAnswerB())
                || StringUtils.isEmpty(selectQuestion.getAnswerC())
                || StringUtils.isEmpty(selectQuestion.getAnswerD())) {
            model.addAttribute("error", "Invalid select question information");
            return "template_import";  // 返回导入页面，显示错误消息
        }

        // 2. 设置用户ID，你可能需要从 session 中获取用户信息
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            selectQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }
        // 3. 设置其他属性，比如 upTime 等
        selectQuestion.setUpTime(LocalDateTime.now());

        // 4. 题目导入，调用 Mapper 插入到数据库
        selectQuestionMapper.insert(selectQuestion);

        // 5. 添加成功信息到 model，用于在前端显示
        model.addAttribute("successMessage", "Select question import successful");

        return "redirect:/import";
    }
}
