package com.kaskademindbank.service;

import com.kaskademindbank.entity.FillQuestion;
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
public interface IFillQuestionService extends IService<FillQuestion> {

    String importByTemplate(FillQuestion fillQuestion, Model model, HttpSession session);
}
