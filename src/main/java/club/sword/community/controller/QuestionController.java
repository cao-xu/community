package club.sword.community.controller;

import club.sword.community.dto.CommentDTO;
import club.sword.community.dto.QuestionDTO;
import club.sword.community.enums.CommentTypeEnum;
import club.sword.community.exception.CustomizeErrorCode;
import club.sword.community.exception.CustomizeException;
import club.sword.community.service.CommentService;
import club.sword.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model) {
        Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }
        //按照传入参数问题id查询问题DTO，返回给页面展示
        QuestionDTO questionDTO = questionService.findById(questionId);
        //查询评论（以问题id，评论类型属于问题的（非二级评论））
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(questionId);
        model.addAttribute("question" , questionDTO);
        model.addAttribute("comments", comments);
        return "question";
    }
}
