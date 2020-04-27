package club.sword.community.community.dto;

import club.sword.community.community.model.User;
import lombok.Data;

//一种数据传输层类，主要用于数据的拼接
//这里的QuestionDTO目的是保存Question加上User中的avatar_url字段，等价与联合查询出来的结果
//这种DTO层的好处是对比之前联合查询，可以减少声明各种model、bean的数量，灵活一些，
// model层只存放最基本的model
@Data
public class QuestionDTO {
    //Question
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    //User
    private User user;
}
