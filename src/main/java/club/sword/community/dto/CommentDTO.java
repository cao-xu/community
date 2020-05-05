package club.sword.community.dto;

import club.sword.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;//父类id（问题id，回复id）
    private Integer type;//父类类型
    private Long commentator;//回复用户id
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;//用户
}
