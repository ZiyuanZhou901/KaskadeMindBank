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
import com.kaskademindbank.vo.SelectedItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ZiyuanZhou
 */
@Controller
public class BrowseController {

    @Autowired
    FillQuestionMapper fillQuestionMapper;
    @Autowired
    JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    SelectQuestionMapper selectQuestionMapper;
    @Autowired
    UsersMapper usersMapper;
    @GetMapping("/browse")
    public String browseOverview(Model model, HttpSession session, @RequestParam(defaultValue = "1") int page) {
        Users user = (Users) session.getAttribute("user");

        int pageSize = 10;
        List<FillQuestion> fillQuestions = fillQuestionMapper.findFillQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        List<JudgeQuestion> judgeQuestions = judgeQuestionMapper.findJudgeQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        List<SelectQuestion> selectQuestions = selectQuestionMapper.findSelectQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));

        List<QuestionOverview> allQuestions = new ArrayList<>();
        List<QuestionOverview> fillQuestionOverviews = fillQuestions.stream()
                .map(q -> new QuestionOverview("Fill", q.getFquestionId(), q.getSubject(), q.getDescription(), q.getUpTime()))
                .toList();
        allQuestions.addAll(fillQuestionOverviews);

        // 判断题
        List<QuestionOverview> judgeQuestionOverviews = judgeQuestions.stream()
                .map(q -> new QuestionOverview("Judge", q.getJquestionId(), q.getSubject(), q.getDescription(), q.getUpTime()))
                .toList();
        allQuestions.addAll(judgeQuestionOverviews);

        // 选择题
        List<QuestionOverview> selectQuestionOverviews = selectQuestions.stream()
                .map(q -> new QuestionOverview("Select", q.getSquestionId(), q.getSubject(), q.getDescription(), q.getUpTime()))
                .toList();
        allQuestions.addAll(selectQuestionOverviews);

        // 将结果按照 upTime 排序
        allQuestions.sort(Comparator.comparing(QuestionOverview::getUpTime).reversed());

        // 使用 PageHelper 进行分页查询
        PageHelper.startPage(page, pageSize);
        List<QuestionOverview> pagedQuestions = allQuestions.subList(Math.min((page - 1) * pageSize, allQuestions.size()),
                Math.min(page * pageSize, allQuestions.size()));

        PageInfo<QuestionOverview> pageInfo = new PageInfo<>(pagedQuestions);
        Integer totalPage = (int) Math.ceil((double) allQuestions.size() / pageSize);
        // 将结果传递给前端
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("questionList",pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        System.out.println(totalPage);
        return "browse_overview";
    }
    @GetMapping("/browse/{questionType}/{questionId}")
    public String browseDetail(@PathVariable String questionType,
                               @PathVariable Integer questionId,
                               Model model, HttpSession session) {
        String tableName = questionType + "Question";
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        if ("fill".equals(questionType)) {
            FillQuestion fillQuestion = fillQuestionMapper.selectById(questionId);
            model.addAttribute("type", "fill");
            model.addAttribute("question", fillQuestion);
        } else if ("judge".equals(questionType)) {
            JudgeQuestion judgeQuestion = judgeQuestionMapper.selectById(questionId);
            model.addAttribute("type", "judge");
            model.addAttribute("question", judgeQuestion);
        } else if ("select".equals(questionType)) {
            SelectQuestion selectQuestion = selectQuestionMapper.selectById(questionId);
            model.addAttribute("type", "select");
            model.addAttribute("question", selectQuestion);
        }
        return "browse_detail";
    }
    @GetMapping("/browse/export")
    public String exportQuestions(Model model, HttpSession session) {
        // 提供导出选项，允许用户选择导出方式和筛选条件
        return "export_questions";
    }

    @PostMapping("/browse/export")
    public String handleExportRequest(@RequestBody List<SelectedItem> selectedItems, Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));

        //selectedItems是一个SelectedItem的List，其中每个SelectedItem包含了一个题目的type和id
        //遍历每个selectedItem，根据type和id，从数据库中获取对应的题目
        //将题目导出到word中
        //让用户选择存储位置
        return "redirect:/browse";
    }

}
