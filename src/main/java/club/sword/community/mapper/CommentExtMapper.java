package club.sword.community.mapper;

import club.sword.community.model.Comment;

public interface CommentExtMapper {
    //给评论增加评论数
    int incCommentCount(Comment comment);
}
