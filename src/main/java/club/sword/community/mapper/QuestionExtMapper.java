package club.sword.community.mapper;

import club.sword.community.model.Question;

public interface QuestionExtMapper {

    void incView(Question question);

    void incCommentCount(Question question);
}