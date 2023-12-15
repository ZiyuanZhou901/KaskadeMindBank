package com.kaskademindbank.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kaskademindbank.entity.FillQuestion;
import com.kaskademindbank.entity.JudgeQuestion;
import com.kaskademindbank.entity.SelectQuestion;
import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.FillQuestionMapper;
import com.kaskademindbank.mapper.JudgeQuestionMapper;
import com.kaskademindbank.mapper.SelectQuestionMapper;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IFillQuestionService;
import com.kaskademindbank.service.IJudgeQuestionService;
import com.kaskademindbank.service.ISelectQuestionService;
import com.kaskademindbank.service.IUsersService;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.desktop.SystemEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.SwitchPoint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

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
    @Autowired
    FillQuestionMapper fillQuestionMapper;
    @Autowired
    JudgeQuestionMapper judgeQuestionMapper;
    @Autowired
    SelectQuestionMapper selectQuestionMapper;

    @GetMapping("/import")
    public String showImportPage(Model model, HttpSession session) {
        model.addAttribute("successMessage", session.getAttribute("successMessage"));
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("success", session.getAttribute("success"));
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
    @GetMapping("/import/directWord")
    public String showDirectWordPage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return "directWord";
    }

    @GetMapping("/import/directWord/fillQuestion")
    public String showDirectWordFillQuestionPage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("fillQuestion", new FillQuestion());
        return "directWord";
    }
    @PostMapping("/import/directWord/fillQuestion")
    public String handleFormSubmission(@ModelAttribute FillQuestion fillQuestion, Model model) {
        // 在这里可以处理你的表单数据，可以将数据保存到数据库等
        // fillQuestion 对象将自动由Thymeleaf绑定

        // 例如，你可以将填充后的 fillQuestion 对象添加到模型中，以便在视图中显示
        model.addAttribute("filledQuestion", fillQuestion);

        // 这里可以返回一个视图名称，用于显示提交后的结果页面
        return "redirect:/import/directWord";  // 根据你的需求更改视图名称
    }
    @PostMapping("/import/directWord")
    public String directImportWord(@RequestParam("wordFile") MultipartFile wordFile, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);

        try (InputStream inputStream = wordFile.getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);
            List<String> contentBlocks = extractContentBlocks(document);
            model.addAttribute("contentBlocks", contentBlocks);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing the Word file!");
        }
        return "directWord";
    }

    private List<String> extractContentBlocks(XWPFDocument document) {
        List<String> contentBlocks = new ArrayList<>();
        StringBuilder currentBlock = new StringBuilder();

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();

            if (text.isEmpty()) {
                // 遇到空白行，
                if (currentBlock.length() > 0) {
                    contentBlocks.add(currentBlock.toString());
                    currentBlock = new StringBuilder(); // 重置当前块
                }
            } else {
                //添加换行
                currentBlock.append(text);
                currentBlock.append("\r\n");
            }
        }

        // 处理文档末尾可能的最后一个块
        if (currentBlock.length() > 0) {
            contentBlocks.add(currentBlock.toString());
        }
        return contentBlocks;
    }


    @PostMapping("/import/fromExcel")
    public String importByExcel(@RequestParam("excelFile") MultipartFile excelFile, Model model, HttpSession session) {
        Integer userId;
        Users user = (Users) session.getAttribute("user");
        userId = usersMapper.findUserIdByUsername(user.getUserName());

        // 检查上传的文件是否为空
        if (!excelFile.isEmpty()) {
            try (InputStream inputStream = excelFile.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);

                // 遍历每个表单
                for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                    Sheet sheet = workbook.getSheetAt(sheetIndex);
                    // 根据表单名称处理每个表单
                    switch (sheet.getSheetName()) {
                        case "Fill":
                            processFillSheet(sheet, userId);
                            break;
                        case "Judge":
                            processJudgeSheet(sheet, userId);
                            break;
                        case "Select":
                            processSelectSheet(sheet, userId);
                            break;
                    }
                }

                session.setAttribute("success", "Excel File imported successfully!");
            } catch (IOException | EncryptedDocumentException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "File is empty!");
            }
        } else {
            // 如果文件为空，向模型添加错误消息
            model.addAttribute("errorMessage", "Error processing the file!");
        }

        // 将用户属性添加到模型
        model.addAttribute("user", session.getAttribute("user"));

        // 重定向到导入页面
        return "redirect:/import";
    }

    private void processFillSheet(Sheet sheet, Integer userId) {
        // 假设第一行包含列标题
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            // 跳过标题行
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 假设列按顺序为：主题、描述、答案
            String subject = getCellValueAsString(row.getCell(0));
            String description = getCellValueAsString(row.getCell(1));
            String answer = getCellValueAsString(row.getCell(2));

            // 创建FillQuestion对象并保存到数据库
            FillQuestion fillQuestion = new FillQuestion();
            fillQuestion.setSubject(subject);
            fillQuestion.setDescription(description);
            fillQuestion.setAnswer(answer);
            fillQuestion.setUserId(userId);
            fillQuestion.setUpTime(LocalDateTime.now());
            fillQuestionMapper.insert(fillQuestion);
        }
    }

    private void processJudgeSheet(Sheet sheet, Integer userId) {
        // 假设第一行包含列标题
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            // 跳过标题行
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 假设列按顺序为：主题、描述、答案
            String subject = getCellValueAsString(row.getCell(0));
            String description = getCellValueAsString(row.getCell(1));
            String answerValue = getCellValueAsString(row.getCell(2));

            // 将"正确"和"错误"映射为相应的值
            String answer = "正确".equals(answerValue) ? "correct" : "错误".equals(answerValue) ? "incorrect" : null;

            // 创建JudgeQuestion对象并保存到数据库
            if (answer != null) {
                JudgeQuestion judgeQuestion = new JudgeQuestion();
                judgeQuestion.setSubject(subject);
                judgeQuestion.setDescription(description);
                judgeQuestion.setAnswer(answer);
                judgeQuestion.setUserId(userId);
                judgeQuestion.setUpTime(LocalDateTime.now());
                judgeQuestionMapper.insert(judgeQuestion);
            }
        }
    }

    private void processSelectSheet(Sheet sheet, Integer userId) {
        // 假设第一行包含列标题
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            // 跳过标题行
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 假设列按顺序为：主题、描述、选项A、选项B、选项C、选项D、答案
            String subject = getCellValueAsString(row.getCell(0));
            String description = getCellValueAsString(row.getCell(1));
            String optionA = getCellValueAsString(row.getCell(2));
            String optionB = getCellValueAsString(row.getCell(3));
            String optionC = getCellValueAsString(row.getCell(4));
            String optionD = getCellValueAsString(row.getCell(5));
            String answer = getCellValueAsString(row.getCell(6));

            // 创建SelectQuestion对象并保存到数据库
            SelectQuestion selectQuestion = new SelectQuestion();
            selectQuestion.setSubject(subject);
            selectQuestion.setDescription(description);
            selectQuestion.setAnswerA(optionA);
            selectQuestion.setAnswerB(optionB);
            selectQuestion.setAnswerC(optionC);
            selectQuestion.setAnswerD(optionD);
            selectQuestion.setAnswer(answer);
            selectQuestion.setUserId(userId);
            selectQuestion.setUpTime(LocalDateTime.now());
            selectQuestionMapper.insert(selectQuestion);
        }
    }


    // 获取单元格值为字符串的辅助方法
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
    @PostMapping("/import/fromWord")
    public String importByWord(@RequestParam("wordFile") MultipartFile wordFile, Model model, HttpSession session) {
        Integer userId;
        Users user = (Users) session.getAttribute("user");
        userId=usersMapper.findUserIdByUsername(user.getUserName());
        try {
            // Check if the uploaded file is not empty
            if (!wordFile.isEmpty()) { 
                // Read the content of the Word file
                InputStream inputStream = wordFile.getInputStream();
                XWPFDocument document = new XWPFDocument(inputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);

                String textContent = extractor.getText();

                processWordContent(textContent,userId);

                extractor.close();
                inputStream.close();

                // Add a success message to the model
                session.setAttribute("success", "Word File imported successfully!");
            } else {
                // Add an error message to the model if the file is empty
                model.addAttribute("errorMessage", "File is empty!");
            }
        } catch (IOException e) {

            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing the file!");
        }
        // Add user attribute to the model
        model.addAttribute("user", session.getAttribute("user"));
        // Redirect to the import page
        return "redirect:/import";
    }
    private void processWordContent(String textContent, Integer userId) {
        String[] lines = textContent.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (line.startsWith("题型：填空")) {
                FillQuestion currentFillQuestion = new FillQuestion();
                i++;
                currentFillQuestion.setSubject(lines[i].split("：")[1]);

                i++;
                currentFillQuestion.setDescription(lines[i].split("：")[1]);

                i++;
                currentFillQuestion.setAnswer(lines[i].split("：")[1]);
                currentFillQuestion.setUserId(userId);
                currentFillQuestion.setUpTime(LocalDateTime.now());
                fillQuestionMapper.insert(currentFillQuestion);
            }else if (line.startsWith("题型：判断")) {
                JudgeQuestion currentJudgeQuestion = new JudgeQuestion();
                i++;
                currentJudgeQuestion.setSubject(lines[i].split("：")[1]);

                i++;
                currentJudgeQuestion.setDescription(lines[i].split("：")[1]);

                i++;
                // Check if the answer is "正确" or "错误" and set accordingly
                String answer = lines[i].split("：")[1];
                if ("正确".equals(answer)) {
                    currentJudgeQuestion.setAnswer("correct");
                } else if ("错误".equals(answer)) {
                    currentJudgeQuestion.setAnswer("incorrect");
                }

                currentJudgeQuestion.setUserId(userId);
                currentJudgeQuestion.setUpTime(LocalDateTime.now());
                judgeQuestionMapper.insert(currentJudgeQuestion);
            } else if (line.startsWith("题型：选择")) {
                SelectQuestion currentSelectQuestion = new SelectQuestion();
                i++;
                currentSelectQuestion.setSubject(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setDescription(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setAnswerA(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setAnswerB(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setAnswerC(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setAnswerD(lines[i].split("：")[1]);

                i++;
                currentSelectQuestion.setAnswer(lines[i].split("：")[1]);

                currentSelectQuestion.setUserId(userId);
                currentSelectQuestion.setUpTime(LocalDateTime.now());
                selectQuestionMapper.insert(currentSelectQuestion);
            }

        }
    }





}


