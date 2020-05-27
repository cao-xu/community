package club.sword.community.controller;

import club.sword.community.cache.HotTagCache;
import club.sword.community.dto.PaginationDTO;
import club.sword.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class indexController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "2") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag){
        //在【拦截器】中按token查询user，若存在则将user存入session，否则直接重定向回index.html

        //查询所有问题（问题+用户头像url）存入list
        //结果存入model对象
        PaginationDTO pagination = questionService.list(search, tag, page, size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search",search);
        model.addAttribute("tag",tag);//用于分页按钮中传递
        model.addAttribute("tags",tags);
        return "index";
    }
}
