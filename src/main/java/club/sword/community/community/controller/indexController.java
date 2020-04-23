package club.sword.community.community.controller;

import club.sword.community.community.mapper.UserMapper;
import club.sword.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        //获取cookie中的token
        //按token查询user，若存在则将user存入session，否则直接重定向回index.html
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                request.getSession().setAttribute("user",user);
                break;
            }
        }
        return "index";
    }
}
