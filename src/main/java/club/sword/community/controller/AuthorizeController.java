package club.sword.community.controller;

import club.sword.community.dto.AccessTokenDTO;
import club.sword.community.dto.GithubUser;
import club.sword.community.model.User;
import club.sword.community.provider.GithubProvider;
import club.sword.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //使用Autowired装配到该类的实例对象
    //@Autowired表示被修饰的类需要注入对象,
    //spring会扫描所有被@Autowired标注的类,然后根据 类型在ioc容器中找到匹配的类注入

    //使用@Value将application。properties中的值装配到变量
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //获取access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //根据accessToken获取用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null){

            //将获取的github用户信息保存到数据库：
            // controller——>service——>DAO
            User user = new User();
            //生成一个随机字符串当做token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());//头像url
            //使用MyBatis实现插入用户信息
            userService.createOrUpdate(user);
            //登录成功，写cookie和session
            //在cookie中设置一个token
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
            //返回根目录，index.html
            return "redirect:/";
        }else{
            log.error("callback get github error,{}", githubUser);
            //登录失败，重新登录
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //第一步，删除session中的user对象
        request.getSession().removeAttribute("user");
        //第二步，删除cookie中的token对象
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);//立即删除
        response.addCookie(cookie);
        return "redirect:/";
    }

}
