package club.sword.community.dto;

import lombok.Data;

import java.util.List;

@Data//get(),set()
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
