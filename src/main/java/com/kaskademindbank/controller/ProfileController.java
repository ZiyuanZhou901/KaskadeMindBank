package com.kaskademindbank.controller;

import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.FillQuestionMapper;
import com.kaskademindbank.mapper.JudgeQuestionMapper;
import com.kaskademindbank.mapper.SelectQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    FillQuestionMapper fillQuestionMapper;
    @Autowired
    JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    SelectQuestionMapper selectQuestionMapper;
    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        //查找fill judge select每个类别在过去七天中每天的上传数量，并添加给model
        List<Map<String, Integer>> fillMap = fillQuestionMapper.countByUserIdAndDate(usersMapper.findUserIdByUsername(user.getUserName()));
        for(Map<String, Integer> map : fillMap) {
            System.out.println(map.get("upTime"));
            System.out.println(map.get("uploadCount"));
        }
        List<Map<String, Integer>> judgeMap = judgeQuestionMapper.countByUserIdAndDate(usersMapper.findUserIdByUsername(user.getUserName()));
        List<Map<String, Integer>> selectMap = selectQuestionMapper.countByUserIdAndDate(usersMapper.findUserIdByUsername(user.getUserName()));
        model.addAttribute("fillMap", fillMap);
        model.addAttribute("judgeMap", judgeMap);
        model.addAttribute("selectMap", selectMap);
        //查找类别数量
        Integer fillCount = fillQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer judgeCount = judgeQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer selectCount = selectQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer totalCount = fillCount + judgeCount + selectCount;
        model.addAttribute("fillCount", fillCount);   
        model.addAttribute("judgeCount", judgeCount);
        model.addAttribute("selectCount", selectCount);
        model.addAttribute("totalCount", totalCount);
        return "profile";
    }

}
