package club.sword.community.community.controller;

import club.sword.community.community.dto.AccessTokenDTO;
import club.sword.community.community.dto.GithubUser;
import club.sword.community.community.mapper.UserMapper;
import club.sword.community.community.model.User;
import club.sword.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@Controller
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
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null){

            //将获取的github用户信息保存到数据库：
            // controller——>service——>DAO
            User user = new User();
            //生成一个随机字符串当做token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //使用MyBatis实现插入用户信息
            userMapper.insert(user);//类似于UserDAO，不过更简洁、方便
            //登录成功，写cookie和session
            request.getSession().setAttribute("user", githubUser);
            //返回根目录，index.html
            return "redirect:/";
        }else{
            //登录失败，重新登录
            return "redirect:/";
        }

    }

}
