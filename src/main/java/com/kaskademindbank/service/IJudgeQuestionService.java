package com.kaskademindbank.service;

import com.kaskademindbank.entity.JudgeQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
public interface IJudgeQuestionService extends IService<JudgeQuestion> {

    String importByTemplate(JudgeQuestion judgeQuestion, Model model,
                            HttpSession session,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            @RequestParam("audioFile") MultipartFile audioFile,
                            @RequestParam("videoFile") MultipartFile videoFile);

    List<String> getUploadedSubjectsByUserId(Integer userId);

    String editJudgeQuestion(JudgeQuestion judgeQuestion, Model model, HttpSession session, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile);
}
