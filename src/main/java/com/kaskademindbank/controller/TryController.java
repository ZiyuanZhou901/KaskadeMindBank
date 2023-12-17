package com.kaskademindbank.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.JudgeQuestion;
import com.kaskademindbank.entity.SelectQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.FillQuestionMapper;
import com.kaskademindbank.mapper.JudgeQuestionMapper;
import com.kaskademindbank.mapper.SelectQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.vo.QuestionOverview;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ZiyuanZhou
 */
@Controller
public class TryController {
    @Autowired
    FillQuestionMapper fillQuestionMapper;
    @Autowired
    JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    SelectQuestionMapper selectQuestionMapper;
    @Autowired
    UsersMapper usersMapper;
    @GetMapping("/tryit")
    public String tryPage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        Users user = (Users) session.getAttribute("user");
        List<FillQuestion> fillQuestions = fillQuestionMapper.findFillQuestionsByUserIdWoFile(usersMapper.findUserIdByUsername(user.getUserName()));
        fillQuestions.sort(new Comparator<FillQuestion>() {
            @Override 
            public int compare(FillQuestion o1, FillQuestion o2) {
                return (int) (Math.random() * 10) - 5;
            }
        });

        model.addAttribute("user", user);
        session.setAttribute("user", user);
        model.addAttribute("fillQuestions", fillQuestions);
        return "tryFill";
    }
    @GetMapping("/tryitJudge")
    public String tryJudgePage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        List<JudgeQuestion> judgeQuestions = judgeQuestionMapper.findJudgeQuestionsByUserIdWoFile(usersMapper.findUserIdByUsername(user.getUserName()));
        judgeQuestions.sort(new Comparator<JudgeQuestion>() {
            @Override
            public int compare(JudgeQuestion o1, JudgeQuestion o2) {
                return (int) (Math.random() * 10) - 5;
            }
        });

        model.addAttribute("user", user);
        session.setAttribute("user", user);
        model.addAttribute("judgeQuestions", judgeQuestions);
        return "tryJudge";
    }

    @GetMapping("/tryitSelect")
    public String trySelectPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        List<SelectQuestion> selectQuestions = selectQuestionMapper.findSelectQuestionsByUserIdWoFile(usersMapper.findUserIdByUsername(user.getUserName()));
        selectQuestions.sort(new Comparator<SelectQuestion>() {
            @Override
            public int compare(SelectQuestion o1, SelectQuestion o2) {
                return (int) (Math.random() * 10) - 5;
            }
        });

        model.addAttribute("user", user);
        session.setAttribute("user", user);
        model.addAttribute("selectQuestions", selectQuestions);
        return "trySelect";
    }

}
