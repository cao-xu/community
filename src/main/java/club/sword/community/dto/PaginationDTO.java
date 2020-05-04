package club.sword.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class PaginationDTO {
    //问题列表
    private List<QuestionDTO> question;
    //分页控制参数
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;//选择的当前页面
    private List<Integer> pages = new ArrayList<>();//显示的页码数组
    private Integer totalPage;//总页数


    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
        this.page = page;

        //设置显示pages数组
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}