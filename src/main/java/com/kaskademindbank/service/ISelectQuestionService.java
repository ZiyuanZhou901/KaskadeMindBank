package com.kaskademindbank.service;

import com.kaskademindbank.entity.SelectQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * @author ZiyuanZhou
 */
public interface ISelectQuestionService extends IService<SelectQuestion> {

    String importByTemplate(SelectQuestion selectQuestion, Model model,
                            HttpSession session,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            @RequestParam("audioFile") MultipartFile audioFile,
                            @RequestParam("videoFile") MultipartFile videoFile);

    List<String> getUploadedSubjectsByUserId(Integer userId);

    String editSelectQuestion(SelectQuestion selectQuestion, Model model, HttpSession session, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile);
    String directSelectQuestion(SelectQuestion selectQuestion, Model model, HttpSession session, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile);
}
