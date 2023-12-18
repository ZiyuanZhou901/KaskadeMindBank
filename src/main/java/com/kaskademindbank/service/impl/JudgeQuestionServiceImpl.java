package com.kaskademindbank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.JudgeQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.JudgeQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IJudgeQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.desktop.SystemEventListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @Value("${file.path}")  // 假设你在 application.properties 中配置了上传路径
    private String uploadPath;

    // 处理文件上传
    private void handleFileUpload(MultipartFile file, String fileName) throws IOException {
        String filePath = Paths.get(uploadPath, fileName).toString();
        file.transferTo(new File(filePath));
    }
    @Override
    public String importByTemplate(JudgeQuestion judgeQuestion, Model model, HttpSession session,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("audioFile") MultipartFile audioFile,
                                   @RequestParam("videoFile") MultipartFile videoFile) {
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

        try {
            if (!imageFile.isEmpty()) {
                String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
                handleFileUpload(imageFile, imageFileName);
                judgeQuestion.setPicFile(imageFileName);
            }
            if (!audioFile.isEmpty()) {
                String audioFileName = "audio_" + UUID.randomUUID() + ".mp3";
                handleFileUpload(audioFile, audioFileName);
                judgeQuestion.setVoiFile(audioFileName);
            }
            if (!videoFile.isEmpty()) {
                String videoFileName = "video_" + UUID.randomUUID() + ".mp4";
                handleFileUpload(videoFile, videoFileName);
                judgeQuestion.setVidFile(videoFileName);
            }
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("error", "文件大小超过限制");
            return "template_import";
        } catch (IOException e) {
            model.addAttribute("error", "上传文件时发生错误");
            return "template_import";
        }
        // 5. 题目导入，调用 Mapper 插入到数据库
        judgeQuestionMapper.insert(judgeQuestion);

        // 6. 添加成功信息到 model，用于在前端显示
        session.setAttribute("successMessage", "Successfully imported judge question");

        return "redirect:/import";
    }

    @Override
    public List<String> getUploadedSubjectsByUserId(Integer userId) {
        return judgeQuestionMapper.findSubjectsByUserId(userId);
    }

    @Override
    public String editJudgeQuestion(JudgeQuestion judgeQuestion, Model model, HttpSession session, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile) {
        Integer questionId = (Integer) session.getAttribute("questionId");
        JudgeQuestion existingQuestion = judgeQuestionMapper.selectById(questionId);
        System.out.println(questionId);
        existingQuestion.setSubject(judgeQuestion.getSubject());
        existingQuestion.setDescription(judgeQuestion.getDescription());
        existingQuestion.setAnswer(judgeQuestion.getAnswer());
        try {
            if (!imageFile.isEmpty()) {
                String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
                handleFileUpload(imageFile, imageFileName);
                if (existingQuestion.getPicFile() != null) {
                    File file = new File(uploadPath + existingQuestion.getPicFile());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                existingQuestion.setPicFile(imageFileName);
            }
            if (!audioFile.isEmpty()) {
                String audioFileName = "audio_" + UUID.randomUUID() + ".mp3";
                handleFileUpload(audioFile, audioFileName);
                if (existingQuestion.getVoiFile() != null) {
                    File file = new File(uploadPath + existingQuestion.getVoiFile());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                existingQuestion.setVoiFile(audioFileName);
            }
            if (!videoFile.isEmpty()) {
                String videoFileName = "video_" + UUID.randomUUID() + ".mp4";
                handleFileUpload(videoFile, videoFileName);
                if (existingQuestion.getVidFile()!=null){
                    File file = new File(uploadPath+existingQuestion.getVidFile());
                    if (file.exists()){
                        file.delete();
                    }
                }
                existingQuestion.setVidFile(videoFileName);
            }
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("error", "文件大小超过限制");
            return "redirect:/edit";
        } catch (IOException e) {
            model.addAttribute("error", "上传文件时发生错误");
            return "redirect:/edit";
        }
        judgeQuestionMapper.updateById(existingQuestion);
        return "redirect:/browse/judge/"+questionId;
    }

    @Override
    public String directJudgeQuestion(JudgeQuestion judgeQuestion, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            judgeQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }
        judgeQuestion.setUpTime(LocalDateTime.now());
        judgeQuestionMapper.insert(judgeQuestion);
        session.setAttribute("contentBlocks",model.getAttribute("contentBlocks"));
        session.setAttribute("successMessage", "Successfully imported judge question");

        return "redirect:/import/directWord";
    }
}
