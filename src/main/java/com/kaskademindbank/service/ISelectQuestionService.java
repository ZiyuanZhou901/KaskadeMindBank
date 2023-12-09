package com.kaskademindbank.service;

import com.kaskademindbank.entity.SelectQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
public interface ISelectQuestionService extends IService<SelectQuestion> {

    String importByTemplate(SelectQuestion selectQuestion, Model model, HttpSession session);
}
