package club.sword.community.dto;

import lombok.Data;

/**
 * Created by XuCao on 2020/5/19
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String sort;
    private Long time;
    private String tag;
    private Integer page;
    private Integer size;
}
