package club.sword.community.dto;

import lombok.Data;

/**
 * Created by XuCao on 2020/5/27
 */
//热门标签：名称+优先级
@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}
