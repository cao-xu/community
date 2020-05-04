package club.sword.community.service;

import club.sword.community.dto.PaginationDTO;
import club.sword.community.dto.QuestionDTO;
import club.sword.community.exception.CustomizeErrorCode;
import club.sword.community.exception.CustomizeException;
import club.sword.community.mapper.QuestionMapper;
import club.sword.community.mapper.UserMapper;
import club.sword.community.model.Question;
import club.sword.community.model.QuestionExample;
import club.sword.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //第二步，查询问题对应的用户
        for (Question question:questions){
            User user =userMapper.selectByPrimaryKey(question.getCreator());
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
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
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

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);

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
        //设置查询语句为：用户id
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
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
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question, questionDTO);
        //第二步
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        //若带有问题id，则更新内容
        //若不带有问题id，则插入新问题
        if (question.getId() == null){
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //不可以使用insert(),因为id是自动生成的，为空，所以用部分插入
            questionMapper.insertSelective(question);
        }else {
            //更新
            //先查询是否存在
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            if (dbQuestion != null){
                question.setGmtModified(System.currentTimeMillis());
                //2020.5.2
                // 缺少对CURD操作是否成功的判断，应该判断是否执行成功，再进行下一步操作
                QuestionExample example = new QuestionExample();
                example.createCriteria()
                        .andCreatorEqualTo(question.getCreator());
                int updated = questionMapper.updateByExampleSelective(question, example);
                if (updated != 1){
                    //2020.5.4
                    //修复判断CURD操作是否成功，抛出异常
                    //未能更新成功，返回错误消息到error.html
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
            }
            if (dbQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            if (dbQuestion.getCreator().longValue() != question.getCreator().longValue()){
                throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
            }

        }
    }
}
