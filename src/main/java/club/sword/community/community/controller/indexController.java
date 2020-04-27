package club.sword.community.community.controller;

import club.sword.community.community.dto.QuestionDTO;
import club.sword.community.community.mapper.UserMapper;
import club.sword.community.community.model.User;
import club.sword.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        HttpServletRequest request){
        //获取cookie中的token
        //按token查询user，若存在则将user存入session，否则直接重定向回index.html
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        //查询所有问题（问题+用户头像url）存入list
        //结果存入model对象
        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("question", questionList);
        return "index";
    }
}
