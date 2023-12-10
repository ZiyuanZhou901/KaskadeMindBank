package com.kaskademindbank.controller;

import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.JudgeQuestion;
import com.kaskademindbank.entity.SelectQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IFillQuestionService;
import com.kaskademindbank.service.IJudgeQuestionService;
import com.kaskademindbank.service.ISelectQuestionService;
import com.kaskademindbank.service.IUsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.desktop.SystemEventListener;
import java.lang.invoke.SwitchPoint;
import java.util.List;

@Controller
public class ImportController {
    @Autowired
    IFillQuestionService fillQuestionService;
    @Autowired
    IJudgeQuestionService judgeQuestionService;
    @Autowired
    ISelectQuestionService selectQuestionService;
    @Autowired
    UsersMapper usersMapper;

    @GetMapping("/import")
    public String showImportPage(Model model, HttpSession session) {
        model.addAttribute("successMessage", session.getAttribute("successMessage"));
        model.addAttribute("user", session.getAttribute("user"));
        return "import";
    }

    @GetMapping("/import/template/fillQuestion")
    public String showTemplateImportPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<String> uploadedSubjects = fillQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        System.out.println("uploadSubjects: " + model.getAttribute("uploadedSubjects"));
        model.addAttribute("user",user);
        model.addAttribute("fillQuestion", new FillQuestion());
        model.addAttribute("questionType", "fillQuestion");
        return "template_import";
    }

    @PostMapping("/import/template/fillQuestion")
    public String importByTemplate(FillQuestion fillQuestion,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("audioFile") MultipartFile audioFile,
                                   @RequestParam("videoFile") MultipartFile videoFile,
                                   Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return fillQuestionService.importByTemplate(fillQuestion, model, session, imageFile, audioFile, videoFile);
    }


    @GetMapping("/import/template/judgeQuestion")
    public String showTemplateImportJudgePage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<String> uploadedSubjects = judgeQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        model.addAttribute("user", user);
        model.addAttribute("judgeQuestion", new JudgeQuestion());
        model.addAttribute("questionType", "judgeQuestion");
        return "template_import";
    }

    @PostMapping("/import/template/judgeQuestion")
    public String importByTemplateJudge(JudgeQuestion judgeQuestion,
                                        @RequestParam("imageFile") MultipartFile imageFile,
                                        @RequestParam("audioFile") MultipartFile audioFile,
                                        @RequestParam("videoFile") MultipartFile videoFile,
                                        Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return judgeQuestionService.importByTemplate(judgeQuestion, model, session, imageFile, audioFile, videoFile);
    }

    @GetMapping("/import/template/selectQuestion")
    public String showTemplateImportSelectPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<String> uploadedSubjects = selectQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        model.addAttribute("user", user);
        model.addAttribute("selectQuestion", new SelectQuestion());
        model.addAttribute("questionType", "selectQuestion");
        return "template_import";
    }

    @PostMapping("/import/template/selectQuestion")
    public String importByTemplateSelect(SelectQuestion selectQuestion,
                                         @RequestParam("imageFile") MultipartFile imageFile,
                                         @RequestParam("audioFile") MultipartFile audioFile,
                                         @RequestParam("videoFile") MultipartFile videoFile,
                                         Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return selectQuestionService.importByTemplate(selectQuestion, model, session, imageFile, audioFile, videoFile);
    }

    @GetMapping("/import/directinput")
    public String showDirectInputImportPage() {
        return "direct_input_import";
    }

    @PostMapping("/import/directinput")
    public String importByDirectInput(String questionText, Model model) {

        //importService.importByDirectInput(questionText);

        // 添加成功信息到 model，用于在前端显示
        model.addAttribute("successMessage", "Direct input import successful");

        return "direct_input_import";
    }
}

