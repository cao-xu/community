package club.sword.community.service;


import club.sword.community.enums.CommentTypeEnum;
import club.sword.community.exception.CustomizeErrorCode;
import club.sword.community.exception.CustomizeException;
import club.sword.community.mapper.CommentExtMapper;
import club.sword.community.mapper.CommentMapper;
import club.sword.community.mapper.QuestionExtMapper;
import club.sword.community.mapper.QuestionMapper;
import club.sword.community.model.Comment;
import club.sword.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;//执行脚本，MBG自动生成的

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    public void insert(Comment comment) {
        //判断parentId是否为空
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //判断typeId是否合法
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复的是“评论”
            // 查询要回复的评论是否存在
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 查询回复对应的问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 部分插入新的回复
            commentMapper.insertSelective(comment);
            // 给“评论”增加回复数（评论也有评论数这一属性）
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        }else {
            // 回复的是“问题”
            //查询要回复的问题是否存在
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);//设置该回复的回复数为0
            //插入回复
            commentMapper.insertSelective(comment);
            //给“问题”增加回复数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
