package club.sword.community.enums;

/**
 * Created by XuCao on 2020/5/14
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");

    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(Integer type) {
        //遍历枚举实例，找到则返回name
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";

    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
