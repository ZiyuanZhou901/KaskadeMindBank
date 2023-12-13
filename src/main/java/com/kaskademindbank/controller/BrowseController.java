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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /*@GetMapping("/browse")
    public String browseOverview(Model model, HttpSession session, @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                 @RequestParam(defaultValue="10",value="pageSize")Integer pageSize) {
        Users user = (Users) session.getAttribute("user");
        //Fill为什么没有id？
        List<FillQuestion> fillQuestions = fillQuestionMapper.findFillQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        List<JudgeQuestion> judgeQuestions = judgeQuestionMapper.findJudgeQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        List<SelectQuestion> selectQuestions = selectQuestionMapper.findSelectQuestionsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        List<QuestionOverview> fillQuestionOverviews = fillQuestions.stream()
                .map(q -> new QuestionOverview("Fill", q.getFquestionId(), q.getSubject(), q.getDescription(), q.getUpTime()))
                .toList();
        List<QuestionOverview> allQuestions = new ArrayList<>(fillQuestionOverviews);

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
        PageHelper.startPage(pageNum, pageSize,false);
        try {
            PageInfo<QuestionOverview> pageInfo = new PageInfo<>(allQuestions);
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage();
        }

        model.addAttribute("user", user);
        return "browse_overview";
    }*/
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
        System.out.println(totalPage);
        return "browse_overview";
    }
    @GetMapping("/browse/{questionType}/{questionId}")
    public String browseDetail(@PathVariable String questionType,
                               @PathVariable Integer questionId,
                               Model model, HttpSession session) {
        // 根据题目类型和题目ID获取详细内容
        // 将详细内容传递给前端

        return "browse_detail";
    }
    @GetMapping("/browse/export")
    public String exportQuestions(Model model, HttpSession session) {
        // 提供导出选项，允许用户选择导出方式和筛选条件
        return "export_questions";
    }

    @PostMapping("/browse/export")
    public String doExportQuestions(@RequestParam("exportType") String exportType,
                                    @RequestParam("filterCriteria") String filterCriteria,
                                    Model model, HttpSession session) {

        // 根据用户选择进行题目导出
        // 可能需要异步处理导出操作，避免长时间的等待
        return "export_result";
    }

}
