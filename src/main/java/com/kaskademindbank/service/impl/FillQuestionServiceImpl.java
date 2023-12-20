package com.kaskademindbank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.FillQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IFillQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ZiyuanZhou
 */
@Service
public class FillQuestionServiceImpl extends ServiceImpl<FillQuestionMapper, FillQuestion> implements IFillQuestionService {

    @Autowired
    private FillQuestionMapper fillQuestionMapper;
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
    public String importByTemplate(FillQuestion fillQuestion, Model model, HttpSession session,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("audioFile") MultipartFile audioFile,
                                   @RequestParam("videoFile") MultipartFile videoFile) {

        // 1. 数据校验
        if (fillQuestion == null || StringUtils.isEmpty(fillQuestion.getSubject())
                || StringUtils.isEmpty(fillQuestion.getDescription()) || StringUtils.isEmpty(fillQuestion.getAnswer())) {
            model.addAttribute("error", "无效的填空题信息");
            return "template_import"; // 返回导入页面，显示错误消息
        }

        // 2. 设置用户ID
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            fillQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }

        // 3. 设置其他属性，比如 upTime 等
        fillQuestion.setUpTime(LocalDateTime.now());

        // 4. 处理文件上传
        try {
            if (!imageFile.isEmpty()) {
                String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
                handleFileUpload(imageFile, imageFileName);
                fillQuestion.setPicFile(imageFileName);
            }
            if (!audioFile.isEmpty()) {
                String audioFileName = "audio_" + UUID.randomUUID() + ".mp3";
                handleFileUpload(audioFile, audioFileName);
                fillQuestion.setVoiFile(audioFileName);
            }
            if (!videoFile.isEmpty()) {
                String videoFileName = "video_" + UUID.randomUUID() + ".mp4";
                handleFileUpload(videoFile, videoFileName);
                fillQuestion.setVidFile(videoFileName);
            }
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("error", "文件大小超过限制");
            return "template_import";
        } catch (IOException e) {
            model.addAttribute("error", "上传文件时发生错误");
            return "template_import";
        }

        // 5. 题目导入，调用 Mapper 插入到数据库
        fillQuestionMapper.insert(fillQuestion);

        // 6. 添加成功信息到 model，用于在前端显示
        session.setAttribute("successMessage", "Successfully imported fill question");

        return "redirect:/import";
    }

    @Override
    public List<String> getUploadedSubjectsByUserId(Integer userId) {
        return fillQuestionMapper.findSubjectsByUserId(userId);
    }

    @Override
    public String directFillQuestion(FillQuestion fillQuestion, Model model, HttpSession session,
                                     @RequestParam("imageFile") MultipartFile imageFile,
                                     @RequestParam("audioFile") MultipartFile audioFile,
                                     @RequestParam("videoFile") MultipartFile videoFile) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            fillQuestion.setUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        }
        fillQuestion.setUpTime(LocalDateTime.now());
        try {
            if (!imageFile.isEmpty()) {
                String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
                handleFileUpload(imageFile, imageFileName);
                fillQuestion.setPicFile(imageFileName);
            }
            if (!audioFile.isEmpty()) {
                String audioFileName = "audio_" + UUID.randomUUID() + ".mp3";
                handleFileUpload(audioFile, audioFileName);
                fillQuestion.setVoiFile(audioFileName);
            }
            if (!videoFile.isEmpty()) {
                String videoFileName = "video_" + UUID.randomUUID() + ".mp4";
                handleFileUpload(videoFile, videoFileName);
                fillQuestion.setVidFile(videoFileName);
            }
        } catch (MaxUploadSizeExceededException e) {
            model.addAttribute("error", "文件大小超过限制");
            return "template_import";
        } catch (IOException e) {
            model.addAttribute("error", "上传文件时发生错误");
            return "template_import";
        }
        fillQuestionMapper.insert(fillQuestion);
        session.setAttribute("contentBlocks",model.getAttribute("contentBlocks"));
        session.setAttribute("successMessage", "Successfully imported fill question");

        return "redirect:/import/directWord";
    }

    @Override
    public String editFillQuestion(FillQuestion fillQuestion, Model model, HttpSession session, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile) {
        Integer questionId = (Integer) session.getAttribute("questionId");
        FillQuestion existingQuestion = fillQuestionMapper.selectById(questionId);
        System.out.println(questionId);
        existingQuestion.setSubject(fillQuestion.getSubject());
        existingQuestion.setDescription(fillQuestion.getDescription());
        existingQuestion.setAnswer(fillQuestion.getAnswer());
        try {
            if (!imageFile.isEmpty()) {
                String imageFileName = "image_" + UUID.randomUUID() + ".jpg";
                handleFileUpload(imageFile, imageFileName);
                if (existingQuestion.getPicFile()!=null){
                    File file = new File(uploadPath+existingQuestion.getPicFile());
                    if (file.exists()){
                        file.delete();
                    }
                }
                existingQuestion.setPicFile(imageFileName);
            }
            if (!audioFile.isEmpty()) {
                String audioFileName = "audio_" + UUID.randomUUID() + ".mp3";
                handleFileUpload(audioFile, audioFileName);
                if (existingQuestion.getVoiFile()!=null){
                    File file = new File(uploadPath+existingQuestion.getVoiFile());
                    if (file.exists()){
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
        fillQuestionMapper.updateById(existingQuestion);
        return "redirect:/browse/fill/"+questionId;
    }

}

