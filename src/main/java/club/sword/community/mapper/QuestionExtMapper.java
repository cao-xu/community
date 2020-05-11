package club.sword.community.mapper;

import club.sword.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    void incView(Question question);

    void incCommentCount(Question question);

    List<Question> selectRelated(Question question);
}