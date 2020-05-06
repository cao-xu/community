package club.sword.community.enums;

public enum CommentTypeEnum {
    QUESTION(1),//回复的是问题
    COMMENT(2);//回复的是评论
    private Integer type;


    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }//初始函数

    public static boolean isExist(Integer type) {
        //判断评论类型是否正常
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
