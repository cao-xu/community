package club.sword.community.community.service;

import club.sword.community.community.dto.PaginationDTO;
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

    public PaginationDTO list(Integer page, Integer size) {

        //创建分页DTO（questionDTO + 页码等）
        PaginationDTO paginationDTO = new PaginationDTO();
        //查询所有问题及用户信息放入DTOlist
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        Integer offset = size * (page - 1);
        //第一步，查询指定页码的问题，返回一个列表
        List<Question> questions = questionMapper.list(offset, size);
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
        //设置查询到的questionDTO
        paginationDTO.setQuestion(questionDTOList);
        Integer totalCount = questionMapper.count();
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //设置paginationDTO页面控制相关参数
        //参数：totalCount：总问题数，page：当前页码，size：页面显示数量
        paginationDTO.setPagination(totalPage, page);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //总页数
        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);
        //按照userId查询questionList
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestion(questionDTOList);
        return paginationDTO;

    }

    public QuestionDTO findById(Long id) {

        //第一步，查到问题，装入DTO
        //第二步，查到用户信息，装入DTO
        //返回DTO
        //第一步
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.findById(id);
        BeanUtils.copyProperties(question, questionDTO);
        //第二步
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
