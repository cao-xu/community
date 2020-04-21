package club.sword.community.community.controller;

import club.sword.community.community.dto.AccessTokenDTO;
import club.sword.community.community.dto.GithubUser;
import club.sword.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("d06bcd194bb01844461f");
        accessTokenDTO.setClient_secret("bf4ee170e7934a11f6cb8cf2980958275731a84a");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }

}
