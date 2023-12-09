package com.kaskademindbank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kaskademindbank.entity.JudgeQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.JudgeQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IJudgeQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.awt.desktop.SystemEventListener;
import java.time.LocalDateTime;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Service
public class JudgeQuestionServiceImpl extends ServiceImpl<JudgeQuestionMapper, JudgeQuestion> implements IJudgeQuestionService {
    @Autowired
    private JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Override
    public String importByTemplate(JudgeQuestion judgeQuestion, Model model, HttpSession session) {
        // 1. 数据校验
        if (judgeQuestion == null || StringUtils.isEmpty(judgeQuestion.getSubject())
                || StringUtils.isEmpty(judgeQuestion.getDescription())) {
            model.addAttribute("error", "Invalid judge question information");
            return "template_import";  // 返回导入页面，显示错误消息
        }
        // 2. 设置用户ID，你可能需要从 session 中获取用户信息
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            judgeQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }
        // 3. 设置其他属性，比如 upTime 等
        judgeQuestion.setUpTime(LocalDateTime.now());
        // 4. 设置答案为 1（正确），可以根据实际需要调整
        if ("correct".equals(judgeQuestion.getAnswer())) {
            judgeQuestion.setAnswer("1"); // 如果选择的是 "Correct"，设置为 "1"
        } else {
            judgeQuestion.setAnswer("0"); // 如果选择的是 "Incorrect"，设置为 "0"
        }
        // 5. 题目导入，调用 Mapper 插入到数据库
        judgeQuestionMapper.insert(judgeQuestion);

        // 6. 添加成功信息到 model，用于在前端显示
        model.addAttribute("successMessage", "Judge question import successful");

        return "redirect:/import";
    }
}
