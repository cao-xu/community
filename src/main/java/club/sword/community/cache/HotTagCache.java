package club.sword.community.cache;

import club.sword.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by XuCao on 2020/5/27
 */
@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    //设计优先队列使用“小顶堆”实现
    //优先级数越小，层数越高，控制堆的size为n实现top n，堆中只留下最大的n个元素
    public void updateTags(Map<String, Integer> tags) {
        int max = 10;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        //依次将所有map元素与“小顶堆”堆顶最小元素比较，找出最热的top max个元素
        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                //取出堆顶元素，即最小值
                HotTagDTO minHot = priorityQueue.peek();
                if (hotTagDTO.compareTo(minHot) > 0) {
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<String> sortedTags = new ArrayList<>();

        //从堆中删除并取出堆顶元素
        HotTagDTO poll = priorityQueue.poll();
        while (poll != null) {
            //在列表头index“0”位置插入，原来的元素向后移动，实现“逆序”由大到小排序
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
//        System.out.println(hots);
    }
}
