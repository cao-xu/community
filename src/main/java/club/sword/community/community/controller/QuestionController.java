package club.sword.community.community.controller;

import club.sword.community.community.dto.QuestionDTO;
import club.sword.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model) {
        //按照传入参数问题id查询问题DTO，返回给页面展示
        Long questionId = null;
        questionId = Long.parseLong(id);
        QuestionDTO questionDTO = questionService.findById(questionId);
        model.addAttribute("question" , questionDTO);
        return "question";
    }
}
