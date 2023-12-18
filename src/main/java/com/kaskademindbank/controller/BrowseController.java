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
import com.kaskademindbank.service.IFillQuestionService;
import com.kaskademindbank.service.IJudgeQuestionService;
import com.kaskademindbank.service.ISelectQuestionService;
import com.kaskademindbank.vo.QuestionOverview;
import com.kaskademindbank.vo.SelectedItem;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    IFillQuestionService fillQuestionService;
    @Autowired
    IJudgeQuestionService judgeQuestionService;
    @Autowired
    ISelectQuestionService selectQuestionService;
    @Value("${file.path}")
    private String uploadPath;
    @GetMapping("/browse")
    public String browseOverview(Model model, HttpSession session, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int pageSize) {
        Users user = (Users) session.getAttribute("user");
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
        session.setAttribute("user", user);
        model.addAttribute("currentPage", page);
        model.addAttribute("successDeleteMessage", session.getAttribute("successDeleteMessage"));
        return "browse_overview";
    }

    @GetMapping("/browse/{questionType}/{questionId}")
    public String browseDetail(@PathVariable String questionType,
                               @PathVariable Integer questionId,
                               Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        if ("fill".equals(questionType)) {
            FillQuestion fillQuestion = fillQuestionMapper.selectById(questionId);
            model.addAttribute("type", "fill");
            session.setAttribute("type", "fill");
            model.addAttribute("question", fillQuestion);
            String picD="/upload/"+fillQuestion.getPicFile();
            model.addAttribute("picD",picD);
            String voiD="/upload/"+fillQuestion.getVoiFile();
            model.addAttribute("voiD",voiD);
            String vidD="/upload/"+fillQuestion.getVidFile();
            model.addAttribute("vidD",vidD);
        } else if ("judge".equals(questionType)) {
            JudgeQuestion judgeQuestion = judgeQuestionMapper.selectById(questionId);
            model.addAttribute("type", "judge");
            session.setAttribute("type", "judge");
            model.addAttribute("question", judgeQuestion);
            String picD="/upload/"+judgeQuestion.getPicFile();
            model.addAttribute("picD",picD);
            String voiD="/upload/"+judgeQuestion.getVoiFile();
            model.addAttribute("voiD",voiD);
            String vidD="/upload/"+judgeQuestion.getVidFile();
            model.addAttribute("vidD",vidD);
        } else if ("select".equals(questionType)) {
            SelectQuestion selectQuestion = selectQuestionMapper.selectById(questionId);
            model.addAttribute("type", "select");
            session.setAttribute("type", "select");
            model.addAttribute("question", selectQuestion);
            String picD="/upload/"+selectQuestion.getPicFile();
            model.addAttribute("picD",picD);
            String voiD="/upload/"+selectQuestion.getVoiFile();
            model.addAttribute("voiD",voiD);
            String vidD="/upload/"+selectQuestion.getVidFile();
            model.addAttribute("vidD",vidD);
        }
        System.out.println(model.getAttribute("type"));
        session.setAttribute("questionId", questionId);
        return "browse_detail";
    }
    @GetMapping("/edit")
    public String editQuestion(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        String type = (String) session.getAttribute("type");
        Integer questionId = (Integer) session.getAttribute("questionId");
        session.setAttribute("questionId", questionId);
        if ("fill".equals(type)) {
            FillQuestion fillQuestion = fillQuestionMapper.selectById(questionId);
            model.addAttribute("type", "fillQuestion");
            model.addAttribute("fillQuestion", new FillQuestion());
            model.addAttribute("question", fillQuestion);
        } else if ("judge".equals(type)) {
            JudgeQuestion judgeQuestion = judgeQuestionMapper.selectById(questionId);
            model.addAttribute("type", "judgeQuestion");
            model.addAttribute("judgeQuestion", new JudgeQuestion());
            model.addAttribute("question", judgeQuestion);
        } else if ("select".equals(type)) {
            SelectQuestion selectQuestion = selectQuestionMapper.selectById(questionId);
            model.addAttribute("type", "selectQuestion");
            model.addAttribute("selectQuestion", new SelectQuestion());
            model.addAttribute("question", selectQuestion);
        }
        return "edit";
    }

    @GetMapping("/delete")
    public String deleteQuestion(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        String type = (String) session.getAttribute("type");
        Integer questionId = (Integer) session.getAttribute("questionId");
        session.setAttribute("questionId", questionId);
        if ("fill".equals(type)) {
            FillQuestion question = fillQuestionMapper.selectById(questionId);
            deleteFileIfExists(question.getPicFile());
            deleteFileIfExists(question.getVoiFile());
            deleteFileIfExists(question.getVidFile());
            fillQuestionMapper.deleteById(questionId);
        } else if ("judge".equals(type)) {
            JudgeQuestion question = judgeQuestionMapper.selectById(questionId);
            deleteFileIfExists(question.getPicFile());
            deleteFileIfExists(question.getVoiFile());
            deleteFileIfExists(question.getVidFile());
            judgeQuestionMapper.deleteById(questionId);
        } else if ("select".equals(type)) {
            SelectQuestion question = selectQuestionMapper.selectById(questionId);
            deleteFileIfExists(question.getPicFile());
            deleteFileIfExists(question.getVoiFile());
            deleteFileIfExists(question.getVidFile());
            selectQuestionMapper.deleteById(questionId);
        }
        session.setAttribute("successDeleteMessage", "Successfully deleting question");
        return "redirect:/browse";
    }
    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(uploadPath+filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("File deleted: " + filePath);
                } else {
                    System.err.println("Failed to delete file: " + filePath);
                }
            }
        }
    }
    @PostMapping("/edit/fillQuestion")
    public String editFillQuestion(FillQuestion fillQuestion,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   @RequestParam("audioFile") MultipartFile audioFile,
                                   @RequestParam("videoFile") MultipartFile videoFile,
                                   Model model, HttpSession session) {

        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return fillQuestionService.editFillQuestion(fillQuestion, model, session, imageFile, audioFile, videoFile);
    }
    @PostMapping("/edit/judgeQuestion")
    public String editJudgeQuestion(JudgeQuestion judgeQuestion,
                                    @RequestParam("imageFile") MultipartFile imageFile,
                                    @RequestParam("audioFile") MultipartFile audioFile,
                                    @RequestParam("videoFile") MultipartFile videoFile,
                                    Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return judgeQuestionService.editJudgeQuestion(judgeQuestion, model, session, imageFile, audioFile, videoFile);
    }
    @PostMapping("/edit/selectQuestion")
    public String editSelectQuestion(SelectQuestion selectQuestion,
                                     @RequestParam("imageFile") MultipartFile imageFile,
                                     @RequestParam("audioFile") MultipartFile audioFile,
                                     @RequestParam("videoFile") MultipartFile videoFile,
                                     Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return selectQuestionService.editSelectQuestion(selectQuestion, model, session, imageFile, audioFile, videoFile);
    }

    @GetMapping("/browse/export")
    public String exportQuestions(Model model, HttpSession session) {
        return "redirect:/browse";
    }

    @PostMapping("/browse/export")
    public ResponseEntity<byte[]> handleExportRequest(@RequestBody List<SelectedItem> selectedItems, Model model, HttpSession session) {
        // 获取用户信息
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);

        // 创建一个新的 Word 文档
        try (XWPFDocument document = new XWPFDocument()) {
            Integer index = 0;
            // 遍历每个选中的题目
            for (SelectedItem selectedItem : selectedItems) {
                index++;
                // 根据 type 和 id 从数据库中获取对应的题目信息
                addQuestionToDocument(document, selectedItem.getType(), Integer.valueOf(selectedItem.getId()), index);
            }

            // 保存 Word 文档到字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.write(byteArrayOutputStream);

            // 构建 ResponseEntity，设置响应头和内容
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document", StandardCharsets.UTF_8));
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("exported_document.docx", StandardCharsets.UTF_8).build());

            // 返回字节数组和响应头
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，这里可以根据具体情况返回错误信息或者其他响应
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 添加题目信息到 Word 文档的辅助方法
    private void addQuestionToDocument(XWPFDocument document, String type, Integer id, Integer index) {
        // 根据 type 和 id 从数据库中获取对应的题目信息
        if ("Fill".equals(type)) {
            FillQuestion question = fillQuestionMapper.selectById(id);
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("第" + index + "题");
            run.addCarriageReturn();
            run.setText("主题: " + question.getSubject());
            run.addCarriageReturn();
            run.setText("题目: " + question.getDescription());
            run.addCarriageReturn();
            run.setText("答案: " + question.getAnswer());
            if(question.getPicFile()!=null){
                String picPath = question.getPicFile();
                insertImage(document, picPath);
            }
            run.addCarriageReturn();
        } else if ("Judge".equals(type)) {
            JudgeQuestion question = judgeQuestionMapper.selectById(id);
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("第" + index + "题");
            run.addCarriageReturn();
            run.setText("主题: " + question.getSubject());
            run.addCarriageReturn();
            run.setText("题目: " + question.getDescription());
            run.addCarriageReturn();
            run.setText("答案: " + question.getAnswer());
            if(question.getPicFile()!=null){
                String picPath = question.getPicFile();
                insertImage(document, picPath);
            }
            run.addCarriageReturn();
        } else if ("Select".equals(type)) {
            SelectQuestion question = selectQuestionMapper.selectById(id);
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("第" + index + "题");
            run.addCarriageReturn();
            run.setText("主题: " + question.getSubject());
            run.addCarriageReturn();
            run.setText("题目: " + question.getDescription());
            run.addCarriageReturn();
            run.setText("选项A: " + question.getAnswerA());
            run.addCarriageReturn();
            run.setText("选项B: " + question.getAnswerB());
            run.addCarriageReturn();
            run.setText("选项C: " + question.getAnswerC());
            run.addCarriageReturn();
            run.setText("选项D: " + question.getAnswerD());
            run.addCarriageReturn();
            run.setText("答案: " + question.getAnswer());
            if(question.getPicFile()!=null){
                String picPath = question.getPicFile();
                insertImage(document, picPath);
            }
            run.addCarriageReturn();
        }

    }
    // 插入图片到文档中
    private void insertImage(XWPFDocument document, String picPath) {
        try {
            // 图片存储在项目根目录下的一级目录upload
            byte[] bytes = Files.readAllBytes(Path.of(uploadPath, picPath));


            // 使用 Apache Tika 获取文件的真实类型
            Tika tika = new Tika();
            String mimeType = tika.detect(bytes);

            // 根据文件类型设置格式
            int format;
            if (mimeType.startsWith("image/jpeg")) {
                format = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (mimeType.startsWith("image/png")) {
                format = XWPFDocument.PICTURE_TYPE_PNG;
            } else {
                format = XWPFDocument.PICTURE_TYPE_JPEG;
            }

            int originalWidth = ImageIO.read(new ByteArrayInputStream(bytes)).getWidth();
            int originalHeight = ImageIO.read(new ByteArrayInputStream(bytes)).getHeight();
            double scale = 300.0 / originalWidth;
            int scaledWidth = 300;
            int scaledHeight = (int) (originalHeight * scale);
            document.createParagraph().createRun().addPicture(new ByteArrayInputStream(bytes), format, "image", Units.toEMU(scaledWidth), Units.toEMU(scaledHeight));
            // 添加换行
            document.createParagraph().createRun().addBreak();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
