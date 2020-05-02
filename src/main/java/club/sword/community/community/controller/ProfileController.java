package club.sword.community.community.controller;

import club.sword.community.community.dto.PaginationDTO;
import club.sword.community.community.model.User;
import club.sword.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable("action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "2") Integer size){
        //在拦截器查询是否登录后，再查询登录的用户
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if (action.equals("questions")){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            //按照当前用户id查询 问题list和分页参数
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }else if (action.equals("replies")){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");

        }
        return "profile";
    }
}
