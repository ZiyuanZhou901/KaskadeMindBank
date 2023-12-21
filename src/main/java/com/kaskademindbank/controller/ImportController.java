package com.kaskademindbank.controller;

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
import jakarta.servlet.http.HttpSession;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

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
    @Value("${file.path}")
    private String uploadPath;
    @GetMapping("/import")
    public String showImportPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        Integer fillCount = fillQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer judgeCount = judgeQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer selectCount = selectQuestionMapper.countByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
        Integer totalCount = fillCount + judgeCount + selectCount;
        model.addAttribute("successMessage", session.getAttribute("successMessage"));
        model.addAttribute("fillCount", fillCount);
        model.addAttribute("judgeCount", judgeCount);
        model.addAttribute("selectCount", selectCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("user", user);

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

    @GetMapping("/import/directWord/fillQuestion")
    public String showDirectWordFillQuestionPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user != null) {
            List<String> uploadedSubjects = fillQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        model.addAttribute("fillQuestion", new FillQuestion());
        return "directWordFill";
    }
    @PostMapping("/import/directWord/fillQuestion")
    public String handleFormSubmission(@ModelAttribute FillQuestion fillQuestion,@RequestParam("imageFile") MultipartFile imageFile,
                                       @RequestParam("audioFile") MultipartFile audioFile,
                                       @RequestParam("videoFile") MultipartFile videoFile, Model model,HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        return fillQuestionService.directFillQuestion(fillQuestion,model,session,imageFile,audioFile,videoFile);
    }
    @GetMapping("/import/directWord/judgeQuestion")
    public String showDirectWordJudgeQuestionPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user != null) {
            List<String> uploadedSubjects = judgeQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        model.addAttribute("judgeQuestion", new JudgeQuestion());
        return "directWordJudge";
    }

    @PostMapping("/import/directWord/judgeQuestion")
    public String handleJudgeQuestionFormSubmission(@ModelAttribute JudgeQuestion judgeQuestion, @RequestParam("imageFile") MultipartFile imageFile,
                                                    @RequestParam("audioFile") MultipartFile audioFile,
                                                    @RequestParam("videoFile") MultipartFile videoFile, Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        return judgeQuestionService.directJudgeQuestion(judgeQuestion, model, session,imageFile,audioFile,videoFile);
    }

    @GetMapping("/import/directWord/selectQuestion")
    public String showDirectWordSelectQuestionPage(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user != null) {
            List<String> uploadedSubjects = selectQuestionService.getUploadedSubjectsByUserId(usersMapper.findUserIdByUsername(user.getUserName()));
            model.addAttribute("uploadedSubjects", uploadedSubjects);
        }
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        model.addAttribute("selectQuestion", new SelectQuestion());
        return "directWordSelect";
    }

    @PostMapping("/import/directWord/selectQuestion")
    public String handleSelectQuestionFormSubmission(@ModelAttribute SelectQuestion selectQuestion,  @RequestParam("imageFile") MultipartFile imageFile,
                                                     @RequestParam("audioFile") MultipartFile audioFile,
                                                     @RequestParam("videoFile") MultipartFile videoFile, Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        return selectQuestionService.directSelectQuestion(selectQuestion, model, session,imageFile,audioFile,videoFile);
    }

    @GetMapping("/import/directWord")
    public String showDirectWordPage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("contentBlocks", session.getAttribute("contentBlocks"));
        model.addAttribute("successMessage", session.getAttribute("successMessage"));
        return "directWord";
    }
    @PostMapping("/import/directWord")
    public String directImportWord(@RequestParam("wordFile") MultipartFile wordFile, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        try (InputStream inputStream = wordFile.getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);
            List<String> contentBlocks = extractContentBlocks(document);
            model.addAttribute("contentBlocks", contentBlocks);
            session.setAttribute("contentBlocks", contentBlocks);
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

                session.setAttribute("successMessage", "Excel File imported successfully!");
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
            FillQuestion fillQuestion = new FillQuestion();
            Row row = rowIterator.next();

            // 假设列按顺序为：主题、描述、答案
            String subject = getCellValueAsString(row.getCell(0));
            String description = getCellValueAsString(row.getCell(1));
            String answer = getCellValueAsString(row.getCell(2));

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
            if (!wordFile.isEmpty()) {
                InputStream inputStream = wordFile.getInputStream();
                XWPFDocument document = new XWPFDocument(inputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);

                String textContent = extractor.getText();

                processWordContent(textContent, userId, document);

                extractor.close();
                inputStream.close();
                session.setAttribute("successMessage", "Word File imported successfully!");
            } else {
                model.addAttribute("errorMessage", "File is empty!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing the file!");
        }
        model.addAttribute("user", session.getAttribute("user"));
        return "redirect:/import";
    }
    private void processWordContent(String textContent, Integer userId, XWPFDocument document) {
        String[] lines = textContent.split("\\r?\\n");
        Iterator<XWPFPictureData> pictures = document.getAllPictures().iterator();

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
                Integer flag=i+1;
                if(flag<lines.length){
                    String line1=lines[flag];
                    if (line1.startsWith("图片：")) {
                        if (pictures.hasNext()) {
                            XWPFPictureData picture = pictures.next();
                            try {
                                // 图片处理逻辑
                                currentFillQuestion.setPicFile(processImage(picture));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("没有足够的图片用于处理: " + line);
                        }
                    }
                }
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
                Integer flag=i+1;
                if(flag<lines.length){
                    String line1=lines[flag];
                    if (line1.startsWith("图片：")) {
                        if (pictures.hasNext()) {
                            XWPFPictureData picture = pictures.next();
                            try {
                                // 图片处理逻辑
                                currentJudgeQuestion.setPicFile(processImage(picture));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("没有足够的图片用于处理: " + line);
                        }
                    }
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
                Integer flag=i+1;
                if (flag<lines.length){
                    String line1=lines[flag];
                    if (line1.startsWith("图片：")) {
                        if (pictures.hasNext()) {
                            XWPFPictureData picture = pictures.next();
                            try {
                                // 图片处理逻辑
                                currentSelectQuestion.setPicFile(processImage(picture));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("没有足够的图片用于处理: " + line);
                        }
                    }
                }
                currentSelectQuestion.setUserId(userId);
                currentSelectQuestion.setUpTime(LocalDateTime.now());
                selectQuestionMapper.insert(currentSelectQuestion);
            }
        }
    }
    private String processImage(XWPFPictureData picture) throws IOException {

        byte[] bytes = picture.getData();

        String randomFileName = UUID.randomUUID().toString() + ".jpg";

        Path destinationPath = Paths.get(uploadPath, randomFileName);

        Files.write(destinationPath, bytes);
        return randomFileName;
    }




}


