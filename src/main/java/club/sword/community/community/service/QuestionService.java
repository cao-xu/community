package club.sword.community.community.service;

import club.sword.community.community.dto.QuestionDTO;
import club.sword.community.community.mapper.QuestionMapper;
import club.sword.community.community.mapper.UserMapper;
import club.sword.community.community.model.Question;
import club.sword.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> list() {

        //查询所有问题及用户信息放入DTOlist
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //第一步，查询所有问题list
        List<Question> questions = questionMapper.list();
        //第二步，查询问题对应的用户
        for (Question question:questions){
            User user =userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //插入user
            questionDTO.setUser(user);
            //插入question,BeanUtils将原model中的元素插入目标model
            BeanUtils.copyProperties(question, questionDTO);
            questionDTOList.add(questionDTO);
        }
        //返回查询结果list
        return questionDTOList;
    }
}
